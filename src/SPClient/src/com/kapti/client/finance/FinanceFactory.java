/*
 * FinanceFactory.java
 * StockPlay - Fabriek die ons allerhande financïele objecten teruggeeft die gesyncroniseerd kunnen worden met de database.
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
 * \brief   Fabriek die ons allerhande financïele objecten teruggeeft die gesyncroniseerd kunnen worden met de database.
 *
 */

public class FinanceFactory {

    private static FinanceFactory instance = new FinanceFactory();

    public static FinanceFactory getInstance() {
        return instance;
    }

    private FinanceFactory() {}

    /*
     * SECURITIES
     */

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

    /*
     * EXCHANGES
     */
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

    /*
     * QUOTES
     */

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

        //Controle ofdat er wel een latest quote bestaat
        Quote quote = null;
        if(it.hasNext())
            quote = it.next();

        return quote;

    }
    
    /*
     * INDEXEN
     */
    
    private HashMap<String, Index> indexes = null;

    public Index getIndexById(String isin) throws StockPlayException {

        if(indexes == null)
            cacheIndexes();
        return indexes.get(isin);
    }

    public Quote getLatestQuoteFromIndex(Index index) throws StockPlayException {
        Collection<Quote> quotes = getLatestQuoteByFilter("isin == '" + index.getISIN() + "'");
        Iterator<Quote> it = quotes.iterator();

        Quote quote = null;
        if(it.hasNext())
            quote = it.next();

        return quote;
    }

    private void cacheIndexes() throws StockPlayException {
        indexes = new HashMap<String, Index>();
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object[] obj = (Object[]) client.execute("Finance.Index.List", new Object[]{});

            for (Object i : obj) {
                Index index = Index.fromStruct((HashMap) i);
                indexes.put(index.getISIN(), index);
            }
        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }
}
