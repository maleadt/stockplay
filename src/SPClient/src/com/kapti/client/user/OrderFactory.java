/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Thijs
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
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            
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
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        HashMap h = t.toStruct();

        //verwijder illegale velden
        h.remove(Order.Fields.ID.toString());
        h.remove(Order.Fields.CREATIONTIME.toString());
        h.remove(Order.Fields.EXECUTIONTIME.toString());

        try {
        if (t.getId() > 0) {
            h.remove(Order.Fields.USER.name());
            h.remove(Order.Fields.ISIN.name());
      
            return (Boolean) client.execute("User.Order.Modify", new Object[]{"id == '" + t.getId() + "'", h});
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
