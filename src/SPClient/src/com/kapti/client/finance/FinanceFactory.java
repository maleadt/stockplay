/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.client.finance;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
    private HashMap<String, Security> securities = null;

    private void cacheSecurities() throws StockPlayException {
        securities = new HashMap<String, Security>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] obj = (Object[]) client.execute("Finance.Security.List", new Object[]{});

            for (Object exch : obj) {
                Security security = Security.fromStruct((HashMap) exch);
                securities.put(security.getISIN(), security);
            }
        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Collection<Security> getAllSecurities() throws StockPlayException {
        if (securities == null) {
            cacheSecurities();
        }
        return securities.values();

        //return getSecurityByFilter("");
    }

    public Collection<Security> getSecurityByFilter(String filter) throws StockPlayException {

        ArrayList<Security> result = new ArrayList<Security>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] res = (Object[]) client.execute("Finance.Security.List", new Object[]{filter});

            for (Object sec : res) {
                result.add(Security.fromStruct((HashMap) sec));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Security getSecurityById(String isin) throws StockPlayException {

        if(securities == null)
            cacheSecurities();
        return securities.get(isin);

//        Collection<Security> users = getSecurityByFilter("isin == '" + isin + "'");
//        Iterator<Security> it = users.iterator();
//
//        if (it.hasNext()) {
//            return it.next();
//        } else {
//            return null;
//        }
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
            v.add("id EQUALS '" + exch.getSymbol() + "'");

            //we voegen nu de argumenten aan het bericht toe
            v.add(exch.toStruct());

            Integer result = (Integer) client.execute("Finance.Exchange.Modify", v);
            if (result == 1) {
                exch.setDirty(false);
                exchanges.put(exch.getSymbol(), exch);
                return true;
            }
            return false;

        }
        return true;
    }

    // </editor-fold>
    public boolean makePersistent(Security security) throws StockPlayException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (security.isDirty()) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            Vector v = new Vector();

            //we maken de filter aan zodat enkel dit object wordt gewijzigd
            v.add("isin EQUALS '" + security.getISIN() + "'");

            //we voegen nu de argumenten aan het bericht toe

            v.add(security.toStruct());
            try{
            Integer result = (Integer) client.execute("Finance.Security.Modify", v);
            if (result == 1) {
                security.setDirty(false);
                securities.put(security.getISIN(), security);
                return true;
            }
            }catch(XmlRpcException ex ){
                throw new StockPlayException("Error while saving security", ex);
            }
            return false;

        }
        return true;
    }

    public Collection<Quote> getAllLatestQuotes() throws StockPlayException {
        return getLatestQuoteByFilter("");



    }

    private Collection<Quote> getLatestQuoteByFilter(String filter) throws StockPlayException {
        ArrayList<Quote> result = new ArrayList<Quote>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] res = (Object[]) client.execute("Finance.Security.LatestQuotes", new Object[]{filter});

            for (Object quote : res) {
                result.add(Quote.fromStruct((HashMap) quote));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Quote getLatestQuoteFromSecurity(Security sec) throws StockPlayException {

        Collection<Quote> quotes = getLatestQuoteByFilter("isin == '" + sec.getISIN() + "'");
        Iterator<Quote> it = quotes.iterator();

        return quotes.iterator().next();

    }
}
