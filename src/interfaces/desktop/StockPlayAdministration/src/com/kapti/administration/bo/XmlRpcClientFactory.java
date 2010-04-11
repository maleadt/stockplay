/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo;

import java.net.MalformedURLException;
import java.net.URL;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author Thijs
 */
public class XmlRpcClientFactory {

    static Logger logger = Logger.getLogger(XmlRpcClientFactory.class);

    public static XmlRpcClient getXmlRpcClient() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://localhost:8080/backend/public"));
            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(config);


            return client;
        } catch (MalformedURLException ex) {
            logger.error("Error while creating XML-RPC-client", ex);
      
            return null;
        }

    }
}
