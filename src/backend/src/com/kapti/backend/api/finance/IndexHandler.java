/*
 * IndexHandler.java
 * StockPlay - Handler van de Finance.Index subklasse.
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
package com.kapti.backend.api.finance;

import com.kapti.backend.api.MethodClass;
import com.kapti.data.Index;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

/**
 * \brief   Handler van de Finance.Index subklasse.
 *
 * Deze klasse is de handler van de Finance.Index subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class IndexHandler extends MethodClass {
    //
    // Methodes
    //

    public Vector<Hashtable<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Index, Integer> tIndexDAO = getDAO().getIndexDAO();

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Index> tIndexs = tIndexDAO.findAll();
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.Index tIndex : tIndexs) {
            oVector.add(tIndex.toStruct(
                    com.kapti.data.Index.Fields.ID,
                    com.kapti.data.Index.Fields.NAME,
                    com.kapti.data.Index.Fields.EXCHANGE));
        }

        return oVector;
    }

    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Index, Integer> tIndexDAO = getDAO().getIndexDAO();

        // Get the Indexs we need to modify
        Collection<com.kapti.data.Index> tIndexs = tIndexDAO.findAll();

        // Now apply the new properties
        // TODO: controleren of de struct geen ID field bevat, deze kan _enkel_
        //       gebruikt worden om een initiële Exchange aa nte maken (Create)
        for (com.kapti.data.Index tIndex : tIndexs) {
            tIndex.applyStruct(iDetails);
            tIndexDAO.update(tIndex);
        }

        return 1;
    }

    public int Create(Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Index, Integer> tIndexDAO = getDAO().getIndexDAO();

        // Instantiate a new index
        Index tIndex = Index.fromStruct(iDetails);

        tIndex.applyStruct(iDetails);
        tIndexDAO.create(tIndex);

        return 1;
    }
}

