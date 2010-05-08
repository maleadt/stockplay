/*
 * OrderFactory.java
 * StockPlay - Fabriek die ons de orders aanleverd.
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
 * \brief   Fabriek die ons de orders aanleverd.
 *
 */

public class OrderFactory {

    private static OrderFactory instance = new OrderFactory();

    public static OrderFactory getInstance() {
        return instance;
    }

    private OrderFactory() {
    }

    public Order createOrder() {
        return new Order(-1); // We kennen het nog geen ID toe
    }

    public Collection<Order> getAllOrders() throws StockPlayException {
        return getOrdersByFilter("");
    }

    public Collection<Order> getAllPendingOrders() throws StockPlayException {
        return getOrdersByFilter("status == '" + Order.OrderStatus.ACCEPTED.name() + "'");
    }

    public Collection<Order> getOrdersByFilter(String filter) throws StockPlayException {

        ArrayList<Order> result = new ArrayList<Order>();
        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
            Object[] users = (Object[]) client.execute("User.Order.List", new Object[]{filter});

            for (Object obj : users)
                result.add(Order.fromStruct((HashMap) obj));

            return result;
        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Order getOrderById(int id) throws StockPlayException {
        Collection<Order> users = getOrdersByFilter("id == '" + id + "'");
        Iterator<Order> it = users.iterator();

        if (it.hasNext()) {
            return it.next();
        } else {
            return null;
        }
    }

    public boolean makePersistent(Order t) throws StockPlayException {
        XmlRpcClient client = SPClientFactory.getPrivateClient();
        HashMap h = t.toStruct();

        //verwijder illegale velden
        h.remove(Order.Fields.ID.toString());
        h.remove(Order.Fields.CREATIONTIME.toString());
        h.remove(Order.Fields.EXECUTIONTIME.toString());

        try {
        if (t.getId() > 0) {
            h.remove(Order.Fields.USER.name());
            h.remove(Order.Fields.ISIN.name());
     
            return (Boolean) client.execute("User.Order.Modify", new Object[]{"id == " + t.getId(), h});
        }else {
            Integer id = (Integer) client.execute("User.Order.Create", new Object[]{h});
            if (id > 0) {
                t.setId(id);
            }

            return id > 0;
        }
        } catch(XmlRpcException ex) {
            throw new StockPlayException("Error occured while saving order " + t.getId() , ex);
        }
    }
}