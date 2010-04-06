/*
 * ProcessorFactory.java
 * StockPlay - Factory voor request processors.
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

import com.kapti.backend.api.MethodClass;
import com.kapti.data.persistence.StockPlayDAO;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory.RequestSpecificProcessorFactoryFactory;

/**
 * \brief Factory voor request processors.
 *
 * Deze klasse, een uitbreiding van RequestSpecificProcessorFactoryFactory,
 * zorgt ervoor dat aangemaakte handlers extra informatie kunnen meegegeven
 * worden, dit vooraleer ze een effectieve XML-RPC request afhandelen.
 */
public class ProcessorFactory extends RequestSpecificProcessorFactoryFactory {
    //
    // Dataleden
    //

    private StockPlayDAO mDAO = null;

    private Logger mLogger;


    //
    // Constructie
    //

    public ProcessorFactory(StockPlayDAO iDAO) {
        mLogger = Logger.getLogger(this.getClass());
        mDAO = iDAO;
    }


    //
    // Methoden
    //

    /**
     * Deze methode wordt door de servlet server gebruikt om een lokale handler
     * te vinden om een RPC af te handelen. We overschrijven deze methode louter
     * om de geinstantieerde handler een referentie naar de container met
     * persistente data mee te geven.
     */
    @Override
    protected Object getRequestProcessor(Class iClass, XmlRpcRequest iRequest) throws XmlRpcException {
        mLogger.info("getting processor for method " + iRequest.getMethodName());
        MethodClass proc = (MethodClass) super.getRequestProcessor(iClass, iRequest);
        proc.init(mDAO);
        return proc;
    }
}
