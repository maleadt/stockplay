/*
 * PointsTransactionFactory.java
 * StockPlay - Fabriek die de puntentransaction objecten aanmaakt.
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

import com.kapti.client.XmlRpcClientFactory;
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
 * \brief   Fabriek die de puntentransaction objecten aanmaakt.
 *
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

    public boolean makePersistent(PointsTransaction t) throws StockPlayException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = t.toStruct();

        try {
        return (Integer) client.execute("User.Points.CreateTransaction", new Object[]{h}) > 0;
                } catch (XmlRpcException ex) {
            throw new StockPlayException("Error while saving user", ex);
        }
    }
}

