/*
 * UserHandler.java
 * StockPlay - Handler van de User klasse.
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
package com.kapti.backend.api;

import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.ErrorException;
import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.ParserException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief   Handler van de User klasse.
 *
 * Deze klasse is de handler van de User klasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class UserHandler extends MethodClass {
    //
    // Methodes
    //

    public int Hello(String iClient, int iProtocolVersion) throws XmlRpcException {
        getLogger().info("Client connected: " + iClient);
        if (iProtocolVersion != PROTOCOL_VERSION)
            throw Error.VERSION_NOT_SUPPORTED.getException();
        return 1;
    }

    public int Create(Hashtable<String, Object> iDetails) throws XmlRpcException {
        // Get DAO reference
        GenericDAO<com.kapti.data.User, Integer> tUserDAO = getDAO().getUserDAO();

        com.kapti.data.User tUser = new com.kapti.data.User();
        try {
            tUser.fromStruct(iDetails);
        } catch (ErrorException ex) {
            mLogger.error(ex.getMessage());
            throw Error.INTERNAL_FAILURE.getException();
        }
        try {
            tUserDAO.create(tUser);
        } catch (StockPlayException ex) {
            mLogger.error(ex.getMessage());
            throw Error.DATABASE_FAILURE.getException();
        }
        return 1;
    }

    public Vector<Hashtable<String, Object>> Details(String iFilter) throws XmlRpcException, StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.User, Integer> userDAO = getDAO().getUserDAO();

        // Fetch and convert all Indexs
        Collection<com.kapti.data.User> tUsers = userDAO.findAll();
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.User tUser : tUsers) {
            oVector.add(tUser.toStruct (
                    com.kapti.data.User.Fields.ID,
                    com.kapti.data.User.Fields.FIRSTNAME,
                    com.kapti.data.User.Fields.LASTNAME,
                    com.kapti.data.User.Fields.RRN,
                    com.kapti.data.User.Fields.CASH,
                    com.kapti.data.User.Fields.STARTAMOUNT));
        }

        return oVector;
    }

    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.User, Integer> userDAO = getDAO().getUserDAO();

        // Fetch and convert all Indexs
        Collection<com.kapti.data.User> tUsers = userDAO.findAll();
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.User tUser : tUsers) {
            oVector.add(tUser.toStruct (
                    com.kapti.data.User.Fields.ID,
                    com.kapti.data.User.Fields.NICKNAME,
                    com.kapti.data.User.Fields.REGDATE,
                    com.kapti.data.User.Fields.POINTS));
        }

        return oVector;
    }

    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException, FilterException, ParserException {
        // Get DAO reference
        GenericDAO<com.kapti.data.User, Integer> tUserDAO = getDAO().getUserDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Now apply the new properties
        // TODO: controleren of de struct geen ID field bevat, deze kan _enkel_
        //       gebruikt worden om een initiële Exchange aa nte maken (Create)
        for (com.kapti.data.User tUser : tUserDAO.findByFilter(filter)) {
            tUser.fromStruct(iDetails);
            tUserDAO.update(tUser);
        }

        return 1;
    }
    
    public int Remove(String iFilter) throws XmlRpcException, StockPlayException, FilterException, ParserException {
        // Get DAO reference
        GenericDAO<com.kapti.data.User, Integer> tUserDAO = getDAO().getUserDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        for (com.kapti.data.User tUser : tUserDAO.findByFilter(filter))
            tUserDAO.delete(tUser);
        
        return 1;
    }
}