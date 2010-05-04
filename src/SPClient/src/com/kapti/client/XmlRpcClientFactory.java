/*
 * XmlRpcClientFactory.java
 * StockPlay - Fabriek voor het aanmaken van XmlRPC clients.
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

package com.kapti.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * \brief   Fabriek voor het aanmaken van XmlRPC clients.
 *
 */

public class XmlRpcClientFactory {

    static Logger logger = Logger.getLogger(XmlRpcClientFactory.class);

    public static XmlRpcClient getXmlRpcClient() {
        try {

            ResourceBundle settings = ResourceBundle.getBundle("com/kapti/client/settings");
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
