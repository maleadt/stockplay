/*
 * ConcreteDummy.java
 * StockPlay - Dummy implementatie van de Finance.Exchange subklasse.
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
package com.kapti.backend.api.finance.exchange;

import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief   Dummy implementatie van de Finance.Exchange interface.
 *
 * Deze klasse is een dummy implementatie van de Finance.Exchange interface. Een
 * dergelijke implementatie geeft valide data terug, zonder daarvoor de database
 * te raadplegen. Deze implementatie kan zo gebruikt worden om een client-systeem
 * te testen.
 */
public class ConcreteDatabase extends com.kapti.backend.api.finance.Exchange {
    //
    // Methodes
    //

    @Override
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Exchange, String> exDAO = getDAO().getExchangeDAO();

        // Fetch and convert all exchanges
        Collection<com.kapti.data.Exchange> tExchanges = exDAO.findAll();
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.Exchange tExchange : tExchanges) {
            oVector.add(tExchange.toStruct(
                    com.kapti.data.Exchange.Fields.ID,
                    com.kapti.data.Exchange.Fields.NAME,
                    com.kapti.data.Exchange.Fields.LOCATION));
        }

        return oVector;
    }

    @Override
    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Exchange, String> exDAO = getDAO().getExchangeDAO();

        // Get the exchanges we need to modify
        Collection<com.kapti.data.Exchange> tExchanges = exDAO.findAll();

        // Now apply the new properties
        for (com.kapti.data.Exchange tExchange : tExchanges) {
            tExchange.fromStruct(iDetails);
            exDAO.update(tExchange);
        }

        return 1;
    }

}

