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
import java.util.Iterator;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Thijs
 */
public class PointsTransactionFactory {

    private static PointsTransactionFactory instance = new PointsTransactionFactory();

    public static PointsTransactionFactory getInstance() {
        return instance;
    }

    
    private PointsTransactionFactory() {
    }




    public PointsTransaction createTransaction(User user, Date time) {
        return new PointsTransaction(user, time);
    }

            public Collection<PointsTransaction> getAllTransactions() throws StockPlayException {
        return getTransactionByFilter("");
    }

    public Collection<PointsTransaction> getTransactionByFilter(String filter) throws StockPlayException {

        ArrayList<PointsTransaction> result = new ArrayList<PointsTransaction>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] users = (Object[]) client.execute("User.Points.List", new Object[]{filter});

            for (Object obj : users) {
                result.add(PointsTransaction.fromStruct((HashMap) obj));

            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public PointsTransaction getTransactionByUser(User user) throws StockPlayException{
        Collection<PointsTransaction> users = getTransactionByFilter("userid == '"+ user.getId() +"'");
        Iterator<PointsTransaction> it = users.iterator();

        if(it.hasNext())
            return it.next();
        else
            return null;


    }

    public boolean makePersistent(PointsTransaction t) throws XmlRpcException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = t.toStruct();


        return (Boolean) client.execute("User.Points.CreateTransaction", new Object[]{h});
    }
}

