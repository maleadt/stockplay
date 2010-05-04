/*
 * TransactionFactory.java
 * StockPlay - Transaction fabriek
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
import java.util.HashMap;
import java.util.Iterator;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * \brief   Transaction fabriek
 *
 */

public class TransactionFactory {

    private static TransactionFactory instance = new TransactionFactory();

    public static TransactionFactory getInstance() {
        return instance;
    }

    private TransactionFactory() {
    }

    public Transaction createTransaction() {
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

    public Transaction getTransactionById(int id) throws StockPlayException {
        Collection<Transaction> users = getTransactionByFilter("id == '" + id + "'");
        Iterator<Transaction> it = users.iterator();

        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }


    }

    public boolean execute(Transaction t) throws StockPlayException {

        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = t.toStruct();

        //transactions kunnen niet worden gewijzigd.. Bij problemen moet een tegentransactie worden aangemaakt
//        if (t.getId() > 0) {
//            h.remove(Transaction.Fields.ID.toString());
//            return (Integer) client.execute("User.Transaction.Modify", new Object[]{"id == '" + t.getId() + "'", h}) > 0;
//        } else {

        h.remove(Transaction.Fields.ID.toString());
        try {
        Integer id = (Integer) client.execute("User.Transaction.Create", new Object[]{h});
        if (id > 0) {
            t.setId(id);
        }

        return id > 0; }
        catch(XmlRpcException ex ){
            throw new StockPlayException("An error occured while executing the transaction", ex);
        }
//        }
    }
}
