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

import com.kapti.client.SPClientFactory;
import com.kapti.exceptions.RequestError;
import com.kapti.exceptions.StockPlayException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * \brief   Fabriek die ons allerhande financïele objecten teruggeeft die gesyncroniseerd kunnen worden met de database.
 *
 */
public class FinanceFactory {

    private static FinanceFactory instance = new FinanceFactory();
    private static final long SECURITY_CACHE_TIMEOUT = 1000 * 60;
    private static final long EXCHANGE_CACHE_TIMEOUT = 1000 * 60 * 2;
    private static final long INDEX_CACHE_TIMEOUT = 1000 * 60 * 2;

    public static FinanceFactory getInstance() {
        return instance;
    }

    private FinanceFactory() {}

    /*
     * SECURITIES
     */

    private HashMap<String, Security> securities = null;
    private Date securitiesCacheTime = null;

    private synchronized void cacheSecurities() throws StockPlayException {
        if (securities == null || (Calendar.getInstance().getTime().getTime() - securitiesCacheTime.getTime() > SECURITY_CACHE_TIMEOUT)) {
            securities = new HashMap<String, Security>();
            try {
                XmlRpcClient client = SPClientFactory.getPrivateClient();
                Object[] obj = (Object[]) client.execute("Finance.Security.List", new Object[]{});

                for (Object exch : obj) {
                    Security security = Security.fromStruct((HashMap) exch);
                    securities.put(security.getISIN(), security);
                }
                securitiesCacheTime = Calendar.getInstance().getTime();

            } catch (XmlRpcException ex) {
                throw new RequestError(ex);
            }
        }
    }

    public Collection<Security> getAllSecurities() throws StockPlayException {
        cacheSecurities();
        return securities.values();
    }

    public Collection<Security> getSecurityByFilter(String filter) throws StockPlayException {

        ArrayList<Security> result = new ArrayList<Security>();
        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
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

        cacheSecurities();
        return securities.get(isin);

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
    private Date exchangesCacheTime = null;

    private synchronized void cacheExchanges() throws StockPlayException {

        if (exchanges == null || (Calendar.getInstance().getTime().getTime() - exchangesCacheTime.getTime() > EXCHANGE_CACHE_TIMEOUT)) {
            exchanges = new HashMap<String, Exchange>();
            try {
                XmlRpcClient client = SPClientFactory.getPrivateClient();
                Object[] obj = (Object[]) client.execute("Finance.Exchange.List", new Object[]{});

                for (Object exch : obj) {
                    Exchange exchange = Exchange.fromStruct((HashMap) exch);
                    exchanges.put(exchange.getSymbol(), exchange);
                }
                exchangesCacheTime = Calendar.getInstance().getTime();
            } catch (XmlRpcException ex) {
                throw new RequestError(ex);
            }
        }
    }

    public Collection<Exchange> getAllExchanges() throws StockPlayException {
        cacheExchanges();
        return exchanges.values();
    }

    public Exchange getExchange(String symbol) throws StockPlayException {
        cacheExchanges();
        return exchanges.get(symbol);
    }

    public boolean makePersistent(Exchange exch) throws StockPlayException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (exch.isDirty()) {
            XmlRpcClient client = SPClientFactory.getPrivateClient();


            try {
                Integer result = (Integer) client.execute("Finance.Exchange.Modify", new Object[]{"id EQUALS '" + exch.getSymbol() + "'", exch.toStruct()});
                if (result == 1) {
                    exch.setDirty(false);
                    exchanges.put(exch.getSymbol(), exch);
                    return true;
                }
                return false;
            } catch (XmlRpcException ex) {
                throw new StockPlayException(ex);
            }

        }
        return true;
    }

    // </editor-fold>
    public boolean makePersistent(Security security) throws StockPlayException {
        //enkel als er veranderingen zijn moeten ze worden opgeslagen!
        if (security.isDirty()) {
            XmlRpcClient client = SPClientFactory.getPrivateClient();

            try {
                Integer result = (Integer) client.execute("Finance.Security.Modify", new Object[]{"isin EQUALS '" + security.getISIN() + "'", security.toStruct()});
                if (result == 1) {
                    security.setDirty(false);
                    securities.put(security.getISIN(), security);
                    return true;
                }
            } catch (XmlRpcException ex) {
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
            XmlRpcClient client = SPClientFactory.getPrivateClient();
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
        if (it.hasNext()) {
            quote = it.next();
        }

        return quote;

    }
    private HashMap<String, Index> indexes = null;
    private List<IndexSecurity> index_securities = null;
    private Date indexesCacheTime = null;
    private Date indexSecuritiesCacheTime = null;

    private synchronized void cacheIndexes() throws StockPlayException {
        if (indexes == null || (Calendar.getInstance().getTime().getTime() - indexesCacheTime.getTime() > INDEX_CACHE_TIMEOUT)) {
            indexes = new HashMap<String, Index>();
            try {
                XmlRpcClient client = SPClientFactory.getPrivateClient();
                Object[] result = (Object[]) client.execute("Finance.Index.List", new Object[]{});

                for (Object ind : result) {
                    Index index = Index.fromStruct((HashMap) ind);
                    indexes.put(index.getISIN(), index);
                }
                indexesCacheTime = Calendar.getInstance().getTime();


            } catch (XmlRpcException ex) {
                throw new RequestError(ex);
            }
        }
    }

    private synchronized void cacheIndexSecurities() throws StockPlayException {

        cacheIndexes();

        if (index_securities == null || (Calendar.getInstance().getTime().getTime() - indexSecuritiesCacheTime.getTime() > INDEX_CACHE_TIMEOUT)) {
            index_securities = new ArrayList<IndexSecurity>();
            try {
                XmlRpcClient client = SPClientFactory.getPrivateClient();
                Object[] result = (Object[]) client.execute("Finance.IndexSecurity.List", new Object[]{});

                for (Object index_sec : result) {
                    index_securities.add(IndexSecurity.fromStruct((HashMap) index_sec));
                }
                indexSecuritiesCacheTime = Calendar.getInstance().getTime();

            } catch (XmlRpcException ex) {
                throw new RequestError(ex);
            }

        }
    }

    public Collection<Index> getAllIndexes() throws StockPlayException {
        cacheIndexes();
        return indexes.values();
    }

    public Collection<Index> getIndexByFilter(String filter) throws StockPlayException {

        ArrayList<Index> result = new ArrayList<Index>();
        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
            Object[] res = (Object[]) client.execute("Finance.Index.List", new Object[]{filter});

            for (Object sec : res) {
                result.add(Index.fromStruct((HashMap) sec));
            }
            return result;

        } catch (XmlRpcException ex) {
            throw new RequestError(ex);
        }
    }

    public Index getIndexById(String isin) throws StockPlayException {
        cacheIndexes();
        return indexes.get(isin);

    }

    public Collection<Security> getSecuritiesFromIndex(Index index) throws StockPlayException {
        cacheIndexSecurities();

        List<Security> result = new ArrayList<Security>();

        for (IndexSecurity is : index_securities) {
            if (index.equals(is.getIndex())) {
                result.add(is.getSecurity());
            }
        }
        return result;

    }
}
