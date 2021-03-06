/*
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

package org.apache.tomee.webapp.listener;

import org.apache.tomee.webapp.helper.service.ServiceContext;
import org.apache.tomee.webapp.helper.service.ServiceContextImpl;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;

public class UserSessionListener implements HttpSessionListener {
    private static final String USER_CONTEXT = "UserSessionListener_USER_CONTEXT";

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        final ServiceContext cxt = new ServiceContextImpl();
        httpSessionEvent.getSession().setAttribute(USER_CONTEXT, cxt);
        httpSessionEvent.getSession().setAttribute("objects", new HashMap<String, Object>());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        //do nothing
    }

    public static ServiceContext getServiceContext(HttpSession session) {
        final ServiceContext cxt = (ServiceContext) session.getAttribute(USER_CONTEXT);
        return cxt;
    }
}
