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
package org.apache.openejb.test.entity.bmp;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityContext;
import javax.ejb.RemoveException;
import javax.naming.InitialContext;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;

import org.apache.openejb.test.TestFailureException;
import org.apache.openejb.test.stateful.BasicStatefulHome;
import org.apache.openejb.test.stateful.BasicStatefulObject;
import org.apache.openejb.test.stateless.BasicStatelessHome;
import org.apache.openejb.test.stateless.BasicStatelessObject;

/**
 * 
 * @author <a href="mailto:david.blevins@visi.com">David Blevins</a>
 * @author <a href="mailto:Richard@Monson-Haefel.com">Richard Monson-Haefel</a>
 */
public class EncBmpBean implements javax.ejb.EntityBean{
    
    private int primaryKey;
    private String firstName;
    private String lastName;
    private EntityContext ejbContext;
    
    
    //=============================
    // Home interface methods
    //    
    
    /**
     * Maps to EncBmpHome.findEmptyCollection
     * 
     * @return
     * @exception javax.ejb.FinderException
     * @see EncBmpHome#findEmptyCollection()   
     */
    public java.util.Collection ejbFindEmptyCollection()
    throws javax.ejb.FinderException, java.rmi.RemoteException {
        return new java.util.Vector();
    }

    /**
     * Maps to EncBmpHome.findByPrimaryKey
     * 
     * @param primaryKey
     * @return 
     * @exception javax.ejb.FinderException
     * @see EncBmpHome#findByPrimaryKey(Integer)
     */
    public Integer ejbFindByPrimaryKey(Integer primaryKey)
    throws javax.ejb.FinderException{
        return new Integer(-1);
    }

    /**
     * Maps to EncBmpHome.create
     * 
     * @param name
     * @return 
     * @exception javax.ejb.CreateException
     * @see EncBmpHome#create
     */
    public Integer ejbCreate(String name)
    throws javax.ejb.CreateException{
        return new Integer(-1);
    }
    
    public void ejbPostCreate(String name)
    throws javax.ejb.CreateException{
    }
    
    
    //    
    // Home interface methods
    //=============================
    

    //=============================
    // Remote interface methods
    //    
    

    public void lookupEntityBean() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            BasicBmpHome home = (BasicBmpHome) javax.rmi.PortableRemoteObject.narrow( ctx.lookup("java:comp/env/entity/bmp/beanReferences/bmp_entity"), BasicBmpHome.class );
            Assert.assertNotNull("The EJBHome looked up is null",home);

            BasicBmpObject object = home.create("Enc Bean");
            Assert.assertNotNull("The EJBObject is null", object );
            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupStatefulBean() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            BasicStatefulHome home = (BasicStatefulHome) javax.rmi.PortableRemoteObject.narrow( ctx.lookup("java:comp/env/entity/bmp/beanReferences/stateful"), BasicStatefulHome.class );
            Assert.assertNotNull("The EJBHome looked up is null",home);

            BasicStatefulObject object = home.create("Enc Bean");
            Assert.assertNotNull("The EJBObject is null", object );
            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupStatelessBean() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            BasicStatelessHome home = (BasicStatelessHome) javax.rmi.PortableRemoteObject.narrow( ctx.lookup("java:comp/env/entity/bmp/beanReferences/stateless"), BasicStatelessHome.class );
            Assert.assertNotNull("The EJBHome looked up is null",home);

            BasicStatelessObject object = home.create();
            Assert.assertNotNull("The EJBObject is null", object );
            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }

    public void lookupStringEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            String expected = new String("1");
            String actual   = (String)ctx.lookup("java:comp/env/entity/bmp/references/String");
            
            Assert.assertNotNull("The String looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupDoubleEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Double expected = new Double(1.0D);
            Double actual   = (Double)ctx.lookup("java:comp/env/entity/bmp/references/Double");
            
            Assert.assertNotNull("The Double looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupLongEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Long expected = new Long(1L);
            Long actual   = (Long)ctx.lookup("java:comp/env/entity/bmp/references/Long");
            
            Assert.assertNotNull("The Long looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupFloatEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Float expected = new Float(1.0F);
            Float actual   = (Float)ctx.lookup("java:comp/env/entity/bmp/references/Float");
            
            Assert.assertNotNull("The Float looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupIntegerEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Integer expected = new Integer(1);
            Integer actual   = (Integer)ctx.lookup("java:comp/env/entity/bmp/references/Integer");
            
            Assert.assertNotNull("The Integer looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupShortEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Short expected = new Short((short)1);
            Short actual   = (Short)ctx.lookup("java:comp/env/entity/bmp/references/Short");
            
            Assert.assertNotNull("The Short looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupBooleanEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Boolean expected = new Boolean(true);
            Boolean actual = (Boolean)ctx.lookup("java:comp/env/entity/bmp/references/Boolean");
            
            Assert.assertNotNull("The Boolean looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    
    public void lookupByteEntry() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            
            Byte expected = new Byte((byte)1);
            Byte actual   = (Byte)ctx.lookup("java:comp/env/entity/bmp/references/Byte");
            
            Assert.assertNotNull("The Byte looked up is null", actual );
            Assert.assertEquals(expected, actual );

            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }

    public void lookupResource() throws TestFailureException{
        try{
            try{
            InitialContext ctx = new InitialContext();
            Assert.assertNotNull("The InitialContext is null", ctx );
            } catch (Exception e){
                Assert.fail("Received Exception "+e.getClass()+ " : "+e.getMessage());
            }
        } catch (AssertionFailedError afe){
            throw new TestFailureException(afe);
        }
    }
    //    
    // Remote interface methods
    //=============================


    //================================
    // EntityBean interface methods
    //    
    
    /**
     * A container invokes this method to instruct the
     * instance to synchronize its state by loading it state from the
     * underlying database.
     */
    public void ejbLoad() throws EJBException,RemoteException {
    }
    
    /**
     * Set the associated entity context. The container invokes this method
     * on an instance after the instance has been created.
     */
    public void setEntityContext(EntityContext ctx) throws EJBException,RemoteException {
        ejbContext = ctx;
    }
    
    /**
     * Unset the associated entity context. The container calls this method
     * before removing the instance.
     */
    public void unsetEntityContext() throws EJBException,RemoteException {
    }
    
    /**
     * A container invokes this method to instruct the
     * instance to synchronize its state by storing it to the underlying
     * database.
     */
    public void ejbStore() throws EJBException,RemoteException {
    }
    
    /**
     * A container invokes this method before it removes the EJB object
     * that is currently associated with the instance. This method
     * is invoked when a client invokes a remove operation on the
     * enterprise Bean's home interface or the EJB object's remote interface.
     * This method transitions the instance from the ready state to the pool
     * of available instances.
     */
    public void ejbRemove() throws RemoveException,EJBException,RemoteException {
    }
    
    /**
     * A container invokes this method when the instance
     * is taken out of the pool of available instances to become associated
     * with a specific EJB object. This method transitions the instance to
     * the ready state.
     */
    public void ejbActivate() throws EJBException,RemoteException {
    }
    
    /**
     * A container invokes this method on an instance before the instance
     * becomes disassociated with a specific EJB object. After this method
     * completes, the container will place the instance into the pool of
     * available instances.
     */
    public void ejbPassivate() throws EJBException,RemoteException {
    }
    //    
    // EntityBean interface methods
    //================================
}
