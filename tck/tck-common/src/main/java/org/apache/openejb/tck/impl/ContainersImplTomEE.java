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
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.openejb.tck.impl;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Flushable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.openejb.NoSuchApplicationException;
import org.apache.openejb.OpenEJBException;
import org.apache.openejb.UndeployException;
import org.apache.openejb.assembler.Deployer;
import org.apache.openejb.client.RemoteInitialContextFactory;
import org.apache.openejb.config.RemoteServer;
import org.jboss.testharness.api.DeploymentException;
import org.jboss.testharness.spi.Containers;

/**
 * @version $Rev$ $Date$
 */
public class ContainersImplTomEE implements Containers {
    private static int count = 0;
    private final RemoteServer server;
    private Deployer deployer = null;
    private static final String tmpDir = System.getProperty("java.io.tmpdir");
    public ContainersImplTomEE() {
        System.out.println("Initialized ContainersImplTomEE " + (++count));
        server = new RemoteServer(10, true);
    }
    private Deployer lookup() {
        Properties props = new Properties();
        props.put(Context.INITIAL_CONTEXT_FACTORY, RemoteInitialContextFactory.class.getName());
        props.put(Context.PROVIDER_URL, "http://localhost:8080/openejb/ejb");
        try {
            InitialContext context = new InitialContext(props);
            return (Deployer) context.lookup("openejb/DeployerBusinessRemote");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public boolean deploy(InputStream archive, String name) throws IOException {
        System.out.println("Deploying " + archive + " with name " + name);
        String fileName = getName(name);
        System.out.println(fileName);
        writeToFile(fileName, archive);
        try {
            if (deployer == null) {
                deployer = lookup();
            }
            deployer.deploy(fileName);
        } catch (OpenEJBException e) {
            throw new RuntimeException(e);
        }
        return true;
    }
    private void writeToFile(String fileName, InputStream archive) {
        try {
            FileOutputStream fos = new FileOutputStream(fileName);
            byte[] buffer = new byte[4096];
            int bytesRead = -1;
            while ((bytesRead = archive.read(buffer)) > -1) {
                fos.write(buffer, 0, bytesRead);
            }
            Util.close(fos);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private String getName(String name) {
        return tmpDir + File.separator + name;
    }
    @Override
    public DeploymentException getDeploymentException() {
        System.out.println("getDeploymentException()");
        return new DeploymentException("There is some problem");
    }
    @Override
    public void undeploy(String name) throws IOException {
        System.out.println("Undeploying " + name);
        try {
            deployer.undeploy(getName(name));
        } catch (UndeployException e) {
            throw new RuntimeException(e);
        } catch (NoSuchApplicationException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setup() throws IOException {
        System.out.println("Setup called");
        server.start();
    }
    @Override
    public void cleanup() throws IOException {
        System.out.println("Cleanup called");
        server.stop();
    }
    private static final class Util {
        static void close(Closeable closeable) throws IOException {
            if (closeable == null)
                return;
            try {
                if (closeable instanceof Flushable) {
                    ((Flushable) closeable).flush();
                }
            } catch (IOException e) {
                // no-op
            }
            try {
                closeable.close();
            } catch (IOException e) {
                // no-op
            }
        }
    }
}