/*
 * AuthHandler.java
 * StockPlay - Authentication handler.
 *
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.kapti.backend.xmlrpc;

import com.kapti.data.persistence.StockPlayDAO;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.common.XmlRpcHttpRequestConfig;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping.AuthenticationHandler;

/**
 *
 * @author tim
 */
public class AuthHandler implements AuthenticationHandler {
    //
    // Dataleden
    //
    
    private StockPlayDAO mDAO;
    private SessionsHandler mSessions;
    
    
    //
    // Constructie
    //

    public AuthHandler(StockPlayDAO iDAO, SessionsHandler iSessions) {
        super();
        mDAO = iDAO;
        mSessions = iSessions;
    }


    //
    // Methoden
    //

    public boolean isAuthorized(XmlRpcRequest pRequest) {
        // Haal credentials op


        XmlRpcHttpRequestConfig config = (XmlRpcHttpRequestConfig) pRequest.getConfig();

        String sessionid = config.getBasicUserName();
        String tMethod = pRequest.getMethodName();


        return mSessions.verifyRequest(sessionid, tMethod);
    }
}
