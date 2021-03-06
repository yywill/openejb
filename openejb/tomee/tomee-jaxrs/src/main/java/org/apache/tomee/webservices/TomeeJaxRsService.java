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
package org.apache.tomee.webservices;

import org.apache.openejb.assembler.classic.AppInfo;
import org.apache.openejb.assembler.classic.WebAppInfo;
import org.apache.openejb.loader.SystemInstance;
import org.apache.openejb.server.rest.RESTService;
import org.apache.openejb.server.rest.RsRegistry;
import org.apache.openejb.spi.Service;
import org.apache.tomee.catalina.WebDeploymentListener;
import org.apache.tomee.catalina.WebDeploymentListeners;

import java.util.Properties;

/**
 * @version $Rev$ $Date$
 */
public class TomeeJaxRsService implements Service, WebDeploymentListener {

    @Override
    public void init(Properties props) throws Exception {
        final SystemInstance system = SystemInstance.get();

        TomcatRsRegistry tomcatRestHandler = (TomcatRsRegistry) system.getComponent(RsRegistry.class);
        if (tomcatRestHandler == null) {
            tomcatRestHandler = new TomcatRsRegistry();
            system.setComponent(RsRegistry.class, tomcatRestHandler);
        }

        system.getComponent(WebDeploymentListeners.class).add(this);
    }

    @Override
    public void afterApplicationCreated(AppInfo appInfo, WebAppInfo webApp) {
        // required for Pojo Web Services because when Assembler creates the application
        // the CoreContainerSystem does not contain the WebContext
        // see also the start method getContainerSystem().addWebDeployment(webContext);
        RESTService component = SystemInstance.get().getComponent(RESTService.class);
        if (component == null) return;
        component.afterApplicationCreated(appInfo, webApp);
    }

}
