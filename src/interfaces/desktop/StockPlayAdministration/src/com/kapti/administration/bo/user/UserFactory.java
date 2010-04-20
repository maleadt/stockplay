/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.user;

import com.kapti.administration.bo.XmlRpcClientFactory;
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
 * @author Thijs
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
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] users = (Object[]) client.execute("User.List", new Object[]{filter});

            for (Object obj : users) {
                result.add(User.fromStruct((HashMap) obj));

            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public User getUserById(int id) throws StockPlayException {
        Collection<User> users = getUsersByFilter("id == '" + id + "'");
        Iterator<User> it = users.iterator();

        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }


    }

    public boolean verifyLogin(String nickname, String password) throws StockPlayException {
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object result = client.execute("User.Validate", new Object[]{nickname, password});

            if (result instanceof Boolean) {
                return (Boolean) result;
            } else {
                throw new StockPlayException("Expected Boolean, but got an " + result.getClass().toString());
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
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = user.toStruct();

        try {
            if (user.getId() > 0) {
                h.remove(User.Fields.ID.toString());
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
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

        try {
            return (Integer) client.execute("User.Remove", new Object[]{"id EQUALS '" + user.getId() + "'"}) > 0;
        } catch (XmlRpcException ex) {
            throw new StockPlayException("Error while deleting user", ex);
        }
    }
}

