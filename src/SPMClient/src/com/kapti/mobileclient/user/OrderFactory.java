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
public class OrderFactory {

    private static OrderFactory instance = new OrderFactory();

    public static OrderFactory getInstance() {
        return instance;
    }

    private OrderFactory() {
    }

    public Order createOrder() {
        return  new Order(); // We kennen het nog geen ID toe

    }

    public Vector getAllOrders() throws StockPlayException {
        return getOrdersByFilter("");
    }

    public Vector getAllPendingOrders() throws StockPlayException {
        return getOrdersByFilter("status == '" + Order.OrderStatus.ACCEPTED + "'");
    }

    public Vector getOrdersByFilter(String filter) throws StockPlayException {

        Vector result = new Vector();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector params = new Vector();
            //params.addElement(filter);
            Vector users = (Vector) client.execute("User.Order.List", params);

            for(Enumeration e = users.elements(); e.hasMoreElements();)
                result.addElement(Order.fromStruct((Hashtable)e.nextElement()));

            return result;
        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public Order getOrderById(int id) throws StockPlayException {
        Vector orders = getOrdersByFilter("id == '" + id + "'");
        if(!orders.isEmpty())
            return (Order) orders.firstElement();
        else
            return null;
    }

    public boolean makePersistent(Order t) throws StockPlayException {
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        Hashtable h = t.toStruct();

        //verwijder illegale velden
//        h.remove(Order.IDFIELD);
//        h.remove(Order.CREATIONTIMEFIELD);
//        h.remove(Order.EXECUTIONTIMEFIELD);

        try {
        if (t.getId() > 0) {
            h.remove(Order.ISINFIELD);
            h.remove(Order.USERFIELD);

            Vector params = new Vector();
            params.addElement("id == '" + t.getId() + "'");


            Hashtable h2 = new Hashtable();
            h2.put(Order.STATUSFIELD, t.status);

            params.addElement(h2);
      
            return ((Boolean) client.execute("User.Order.Modify", params)).booleanValue();
        }else {

            Vector params = new Vector();
            params.addElement(h);


            Integer id = (Integer) client.execute("User.Order.Create", params);
            if (id.intValue() > 0) {
                t.setId(id.intValue());
            }

            return id.intValue() > 0;
        }
        } catch(Exception ex) {
            throw new StockPlayException("Error occured while saving order " + t.getId() + ex.getMessage());
        }
    }
}
