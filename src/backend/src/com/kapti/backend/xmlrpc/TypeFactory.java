/*
 * TypeFactory.java
 * StockPlay - Type factory voor custom types
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

import org.apache.ws.commons.util.NamespaceContextImpl;
import org.apache.xmlrpc.common.TypeFactoryImpl;
import org.apache.xmlrpc.common.XmlRpcController;
import org.apache.xmlrpc.common.XmlRpcStreamConfig;
import org.apache.xmlrpc.parser.TypeParser;
import org.apache.xmlrpc.serializer.TypeSerializer;
import org.xml.sax.SAXException;

/**
 *
 * @author tim
 */
public class TypeFactory extends TypeFactoryImpl {

    public TypeFactory(XmlRpcController pController) {
        super(pController);
    }

    @Override
    public TypeParser getParser(XmlRpcStreamConfig iConfig, NamespaceContextImpl iContext, String iURI, String iLocalName) {
        //if (DateSerializer.DATE_TAG.equals(iLocalName)) {
        //    return new DateParser(pFormat);
        //} else {
            return super.getParser(iConfig, iContext, iURI, iLocalName);
        //}
    }

    @Override
    public TypeSerializer getSerializer(XmlRpcStreamConfig pConfig, Object pObject) throws SAXException {
        //if (pObject instanceof Date) {
        //    return new DateSerializer(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"));
        //} else {
            return super.getSerializer(pConfig, pObject);
        //}
    }
}
