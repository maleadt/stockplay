/*
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
import com.kapti.data.IndexSecurity;
import com.kapti.data.IndexSecurity.IndexSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
/**
 * \brief   Handler van de User.Portfolio subklasse.
 *
 * Deze klasse is de handler van de User.Portfolio subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class IndexSecurityHandler extends MethodClass {
    //
    // Methodes
    //

    public Vector<Hashtable<String, Object>> List() throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.IndexSecurity, IndexSecurityPK> tIndexSecurityDAO = getDAO().getIndexSecurityDAO();

        // Fetch and convert all IndexSecurities
        Collection<IndexSecurity> tIndexSecurities = tIndexSecurityDAO.findAll();
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.IndexSecurity tIndexSecurity : tIndexSecurities) {
            oVector.add(tIndexSecurity.toStruct(
                    com.kapti.data.IndexSecurity.Fields.INDEX_ISIN,
                    com.kapti.data.IndexSecurity.Fields.SECURITY_ISIN));
        }

        return oVector;
    }

    public Vector<Hashtable<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.IndexSecurity, IndexSecurityPK> tIndexSecurityDAO = getDAO().getIndexSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all IndexSecurities
        Collection<IndexSecurity> tIndexSecurities = tIndexSecurityDAO.findByFilter(filter);
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.IndexSecurity tIndexSecurity : tIndexSecurities) {
            oVector.add(tIndexSecurity.toStruct(
                    com.kapti.data.IndexSecurity.Fields.INDEX_ISIN,
                    com.kapti.data.IndexSecurity.Fields.SECURITY_ISIN));
        }

        return oVector;
    }

    public int Create(Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.IndexSecurity, IndexSecurityPK> tIndexSecurityDAO = getDAO().getIndexSecurityDAO();

        // Instantiate a new exchange
        IndexSecurity tIndexSecurity = IndexSecurity.fromStruct(iDetails);

        tIndexSecurity.applyStruct(iDetails);
        tIndexSecurityDAO.create(tIndexSecurity);

        return 1;
    }
}