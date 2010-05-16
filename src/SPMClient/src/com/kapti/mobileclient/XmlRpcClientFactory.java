/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient;

import org.kxmlrpc.XmlRpcClient;


/**
 *
 * @author Thijs
 */
public class XmlRpcClientFactory {

    private static final String SERVER_URL = "http://be04.kapti.com:6800/backend/public";
    //private static final String SERVER_URL = "http://be04.kapti.com:6800/backend/public";

    private static String sessionID = null;

    public static XmlRpcClient getXmlRpcClient() {
        XmlRpcClient client = new XmlRpcClient(SERVER_URL);

        if(sessionID != null)
            client.setCredentials(sessionID, "");

        return client;
    }

    public static void setSessionID(String sessionID){
        XmlRpcClientFactory.sessionID = sessionID;
    }

    public static void resetSession(){
        XmlRpcClientFactory.sessionID = null;
    }
}
