/*
 * UserFacory.java
 * StockPlay - Gebruikersfabriek
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
package com.kapti.client.user;

import com.kapti.client.SPClientFactory;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * \brief   Gebruikersfabriek
 *
 */
public class UserFactory {

    private static UserFactory instance = new UserFactory();

    public static UserFactory getInstance() {
        return instance;
    }

    private UserFactory() {
    }

    public Collection<User> getAllUsers() throws StockPlayException {
        return getUsersByFilter("");
    }

    public Collection<User> getUsersByFilter(String filter) throws StockPlayException {

        ArrayList<User> result = new ArrayList<User>();
        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
            Object[] users = (Object[]) client.execute("User.Details", new Object[]{filter});

            for (Object obj : users) {
                result.add(User.fromStruct((HashMap) obj));
            }
            return result;

        } catch (XmlRpcException ex) {
            System.out.println("Filter: " + filter);
            throw new RequestError(ex);
        }
    }

    public User getUserById(int id) throws StockPlayException {
        Collection<User> users = getUsersByFilter("id == " + id);
        Iterator<User> it = users.iterator();

        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }


    }

    public boolean verifyLogin(String nickname, String password) throws StockPlayException {
        try {
            XmlRpcClient client = SPClientFactory.getPublicClient();
            Object result = client.execute("User.Validate", new Object[]{nickname, password});

            if (result instanceof String) {
                SPClientFactory.setSessionID((String) result);
                return true;
            } else {
                return false;
                //throw new StockPlayException("Expected String, but got an " + result.getClass().toString());
            }

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }

    }

    public boolean setLoggedInUser(User user) throws StockPlayException {
        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
            Object result = client.execute("User.CreateSessionForUser", new Object[]{user.getId()});

            if (result instanceof String) {
                SPClientFactory.setSessionID((String) result);
                return true;
            } else {
                return false;
                //throw new StockPlayException("Expected String, but got an " + result.getClass().toString());
            }

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }

    }

    public User createUser() {
        return new User(-1);
    }

    /**
     * Deze functie slaat het opgegeven User-object op via de backend. Indien de gebruiker nog niet bestaat wordt hij aangemaakt, anders wordt hij geupdated..
     * Opmerking: wijzigingen in punten of cash worden NIET meegenomen, deze moeten worden aangepast via een transactie!
     * @param user
     * @return
     * @throws XmlRpcException
     */
    public boolean makePersistent(User user) throws StockPlayException {
        XmlRpcClient client = SPClientFactory.getPrivateClient();
        HashMap h = user.toStruct();
        h.remove(User.Fields.ID.toString());

        try {
            if (user.getId() > 0) {
                return (Integer) client.execute("User.Modify", new Object[]{"id EQUALS '" + user.getId() + "'", h}) > 0;
            } else {
                Integer id = (Integer) client.execute("User.Create", new Object[]{h});
                if (id > 0) {
                    user.setId(id);
                }

                return id > 0;
            }
        } catch (XmlRpcException ex) {
            throw new StockPlayException("Error while saving user", ex);
        }
    }

    public boolean removeUser(User user) throws StockPlayException {
        XmlRpcClient client = SPClientFactory.getPrivateClient();

        try {
            return (Integer) client.execute("User.Remove", new Object[]{"id EQUALS '" + user.getId() + "'"}) > 0;
        } catch (XmlRpcException ex) {
            throw new StockPlayException("Error while deleting user", ex);
        }
    }
}

