/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.user;

import com.kapti.mobileclient.XmlRpcClientFactory;
import com.kapti.mobileclient.exceptions.RequestError;
import com.kapti.mobileclient.exceptions.StockPlayException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;
import org.kxmlrpc.XmlRpcClient;

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

    public Vector getAllUsers() throws StockPlayException {

        return getUsersByFilter("");
    }

    public Vector getUsersByFilter(String filter) throws StockPlayException {

        Vector result = new Vector();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector params = new Vector();
            params.addElement(filter);
            Vector users = (Vector) client.execute("User.List", params);

            for (Enumeration e = users.elements(); e.hasMoreElements();) {
                Object obj = e.nextElement();

                result.addElement(User.fromStruct((Hashtable) obj));
            }

            return result;

        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public User getUserById(int id) throws StockPlayException {
        Vector users = getUsersByFilter("id == '" + id + "'");
        if (!users.isEmpty()) {
            return (User) users.firstElement();
        } else {
            return null;
        }
    }

    public boolean verifyLogin(String nickname, String password) throws StockPlayException {

        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        Vector params = new Vector();
        params.addElement(nickname);
        params.addElement(password);
        try {
            String session = ((String) client.execute("User.Validate", params));

            if (session.length() > 0) {
                XmlRpcClientFactory.setSessionID(session);
                return true;

            }
            return false;



        } catch (Exception ex) {
            return false;
        }

    }

    public Vector getPortfolioByUser(User user) throws StockPlayException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        Vector result = new Vector();
        try {
            Vector params = new Vector();
            params.addElement("userid == '" + user.getId() + "'");

            Vector portfolio = (Vector) client.execute("User.Portfolio.List", params);
            for (Enumeration e = portfolio.elements(); e.hasMoreElements();) {
                Object obj = e.nextElement();

                result.addElement(UserSecurity.fromStruct((Hashtable) obj));
            }

            return result;

        } catch (Exception ex) {
            throw new StockPlayException(ex.getMessage());
        }

    }
}

