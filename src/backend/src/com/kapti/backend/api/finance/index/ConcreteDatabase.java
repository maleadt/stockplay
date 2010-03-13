/*
 * ConcreteDummy.java
 * StockPlay - Dummy implementatie van de Finance.Index subklasse.
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
package com.kapti.backend.api.finance.index;

import com.kapti.backend.api.finance.Index;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief   Dummy implementatie van de Finance.Index interface.
 *
 * Deze klasse is een dummy implementatie van de Finance.Index interface. Een
 * dergelijke implementatie geeft valide data terug, zonder daarvoor de database
 * te raadplegen. Deze implementatie kan zo gebruikt worden om een client-systeem
 * te testen.
 */
public class ConcreteDatabase extends Index {
    //
    // Methodes
    //

    @Override
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, StockPlayException {
        GenericDAO<com.kapti.data.Index, Integer> indexDAO = getDAO().getIndexDAO();


        Vector<Hashtable<String, Object>> result = new Vector<Hashtable<String, Object>>();

        Collection<com.kapti.data.Index> indexes = indexDAO.findAll();

        for (com.kapti.data.Index index : indexes) {
            Hashtable<String, Object> ex = new Hashtable<String, Object>();
            ex.put("id", index.getId());
            ex.put("description", index.getName());
            ex.put("exchange", index.getExchange());

            result.add(ex);
        }


        return result;
    }

    @Override
    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException {
          com.kapti.data.Index index = new com.kapti.data.Index((Integer)iDetails.get("id"));

        index.setExchange((String)iDetails.get("exchange"));
        index.setName((String)iDetails.get("name"));


        GenericDAO<com.kapti.data.Index, Integer> indexDAO = getDAO().getIndexDAO();


        if(indexDAO.findById(index.getId()) != null)
            return indexDAO.update(index) ? 1: 0;
        else
            return indexDAO.create(index) ? 1: 0;

    }
}

