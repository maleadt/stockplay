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
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Thijs
 */
public class UserFactory {

    public Collection<User> getAllUsers() throws StockPlayException {
        return getUsersByFilter("");
    }

    public Collection<User> getUsersByFilter(String filter) throws StockPlayException {

        ArrayList<User> result = new ArrayList<User>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] users = (Object[]) client.execute("User.List", new Object[]{filter});

            for (Object obj : users) {
                if (obj instanceof HashMap) {

                    HashMap user = (HashMap) obj;

                    User res = new User(
                            (Integer) user.get(User.Fields.ID.toString()),
                            (String) user.get(User.Fields.NICKNAME.toString()),
                            (String) user.get(User.Fields.EMAIL.toString()),
                            (String) user.get(User.Fields.LASTNAME.toString()),
                            (String) user.get(User.Fields.FIRSTNAME.toString()),
                            (Date) user.get(User.Fields.REGDATE.toString()),
                            (User.Role) User.Role.fromId((Integer) user.get((User.Fields.ROLE.toString()))),
                            (Integer) user.get(User.Fields.POINTS.toString()),
                            (Double) user.get(User.Fields.STARTAMOUNT.toString()),
                            (Double) user.get(User.Fields.CASH.toString()),
                            Long.parseLong((String) user.get(User.Fields.RRN.toString())));

                    result.add(res);
                }
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
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
    public boolean makePersistent(User user) throws XmlRpcException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

        Hashtable<String, Object> h = new Hashtable<String, Object>();
        h.put(User.Fields.ID.toString(), user.getId());
        h.put(User.Fields.NICKNAME.toString(), user.getNickname());
        h.put(User.Fields.LASTNAME.toString(), user.getLastname());
        h.put(User.Fields.FIRSTNAME.toString(), user.getFirstname());
        h.put(User.Fields.EMAIL.toString(), user.getEmail());
        h.put(User.Fields.ROLE.toString(), user.getRole().getId());
        if (user.getRijksregisternummer() != null) {
            h.put(User.Fields.RRN.toString(), user.getRijksregisternummer().toString());
        }
        if (user.getPassword() != null) {
            h.put(User.Fields.PASSWORD.toString(), user.getPassword());
        }


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
    }

    public boolean removeUser(User user) throws XmlRpcException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

        return (Integer) client.execute("User.Remove", new Object[]{"id EQUALS '" + user.getId() + "'"}) > 0;
    }
}
