/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author Thijs
 */
public class XmlRpcClientFactory {

    public static XmlRpcClient getXmlRpcClient() {
        try {
            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL("http://127.0.0.1:8080/xmlrpc"));
            XmlRpcClient client = new XmlRpcClient();
           
            client.setConfig(config);


            return client;
        } catch (MalformedURLException ex) {
            Logger.getLogger(XmlRpcClientFactory.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

    }
}
