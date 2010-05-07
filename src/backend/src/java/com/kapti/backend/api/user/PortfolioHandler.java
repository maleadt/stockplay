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

package com.kapti.backend.api.user;

import com.kapti.backend.api.MethodClass;
import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

/**
 * \brief   Handler van de User.Portfolio subklasse.
 *
 * Deze klasse is de handler van de User.Portfolio subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class PortfolioHandler extends MethodClass {
    //
    // Methodes
    //

    public Vector<HashMap<String, Object>> List() throws StockPlayException {
        return List("id == '" + getUser().getId() + "'i");
    }

    public Vector<HashMap<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.UserSecurity, UserSecurityPK> tUserSecurityDAO = getDAO().getUserSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);
        
        // Fetch and convert all Indexs
        Collection<UserSecurity> tUserSecurities = tUserSecurityDAO.findByFilter(filter);
        Vector<HashMap<String, Object>> oVector = new Vector<HashMap<String, Object>>();
        for (com.kapti.data.UserSecurity tUserSecurity : tUserSecurities) {
            oVector.add(tUserSecurity.toStruct(
                    com.kapti.data.UserSecurity.Fields.AMOUNT,
                    com.kapti.data.UserSecurity.Fields.ISIN,
                    com.kapti.data.UserSecurity.Fields.USER));
        }

        return oVector;
    }

    public int Create(HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.UserSecurity, UserSecurityPK> tUserSecurityDAO = getDAO().getUserSecurityDAO();

        // Instantiate a new exchange
        UserSecurity tUserSecurity = UserSecurity.fromStruct(iDetails);

        tUserSecurity.applyStruct(iDetails);
        tUserSecurityDAO.create(tUserSecurity);

        return 1;
    }
}