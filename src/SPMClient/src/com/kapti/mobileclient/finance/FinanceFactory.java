/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.finance;

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
public class FinanceFactory {

    private static FinanceFactory instance = new FinanceFactory();

    public static FinanceFactory getInstance() {
        return instance;
    }

    private FinanceFactory() {
    }

    public Vector getAllSecurities() throws StockPlayException {
        return getSecuritiesByFilter("");
    }

    public Vector getSecuritiesByFilter(String filter) throws StockPlayException {

        Vector result = new Vector();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector params = new Vector();
            params.addElement(filter);
            Vector res = (Vector) client.execute("Finance.Security.List", params);

            for (Enumeration e = res.elements(); e.hasMoreElements();) {
                result.addElement(Security.fromStruct((Hashtable) e.nextElement()));
            }

            return result;

        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public Security getSecurityById(String isin) throws StockPlayException {


        Vector securities = getSecuritiesByFilter("isin == '" + isin + "'");
        if (!securities.isEmpty()) {
            return (Security)securities.firstElement();
        } else {
            return null;
        }
    }
    // <editor-fold>
    private Hashtable exchanges = null;

    private void cacheExchanges() throws StockPlayException {
        exchanges = new Hashtable();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector obj = (Vector) client.execute("Finance.Exchange.List", new Vector());

            for(Enumeration e = obj.elements(); e.hasMoreElements();){
                Exchange exch = Exchange.fromStruct((Hashtable)e.nextElement());
                exchanges.put(exch.getSymbol(), exch);
            }
        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public Vector getAllExchanges() throws StockPlayException {
        if (exchanges == null) {
            cacheExchanges();
        }

        Vector result = new Vector();
        for(Enumeration e = exchanges.elements(); e.hasMoreElements();)
            result.addElement(e.nextElement());
        return result;
    }

    public Exchange getExchange(String symbol) throws StockPlayException {
        if (exchanges == null) {
            cacheExchanges();
        }
        return (Exchange) exchanges.get(symbol);
    }


    public Vector getAllLatestQuotes() throws StockPlayException {
        return getLatestQuoteByFilter("");



    }

    private Vector getLatestQuoteByFilter(String filter) throws StockPlayException {
        Vector result = new Vector();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Vector params = new Vector();
            params.addElement(filter);
            Vector res = (Vector) client.execute("Finance.Security.LatestQuotes", params);

            for(Enumeration e =res.elements(); e.hasMoreElements();)
                result.addElement(Quote.fromStruct((Hashtable)e.nextElement()));
            return result;

        } catch (Exception ex) {
            throw new RequestError(ex.getMessage());
        }
    }

    public Quote getLatestQuoteFromSecurity(Security sec) throws StockPlayException {

        Vector quotes = getLatestQuoteByFilter("isin == '" + sec.getISIN() + "'");
        if(!quotes.isEmpty())
            return (Quote)quotes.firstElement();
        else
            return null;

    }
}
