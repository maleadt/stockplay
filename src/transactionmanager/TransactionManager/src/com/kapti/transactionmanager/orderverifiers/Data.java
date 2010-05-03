/*
 * OrderVerifierFactory.java
 * StockPlay - Data cache
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

package com.kapti.transactionmanager.orderverifiers;

import com.kapti.client.XmlRpcClientFactory;
import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Quote;
import com.kapti.client.finance.Security;
import com.kapti.client.user.Order;
import com.kapti.client.user.OrderFactory;
import com.kapti.exceptions.StockPlayException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;

/**
 *
 * \brief   Data cache
 *
 */

public class Data {
    private Collection<Order> currentOrders;
    private OrderFactory orderFactory = OrderFactory.getInstance();
    private static Data ref;
    private HashMap<Security, Quote> currentQuotes;
    private static Logger logger = Logger.getLogger(Data.class);
    private static FinanceFactory financeFactory = FinanceFactory.getInstance();

    public static Data getReference() {
        if (ref == null)
            ref = new Data();
        return ref;
    }

    private Data() {

        // Clear cache
        XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
        try {
            logger.info("Clearing cache");
            client.execute("System.Backend.ClearCache", (Object[]) null);
        } catch (XmlRpcException ex) {
            logger.error("Failed to clear cache..");
        }

        // We halen alle pending orders op
        try {
            currentOrders = orderFactory.getAllPendingOrders();
            logger.info("Aantal orders gevonden: " + currentOrders.size());
        } catch (StockPlayException ex) {
            logger.error("Failed to fetch pending orders", ex);
        }

        // En we halen alle laatste quotes op
        currentQuotes = new HashMap<Security, Quote>();
        try {
            Collection<Quote> latestQuotes = financeFactory.getAllLatestQuotes();
            for (Quote q : latestQuotes)
                currentQuotes.put(q.getSecurity(), q);
        } catch (StockPlayException ex) {
            logger.error("Failed to fetch the latest quotes", ex);
        }

    }

    /**
     * @return the currentOrders
     */
    public Collection<Order> getCurrentOrders() {
        return currentOrders;
    }

    /**
     * @return the currentQuotes
     */
    public HashMap<Security, Quote> getCurrentQuotes() {
        return currentQuotes;
    }

    public double getHighest(Date from, Date to, String isin) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            GregorianCalendar startTijd = new GregorianCalendar();
            startTijd.setTime(new Date());
            SimpleDateFormat formaat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            String filter = "ISIN == '" + isin + "' AND TIMESTAMP < '" + formaat.format(to)
                            + "'d AND TIMESTAMP > '" + formaat.format(from) +"'d";
        try {
            return (Double) client.execute("Finance.Security.getHighest", new Object[]{filter});
        } catch (XmlRpcException ex) {
            logger.error("Failed to fetch the highest quotes", ex);
        }
        return 0;
    }

    public double getLowest(Date from, Date to, String isin) {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();

            GregorianCalendar startTijd = new GregorianCalendar();
            startTijd.setTime(new Date());
            SimpleDateFormat formaat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
            String filter = "ISIN == '" + isin + "' AND TIMESTAMP < '" + formaat.format(to)
                            + "'d AND TIMESTAMP > '" + formaat.format(from) +"'d";
        try {
            return (Double) client.execute("Finance.Security.getLowest", new Object[]{filter});
        } catch (XmlRpcException ex) {
            logger.error("Failed to fetch the lowest quotes", ex);
        }
        return 0;
    }
}