/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.finance;

import com.kapti.administration.bo.XmlRpcClientFactory;
import com.kapti.exceptions.ErrorException;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * @author Thijs
 */
public class FinanceFactory {

    private static FinanceFactory instance = new FinanceFactory();

    public static FinanceFactory getInstance() {
        return instance;
    }

    private FinanceFactory() {
    }

    public Collection<Security> getAllSecurities() throws StockPlayException {

        ArrayList<Security> result = new ArrayList<Security>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] securities = (Object[]) client.execute("Finance.Security.List", new Object[]{new String()});

            for (Object sec : securities) {
                result.add(Security.fromStruct((HashMap) sec));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }
    // <editor-fold>
    private HashMap<String, Exchange> exchanges = null;

    private void cacheExchanges() throws StockPlayException {
        exchanges = new HashMap<String, Exchange>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] obj = (Object[]) client.execute("Finance.Exchange.List", new Object[]{});

            for (Object exch : obj) {
                Exchange exchange = Exchange.fromStruct((HashMap) exch);
                exchanges.put(exchange.getSymbol(), exchange);
            }
        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Collection<Exchange> getAllExchanges() throws StockPlayException {
        if (exchanges == null) {
            cacheExchanges();
        }
        return exchanges.values();
    }

    public Exchange getExchange(String symbol) throws StockPlayException {
        if (exchanges == null) {
            cacheExchanges();
        }
        return exchanges.get(symbol);
    }

    public boolean makePersistent(Exchange exch) throws XmlRpcException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (exch.isDirty()) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            Vector v = new Vector();

            //we maken de filter aan zodat enkel dit object wordt gewijzigd
            v.add("id EQUALS " + exch.getSymbol());

            //we voegen nu de argumenten aan het bericht toe
            v.add(exch.toStruct());

            Integer result = (Integer) client.execute("Finance.Exchange.Modify", v);
            if (result == 1) {
                exch.setDirty(false);
                return true;
            }
            return false;

        }
        return true;
    }

    // </editor-fold>
    public boolean makePersistent(Security security) throws XmlRpcException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (security.isDirty()) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            Vector v = new Vector();

            //we maken de filter aan zodat enkel dit object wordt gewijzigd
            v.add("isin EQUALS '" + security.getISIN() + "'");

            //we voegen nu de argumenten aan het bericht toe
           
            v.add(security.toStruct());

            Integer result = (Integer) client.execute("Finance.Security.Modify", v);
            if (result == 1) {
                security.setDirty(false);
                return true;
            }
            return false;

        }
        return true;
    }
}
