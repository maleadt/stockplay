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
public class TransactionFactory {

    private static TransactionFactory instance = new TransactionFactory();

    public static TransactionFactory getInstance() {
        return instance;
    }
    
    

    private TransactionFactory() {}


    public Transaction createTransaction(){
        return new Transaction(-1);
    }

        public Collection<Transaction> getAllTransactions() throws StockPlayException {
        return getTransactionByFilter("");
    }

    public Collection<Transaction> getTransactionByFilter(String filter) throws StockPlayException {

        ArrayList<Transaction> result = new ArrayList<Transaction>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] users = (Object[]) client.execute("User.Transaction.List", new Object[]{filter});

            for (Object obj : users) {
                result.add(Transaction.fromStruct((HashMap) obj));

            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Transaction getTransactionById(int id) throws StockPlayException{
        Collection<Transaction> users = getTransactionByFilter("id == '"+ id +"'");
        Iterator<Transaction> it = users.iterator();

        if(it.hasNext())
            return it.next();
        else
            return null;


    }

    public boolean makePersistent(Transaction t) throws XmlRpcException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = t.toStruct();

        if (t.getId() > 0) {
            h.remove(Transaction.Fields.ID.toString());
            return (Integer) client.execute("User.Transaction.Modify", new Object[]{"id == '" + t.getId() + "'", h}) > 0;
        } else {
            h.remove(Transaction.Fields.ID.toString());
            Integer id = (Integer) client.execute("User.Transaction.Create", new Object[]{h});
            if (id > 0) {
                t.setId(id);
            }

            return id > 0;
        }
    }
}
