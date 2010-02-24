/*
 * Servlet.java
 * StockPlay - Main servlet
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

import com.kapti.backend.Pobject;
import java.io.IOException;
import java.net.URL;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.server.AbstractReflectiveHandlerMapping.AuthenticationHandler;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcHandlerMapping;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

/**
 * Dit is de servlet die alle aanvragen in de server al afhandelen. De servlet
 * wordt eenmaal geinitialiseerd, waarbij ook de container voor persistente data
 * aangemaakt wordt (Pobject).
 *
 * De servlet handelt, het zij indirect, ook authenticatieaavragen aan. Deze
 * aanvragen worden hier naartoe gestuurt door onze AuthHandler, die gemapt is
 * als default AuthHandler. Deze redirectie vindt plaats omdat enkel te servlet
 * toegang heeft tot de persistente data.
 *
 * @author tim
 */
public class Servlet extends XmlRpcServlet {

    /**
     * Container voor persistente data. Deze wordt doorgegeven aan de init
     * functie van een handler.
     */
    private Pobject pobject = new Pobject();

    public static boolean isAuthenticated(String iUsername, String iPassword) {
        return true;
    }

    /**
     * Deze methode wordt gebruikt bij het configureren van de servlet server,
     * en zorgt voor de connectie tussen aangevraagde methodes en hun lokale
     * tegenhanger. Deze informatie is opgeslaan in een propery bestand,
     * dewelke door deze methode wordt ingelezen en teruggegeven als Mapping
     * object.
     *
     * @return
     * @throws XmlRpcException
     */
    protected XmlRpcHandlerMapping newXmlRpcHandlerMapping() throws XmlRpcException {
        // load the properties file and initialise the PropertyHandlerMapping
        PropertyHandlerMapping oMapping = null;
        URL tUrl = Servlet.class.getResource("XmlRpcServlet.properties");
        if (tUrl == null) {
            throw new XmlRpcException("Failed to locate resource XmlRpcServlet.properties");
        }
        try {
            oMapping = newPropertyHandlerMapping(tUrl);
        } catch (IOException e) {
            throw new XmlRpcException("Failed to load resource " + tUrl + ": " + e.getMessage(), e);
        }

        // add simple auth to our handler..
        AuthenticationHandler handler = new AuthHandler();
        oMapping.setAuthenticationHandler(handler);
        return oMapping;
    }

    protected PropertyHandlerMapping newPropertyHandlerMapping(URL iUrl) throws IOException, XmlRpcException {
        // call the newPropertyHandlerMapping method from our parent class
        PropertyHandlerMapping oMapping = super.newPropertyHandlerMapping(iUrl);

        // create our Request Processor Factory Factory and pass our pobject instance.
        ProcessorFactory factory = new ProcessorFactory(pobject);
        oMapping.setRequestProcessorFactoryFactory(factory);
        oMapping.load(Thread.currentThread().getContextClassLoader(), iUrl);
        return oMapping;
    }
}
