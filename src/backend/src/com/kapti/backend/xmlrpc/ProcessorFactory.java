/*
 * ProcessorFactory.java
 * StockPlay - Factory for request processors
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

import com.kapti.backend.MethodInterface;
import com.kapti.backend.Pobject;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestSpecificProcessorFactoryFactory;

/**
 * Deze klasse, een uitbreiding van RequestSpecificProcessorFactoryFactory,
 * zorgt ervoor dat aangemaakte handlers extra informatie kunnen meegegeven
 * worden, dit vooraleer ze een effectieve XML-RPC request afhandelen.
 *
 * @author tim
 */
public class ProcessorFactory extends RequestSpecificProcessorFactoryFactory {
    // Referentie naar data container

    private Pobject pobject = null;

    public ProcessorFactory(Pobject pobject) {
        // init method, called in BmXmlRpcServlet
        this.pobject = pobject;
    }

    /**
     * Deze methode wordt door de servlet server gebruikt om een lokale handler
     * te vinden om een RPC af te handelen. We overschrijven deze methode louter
     * om de geinstantieerde handler een referentie naar de container met
     * persistente data mee te geven.
     * 
     * @param iClass
     * @param iRequest
     * @return
     * @throws XmlRpcException
     */
    protected Object getRequestProcessor(Class iClass, XmlRpcRequest iRequest) throws XmlRpcException {
        MethodInterface proc = (MethodInterface) super.getRequestProcessor(iClass, iRequest);
        proc.init(pobject);
        return proc;
    }
}
