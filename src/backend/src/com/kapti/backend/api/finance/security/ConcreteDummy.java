/*
 * ConcreteDummy.java
 * StockPlay - Dummy implementatie van de Finance.Security subklasse.
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
package com.kapti.backend.api.finance.security;

import com.kapti.backend.api.finance.Security;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief   Dummy implementatie van de Finance.Security interface.
 *
 * Deze klasse is een dummy implementatie van de Finance.Security interface. Een
 * dergelijke implementatie geeft valide data terug, zonder daarvoor de database
 * te raadplegen. Deze implementatie kan zo gebruikt worden om een client-systeem
 * te testen.
 */
public class ConcreteDummy extends Security {
    //
    // Methodes
    //

    @Override
    public Vector<Hashtable<String, Object>> Details(String iFilter) throws XmlRpcException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int Update(Hashtable<String, Object> iDetails) throws XmlRpcException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

