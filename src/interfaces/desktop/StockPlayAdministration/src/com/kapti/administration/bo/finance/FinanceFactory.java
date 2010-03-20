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

    public Collection<Security> getAllSecurities() throws StockPlayException {

        ArrayList<Security> result = new ArrayList<Security>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] securities = (Object[]) client.execute("Finance.Security.List", new Object[]{});

            for (Object sec : securities) {
                if (sec instanceof HashMap) {

                    HashMap security = (HashMap) sec;

                    Security res = new Security((String) security.get("ID"),
                            getExchange((String) security.get("EXCHANGE")),
                            (String) security.get("NAME"),
                            (Boolean) security.get("VISIBLE"),
                            (Boolean) security.get("SUSPENDED"));

                    result.add(res);
                }
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
                if (exch instanceof HashMap) {

                    HashMap hashObj = (HashMap) exch;

                    Exchange res = new Exchange( (String) hashObj.get("ID"),
                            (String) hashObj.get("NAME"),
                            (String) hashObj.get("LOCATION"));

                    exchanges.put(res.getSymbol(), res);
                } else {
                    throw new ErrorException("Expected Hashtable, but got an " + exch.getClass());
                }
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

    public void makePersistent(Exchange exch) throws XmlRpcException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (exch.isDirty()) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            Vector v = new Vector();

            //we maken de filter aan zodat enkel dit object wordt gewijzigd
            v.add("id EQUALS " + exch.getSymbol());

            //we voegen nu de argumenten aan het bericht toe
            Hashtable values = new Hashtable();
            values.put("NAME", exch.getName());
            values.put("LOCATION", exch.getLocation());
            v.add(values);

            client.execute("Finance.Exchange.Modify", v);
            exch.setDirty(false);
        }
    }

    // </editor-fold>
    public void makePersistent(Security security) throws XmlRpcException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (security.isDirty()) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            Vector v = new Vector();

            //we maken de filter aan zodat enkel dit object wordt gewijzigd
            v.add("symbol EQUALS " + security.getSymbol() + " AND exchange EQUALS " + security.getExchange().getSymbol());

            //we voegen nu de argumenten aan het bericht toe
            Hashtable values = new Hashtable();
            values.put("NAME", security.getName());
            values.put("VISIBLE", security.isVisible());
            values.put("SUSPENDED", security.isSuspended());
            v.add(values);

            client.execute("Finance.Security.Modify", v);
            security.setDirty(false);
        }
    }
    private ArrayList<Security> dummies = null;

    public Collection<Security> getSecurityDummies() {
        if (dummies == null) {
            dummies = new ArrayList<Security>();

            dummies.add(new Security("ABI", new Exchange("BSE", "Euronext Brussels", "Brussel"), "AB Inbev", true, false));
            dummies.add(new Security("ACKB", new Exchange("BSE", "Euronext Brussels", "Brussel"), "Ackermans&vHaar.", true, false));
            dummies.add(new Security("BEFB", new Exchange("BSE", "Euronext Brussels", "Brussel"), "Befimmo", true, false));
            dummies.add(new Security("BEKB", new Exchange("BSE", "Euronext Brussels", "Brussel"), "Bekaert", true, false));

        }
        return dummies;
    }

    /*
    public Quote getLatestQuote(Security security){



    }*/
}
