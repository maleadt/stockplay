/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
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

            ResourceBundle settings = ResourceBundle.getBundle("com/kapti/administration/settings");

            XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
            config.setServerURL(new URL(settings.getString("server")));
            config.setGzipCompressing(settings.getString("gzip").equals("1"));
            config.setGzipRequesting(settings.getString("gzip").equals("1"));
            config.setEnabledForExtensions(true);


            XmlRpcClient client = new XmlRpcClient();

            client.setConfig(config);



            return client;
        } catch (MalformedURLException ex) {
            logger.error("Error while creating XML-RPC-client", ex);
      
            return null;
        }

    }
}
