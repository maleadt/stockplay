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
public class TransactionFactory {

    private static TransactionFactory instance = new TransactionFactory();

    public static TransactionFactory getInstance() {
        return instance;
    }

    private TransactionFactory() {
    }

    public Vector getAllTransactions() throws StockPlayException {
        return getTransactionByFilter("");
    }

    public Vector getTransactionByFilter(String filter) throws StockPlayException {

        Vector result = new Vector();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector params = new Vector();
            params.addElement(filter);
            Vector users = (Vector)  client.execute("User.Transaction.List", params);

            for (Enumeration e = users.elements();  e.hasMoreElements();) {
                result.addElement(Transaction.fromStruct((Hashtable) e.nextElement()));

            }
            return result;

        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public Transaction getTransactionById(int id) throws StockPlayException {
        Vector users = getTransactionByFilter("id == '" + id + "'");
        if(!users.isEmpty())
            return (Transaction)users.firstElement();
        else
            return null;
    }
}
