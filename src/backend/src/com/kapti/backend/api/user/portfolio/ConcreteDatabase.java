/*
 * ConcreteDatabase.java
 * StockPlay - Concrete implementatie van de Portfolio subklasse.
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

package com.kapti.backend.api.user.portfolio;

import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.exception.FilterException;
import com.kapti.filter.exception.ParserException;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

public class ConcreteDatabase extends com.kapti.backend.api.user.Portfolio {

    @Override
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, FilterException, StockPlayException, ParserException {
        // Get DAO reference
        GenericDAO<com.kapti.data.UserSecurity, UserSecurityPK> tPortfolioDAO = getDAO().getUserSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);
        
        // Fetch and convert all Indexs
        Collection<UserSecurity> tSecurities = tPortfolioDAO.findByFilter(filter);
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.UserSecurity tSecurity : tSecurities) {
            oVector.add(tSecurity.toStruct(
                    com.kapti.data.UserSecurity.Fields.AMOUNT,
                    com.kapti.data.UserSecurity.Fields.SYMBOL,
                    com.kapti.data.UserSecurity.Fields.USER));
        }

        return oVector;
    }


    // TODO 
//    @Override
//    public Vector<Hashtable<String, Object>> History(Filter iFilter) throws XmlRpcException, FilterException, StockPlayException {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

}
