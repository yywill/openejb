[INFO] Scanning for projects...
[INFO]                                                                         
[INFO] ------------------------------------------------------------------------
[INFO] Building OpenEJB :: Examples :: Movies Complete 1.0
[INFO] ------------------------------------------------------------------------
[INFO] 
[INFO] --- maven-clean-plugin:2.4.1:clean (default-clean) @ movies-complete ---
[INFO] Deleting /Users/dblevins/examples/movies-complete/target
[INFO] 
[INFO] --- maven-resources-plugin:2.4.3:resources (default-resources) @ movies-complete ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] Copying 1 resource
[INFO] 
[INFO] --- maven-compiler-plugin:2.3.2:compile (default-compile) @ movies-complete ---
[INFO] Compiling 4 source files to /Users/dblevins/examples/movies-complete/target/classes
[INFO] 
[INFO] --- maven-resources-plugin:2.4.3:testResources (default-testResources) @ movies-complete ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory /Users/dblevins/examples/movies-complete/src/test/resources
[INFO] 
[INFO] --- maven-compiler-plugin:2.3.2:testCompile (default-testCompile) @ movies-complete ---
[INFO] Compiling 1 source file to /Users/dblevins/examples/movies-complete/target/test-classes
[INFO] 
[INFO] --- maven-surefire-plugin:2.7.2:test (default-test) @ movies-complete ---
[INFO] Surefire report directory: /Users/dblevins/examples/movies-complete/target/surefire-reports

-------------------------------------------------------
 T E S T S
-------------------------------------------------------
Running org.superbiz.injection.tx.MoviesTest
Apache OpenEJB 4.0.0-beta-1    build: 20111002-04:06
http://openejb.apache.org/
INFO - openejb.home = /Users/dblevins/examples/movies-complete
INFO - openejb.base = /Users/dblevins/examples/movies-complete
INFO - Using 'javax.ejb.embeddable.EJBContainer=true'
INFO - Configuring Service(id=Default Security Service, type=SecurityService, provider-id=Default Security Service)
INFO - Configuring Service(id=Default Transaction Manager, type=TransactionManager, provider-id=Default Transaction Manager)
INFO - Configuring Service(id=movieDatabase, type=Resource, provider-id=Default JDBC Database)
INFO - Found EjbModule in classpath: /Users/dblevins/examples/movies-complete/target/classes
INFO - Found EjbModule in classpath: /Users/dblevins/examples/movies-complete/target/test-classes
INFO - Beginning load: /Users/dblevins/examples/movies-complete/target/classes
INFO - Beginning load: /Users/dblevins/examples/movies-complete/target/test-classes
INFO - Configuring enterprise application: /Users/dblevins/examples/movies-complete
INFO - Configuring Service(id=Default Stateful Container, type=Container, provider-id=Default Stateful Container)
INFO - Auto-creating a container for bean Movies: Container(type=STATEFUL, id=Default Stateful Container)
INFO - Configuring Service(id=Default Stateless Container, type=Container, provider-id=Default Stateless Container)
INFO - Auto-creating a container for bean NoTransactionBean: Container(type=STATELESS, id=Default Stateless Container)
INFO - Configuring Service(id=Default Managed Container, type=Container, provider-id=Default Managed Container)
INFO - Auto-creating a container for bean org.superbiz.injection.tx.MoviesTest: Container(type=MANAGED, id=Default Managed Container)
INFO - Configuring PersistenceUnit(name=movie-unit)
INFO - Auto-creating a Resource with id 'movieDatabaseNonJta' of type 'DataSource for 'movie-unit'.
INFO - Configuring Service(id=movieDatabaseNonJta, type=Resource, provider-id=movieDatabase)
INFO - Adjusting PersistenceUnit movie-unit <non-jta-data-source> to Resource ID 'movieDatabaseNonJta' from 'movieDatabaseUnmanaged'
INFO - Enterprise application "/Users/dblevins/examples/movies-complete" loaded.
INFO - Assembling app: /Users/dblevins/examples/movies-complete
INFO - PersistenceUnit(name=movie-unit, provider=org.apache.openjpa.persistence.PersistenceProviderImpl) - provider time 412ms
INFO - Jndi(name="java:global/movies-complete/Movies!org.superbiz.injection.tx.Movies")
INFO - Jndi(name="java:global/movies-complete/Movies")
INFO - Jndi(name="java:global/movies-complete/NoTransactionBean!org.superbiz.injection.tx.MoviesTest$Caller")
INFO - Jndi(name="java:global/movies-complete/NoTransactionBean")
INFO - Jndi(name="java:global/movies-complete/TransactionBean!org.superbiz.injection.tx.MoviesTest$Caller")
INFO - Jndi(name="java:global/movies-complete/TransactionBean")
INFO - Jndi(name="java:global/EjbModule1583515396/org.superbiz.injection.tx.MoviesTest!org.superbiz.injection.tx.MoviesTest")
INFO - Jndi(name="java:global/EjbModule1583515396/org.superbiz.injection.tx.MoviesTest")
INFO - Created Ejb(deployment-id=Movies, ejb-name=Movies, container=Default Stateful Container)
INFO - Created Ejb(deployment-id=TransactionBean, ejb-name=TransactionBean, container=Default Stateless Container)
INFO - Created Ejb(deployment-id=NoTransactionBean, ejb-name=NoTransactionBean, container=Default Stateless Container)
INFO - Created Ejb(deployment-id=org.superbiz.injection.tx.MoviesTest, ejb-name=org.superbiz.injection.tx.MoviesTest, container=Default Managed Container)
INFO - Started Ejb(deployment-id=Movies, ejb-name=Movies, container=Default Stateful Container)
INFO - Started Ejb(deployment-id=TransactionBean, ejb-name=TransactionBean, container=Default Stateless Container)
INFO - Started Ejb(deployment-id=NoTransactionBean, ejb-name=NoTransactionBean, container=Default Stateless Container)
INFO - Started Ejb(deployment-id=org.superbiz.injection.tx.MoviesTest, ejb-name=org.superbiz.injection.tx.MoviesTest, container=Default Managed Container)
INFO - Deployed Application(path=/Users/dblevins/examples/movies-complete)
INFO - EJBContainer already initialized.  Call ejbContainer.close() to allow reinitialization
Tests run: 2, Failures: 0, Errors: 0, Skipped: 0, Time elapsed: 2.375 sec

Results :

Tests run: 2, Failures: 0, Errors: 0, Skipped: 0

[INFO] 
[INFO] --- maven-jar-plugin:2.3.1:jar (default-jar) @ movies-complete ---
[INFO] Building jar: /Users/dblevins/examples/movies-complete/target/movies-complete-1.0.jar
[INFO] 
[INFO] --- maven-install-plugin:2.3.1:install (default-install) @ movies-complete ---
[INFO] Installing /Users/dblevins/examples/movies-complete/target/movies-complete-1.0.jar to /Users/dblevins/.m2/repository/org/superbiz/movies-complete/1.0/movies-complete-1.0.jar
[INFO] Installing /Users/dblevins/examples/movies-complete/pom.xml to /Users/dblevins/.m2/repository/org/superbiz/movies-complete/1.0/movies-complete-1.0.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5.581s
[INFO] Finished at: Fri Oct 28 17:11:17 PDT 2011
[INFO] Final Memory: 14M/81M
[INFO] ------------------------------------------------------------------------
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
    package org.superbiz.injection.tx;
    
    import javax.interceptor.AroundInvoke;
    import javax.interceptor.InvocationContext;
    
    /**
     * @version $Revision$ $Date$
     */
    public class AddInterceptor {
    
        @AroundInvoke
        public Object invoke(InvocationContext context) throws Exception {
            // Log Add
            return context.proceed();
        }
    }/**
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
    package org.superbiz.injection.tx;
    
    import javax.interceptor.AroundInvoke;
    import javax.interceptor.InvocationContext;
    
    /**
     * @version $Revision$ $Date$
     */
    public class DeleteInterceptor {
    
        @AroundInvoke
        public Object invoke(InvocationContext context) throws Exception {
            // Log Delete
            return context.proceed();
        }
    }
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
    package org.superbiz.injection.tx;
    
    import javax.persistence.Entity;
    
    @Entity
    public class Movie {
    
        private String director;
        private String title;
        private int year;
    
        public Movie() {
        }
    
        public Movie(String director, String title, int year) {
            this.director = director;
            this.title = title;
            this.year = year;
        }
    
        public String getDirector() {
            return director;
        }
    
        public void setDirector(String director) {
            this.director = director;
        }
    
        public String getTitle() {
            return title;
        }
    
        public void setTitle(String title) {
            this.title = title;
        }
    
        public int getYear() {
            return year;
        }
    
        public void setYear(int year) {
            this.year = year;
        }
    
    
    }
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
    package org.superbiz.injection.tx;
    
    import javax.annotation.security.PermitAll;
    import javax.annotation.security.RolesAllowed;
    import javax.ejb.Stateful;
    import javax.ejb.TransactionAttribute;
    import javax.ejb.TransactionAttributeType;
    import javax.interceptor.Interceptors;
    import javax.persistence.EntityManager;
    import javax.persistence.PersistenceContext;
    import javax.persistence.PersistenceContextType;
    import javax.persistence.Query;
    import java.util.List;
    
    import static javax.ejb.TransactionAttributeType.MANDATORY;
    
    //START SNIPPET: code
    @Stateful
    public class Movies {
    
        @PersistenceContext(unitName = "movie-unit", type = PersistenceContextType.TRANSACTION)
        private EntityManager entityManager;
    
        @RolesAllowed({"Employee", "Manager"})
        @TransactionAttribute(TransactionAttributeType.REQUIRED)
        @Interceptors(AddInterceptor.class)
        public void addMovie(Movie movie) throws Exception {
            entityManager.persist(movie);
        }
    
        @RolesAllowed({"Manager"})
        @TransactionAttribute(TransactionAttributeType.MANDATORY)
        @Interceptors(DeleteInterceptor.class)
        public void deleteMovie(Movie movie) throws Exception {
            entityManager.remove(movie);
        }
    
        @PermitAll
        @TransactionAttribute(TransactionAttributeType.SUPPORTS)
        public List<Movie> getMovies() throws Exception {
            Query query = entityManager.createQuery("SELECT m from Movie as m");
            return query.getResultList();
        }
    }
    //END SNIPPET: code
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
    package org.superbiz.injection.tx;
    
    import javax.interceptor.AroundInvoke;
    import javax.interceptor.InvocationContext;
    
    /**
     * @version $Revision$ $Date$
     */
    public class ReadInterceptor {
    
        @AroundInvoke
        public Object invoke(InvocationContext context) throws Exception {
            // Log Delete
            return context.proceed();
        }
    }
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
    package org.superbiz.injection.tx;
    
    import junit.framework.TestCase;
    
    import javax.annotation.security.RunAs;
    import javax.ejb.EJB;
    import javax.ejb.Stateless;
    import javax.ejb.TransactionAttribute;
    import javax.ejb.TransactionAttributeType;
    import javax.ejb.embeddable.EJBContainer;
    import java.util.List;
    import java.util.Properties;
    import java.util.concurrent.Callable;
    
    import static javax.ejb.TransactionAttributeType.REQUIRES_NEW;
    
    /**
     * See the transaction-rollback example as it does the same thing
     * via UserTransaction and shows more techniques for rollback 
     */
    //START SNIPPET: code
    public class MoviesTest extends TestCase {
    
        @EJB
        private Movies movies;
    
        @EJB(beanName = "TransactionBean")
        private Caller transactionalCaller;
    
        @EJB(beanName = "NoTransactionBean")
        private Caller nonTransactionalCaller;
    
        protected void setUp() throws Exception {
            final Properties p = new Properties();
            p.put("movieDatabase", "new://Resource?type=DataSource");
            p.put("movieDatabase.JdbcDriver", "org.hsqldb.jdbcDriver");
            p.put("movieDatabase.JdbcUrl", "jdbc:hsqldb:mem:moviedb");
    
            EJBContainer.createEJBContainer(p).getContext().bind("inject", this);
        }
    
        private void doWork() throws Exception {
    
            movies.addMovie(new Movie("Quentin Tarantino", "Reservoir Dogs", 1992));
            movies.addMovie(new Movie("Joel Coen", "Fargo", 1996));
            movies.addMovie(new Movie("Joel Coen", "The Big Lebowski", 1998));
    
            List<Movie> list = movies.getMovies();
            assertEquals("List.size()", 3, list.size());
    
            for (Movie movie : list) {
                movies.deleteMovie(movie);
            }
    
            assertEquals("Movies.getMovies()", 0, movies.getMovies().size());
        }
    
        public void testWithTransaction() throws Exception {
            transactionalCaller.call(new Callable() {
                public Object call() throws Exception {
                    doWork();
                    return null;
                }
            });
        }
    
        public void testWithoutTransaction() throws Exception {
            try {
                nonTransactionalCaller.call(new Callable() {
                    public Object call() throws Exception {
                        doWork();
                        return null;
                    }
                });
                fail("The Movies bean should be using TransactionAttributeType.MANDATORY");
            } catch (javax.ejb.EJBException e) {
                // good, our Movies bean is using TransactionAttributeType.MANDATORY as we want
            }
        }
    
    
        public static interface Caller {
            public <V> V call(Callable<V> callable) throws Exception;
        }
    
        /**
         * This little bit of magic allows our test code to execute in
         * the scope of a container controlled transaction.
         */
        @Stateless
        @RunAs("Manager")
        @TransactionAttribute(REQUIRES_NEW)
        public static class TransactionBean implements Caller {
    
            public <V> V call(Callable<V> callable) throws Exception {
                return callable.call();
            }
    
        }
    
        @Stateless
        @RunAs("Manager")
        @TransactionAttribute(TransactionAttributeType.NEVER)
        public static class NoTransactionBean implements Caller {
    
            public <V> V call(Callable<V> callable) throws Exception {
                return callable.call();
            }
    
        }
    
    }
    //END SNIPPET: code