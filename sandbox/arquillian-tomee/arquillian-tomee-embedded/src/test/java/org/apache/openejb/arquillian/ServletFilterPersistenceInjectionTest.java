/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.openejb.arquillian;

import org.apache.commons.lang.StringUtils;
import org.jboss.arquillian.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ArchivePaths;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.descriptor.api.Descriptors;
import org.jboss.shrinkwrap.descriptor.api.spec.servlet.web.WebAppDescriptor;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.Resource;
import javax.persistence.*;
import javax.servlet.*;
import javax.transaction.UserTransaction;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

@RunWith(Arquillian.class)
public class ServletFilterPersistenceInjectionTest {

    public static final String TEST_NAME = ServletFilterPersistenceInjectionTest.class.getSimpleName();

    @Test
    public void transactionInjectionShouldSucceed() throws Exception {
        final String expectedOutput = "Transaction injection successful";
        validateTest(expectedOutput);
    }

    @Test
    public void persistentContextInjectionShouldSucceed() throws Exception {
        final String expectedOutput = "Transaction manager injection successful";
        validateTest(expectedOutput);
    }

    @Test
    public void persistenceUnitInjectionShouldSucceed() throws Exception {
        final String expectedOutput = "Transaction manager factory injection successful";
        validateTest(expectedOutput);
    }

    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        WebAppDescriptor descriptor = Descriptors.create(WebAppDescriptor.class)
                .version("3.0")
                .filter(PersistenceServletFilter.class, "/" + TEST_NAME);

        WebArchive archive = ShrinkWrap.create(WebArchive.class, TEST_NAME + ".war")
                .addClass(PersistenceServletFilter.class)
                .addClass(Address.class)
                .addAsManifestResource("persistence.xml", ArchivePaths.create("persistence.xml"))
                .setWebXML(new StringAsset(descriptor.exportAsString()))
                .addAsWebResource(EmptyAsset.INSTANCE, ArchivePaths.create("beans.xml"));

        System.err.println(descriptor.exportAsString());

        return archive;
    }

    public static class PersistenceServletFilter implements Filter {

        @Resource
        private UserTransaction transaction;

        @PersistenceUnit
        private EntityManagerFactory entityMgrFactory;

        @PersistenceContext
        private EntityManager entityManager;

        private FilterConfig config;

        public void init(FilterConfig config) {
            this.config = config;
        }

        public void destroy() {
        }

        @Override
        public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
            String name = req.getParameter("name");
            if (StringUtils.isEmpty(name)) {
                name = "OpenEJB";
            }

            if (transaction != null) {
                try {
                    transaction.begin();
                    transaction.commit();
                    resp.getOutputStream().println("Transaction injection successful");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (entityManager != null) {
                Address a = new Address();
                try {
                    entityManager.contains(a);
                    resp.getOutputStream().println("Transaction manager injection successful");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            if (entityMgrFactory != null) {
                Address a = new Address();
                try {
                    EntityManager em = entityMgrFactory.createEntityManager();
                    em.contains(a);
                    resp.getOutputStream().println("Transaction manager factory injection successful");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    @Entity
    public static class Address {
        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getZip() {
            return zip;
        }

        public void setZip(String zip) {
            this.zip = zip;
        }

        private String street = "123 Lakeview St.", city = "Paradise", state = "ZZ", zip = "00000";

        public String toString() {
            return "Street: " + street + ", City: " + city + ", State: " + state + ", Zip: " + zip;
        }
    }

    private void validateTest(String expectedOutput) throws IOException {
        final InputStream is = new URL("http://localhost:9080/" + TEST_NAME + "/" + TEST_NAME).openStream();
        final ByteArrayOutputStream os = new ByteArrayOutputStream();

        int bytesRead = -1;
        byte[] buffer = new byte[8192];
        while ((bytesRead = is.read(buffer)) > -1) {
            os.write(buffer, 0, bytesRead);
        }

        is.close();
        os.close();

        String output = new String(os.toByteArray(), "UTF-8");
        assertNotNull("Response shouldn't be null", output);
        assertTrue("Output should contain: " + expectedOutput, output.contains(expectedOutput));
    }

}


