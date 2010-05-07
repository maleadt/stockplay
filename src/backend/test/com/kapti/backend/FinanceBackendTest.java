/*
 * SystemBackandTest.java
 * StockPlay - Testcase voor de Finance Backand
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

package com.kapti.backend;

import com.kapti.data.Exchange;
import com.kapti.data.Quote;
import com.kapti.data.Security;
import com.kapti.exceptions.InternalException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

public class FinanceBackendTest {

    private static XmlRpcClient client = null;

    @BeforeClass
    public static void setUpClass() throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:6800/backend/public"));
        client = new XmlRpcClient();

        client.setConfig(config);

    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getSecuritiesTest() throws Exception {
        Object[] securities = (Object[]) client.execute("Finance.Security.List", new Object[]{});

        for (Object sec : securities) {
            if (sec instanceof HashMap) {

                HashMap security = (HashMap) sec;

                Security res = new Security((String) security.get("ISIN"), (String) security.get("SYMBOL"), (String) security.get("EXCHANGE"));
                res.setName((String) security.get("NAME"));
                res.setVisible((Boolean) security.get("VISIBLE"));
                res.setSuspended((Boolean) security.get("SUSPENDED"));

                System.out.println(res.getSymbol() + ": " + res.getName() + " | visible: " + res.isVisible() + " | suspended: " + res.isSuspended());
            }
        }

    }

    @Test
    public void getSecurityTest() throws Exception {
        Object[] securities = (Object[]) client.execute("Finance.Security.List", new Object[]{"symbol EQUALS 'GSZ'"});

        Assert.assertTrue(securities.length == 1);

        for (Object sec : securities) {
            if (sec instanceof HashMap) {

                HashMap security = (HashMap) sec;

                Security res = new Security((String) security.get("ISIN"), (String) security.get("SYMBOL"), (String) security.get("EXCHANGE"));
                res.setName((String) security.get("NAME"));
                res.setVisible((Boolean) security.get("VISIBLE"));
                res.setSuspended((Boolean) security.get("SUSPENDED"));

                System.out.println(res.getSymbol() + ": " + res.getName() + " | visible: " + res.isVisible() + " | suspended: " + res.isSuspended());
            }
        }

    }

    @Test
    public void getExchangesTest() throws Exception {
        Object[] obj = (Object[]) client.execute("Finance.Exchange.List", new Object[]{});

        for (Object exch : obj) {
            if (exch instanceof HashMap) {
                HashMap hashObj = (HashMap) exch;

                Exchange res = new Exchange((String) hashObj.get("ID"));
                res.setName((String) hashObj.get("NAME"));
                res.setLocation((String) hashObj.get("LOCATION"));
            } else {
                throw new Exception("Expected HashMap, but got an " + exch.getClass());
            }
        }
    }

    @Test
    public void getExchangeTest() throws Exception {
        Object[] obj = (Object[]) client.execute("Finance.Exchange.List", new Object[]{"symbol EQUALS 'BSE'"});


        Assert.assertTrue(obj.length == 1);

        for (Object exch : obj) {
            if (exch instanceof HashMap) {
                HashMap hashObj = (HashMap) exch;

                Exchange res = new Exchange((String) hashObj.get("ID"));
                res.setName((String) hashObj.get("NAME"));
                res.setLocation((String) hashObj.get("LOCATION"));
            } else {
                throw new Exception("Expected HashMap, but got an " + exch.getClass());
            }
        }
    }

    @Test
    public void getSecurityDetailsTest() throws Exception {
        Object[] quotes = (Object[]) client.execute("Finance.Security.Details", new Object[]{"isin EQUALS 'AMZN'"});

        //Assert.assertTrue(securities.length == 1);

        for (Object q : quotes) {
            if (q instanceof HashMap) {

                HashMap quote = (HashMap) q;

                Quote res = new Quote("AMZN", (Date) quote.get("TIME"));
                res.setPrice((Double) quote.get("PRICE"));
                res.setVolume((Integer) quote.get("VOLUME"));
                res.setBid((Double) quote.get("BID"));
                res.setAsk((Double) quote.get("ASK"));
                res.setLow((Double) quote.get("LOW"));
                res.setHigh((Double) quote.get("HIGH"));
                res.setOpen((Double) quote.get("OPEN"));

                System.out.println(res.getIsin() + " op " + res.getTime() + ": " + res.getVolume() + " | visible: " + res.getPrice());
            }
        }
    }



    @Test
    public void getSecurityQuotesTest() throws Exception {
        Object[] quotes = (Object[]) client.execute("Finance.Security.Quotes", new Object[]{"isin EQUALS 'EBAY'"});

        //Assert.assertTrue(quotes.length == 1);

        for (Object q : quotes) {
            if (q instanceof HashMap) {

                HashMap quote = (HashMap) q;

                Quote res = new Quote("AMZN", (Date) quote.get("TIME"));
                res.setPrice((Double) quote.get("PRICE"));
                res.setVolume((Integer) quote.get("VOLUME"));
                res.setBid((Double) quote.get("BID"));
                res.setAsk((Double) quote.get("ASK"));
                res.setLow((Double) quote.get("LOW"));
                res.setHigh((Double) quote.get("HIGH"));
                res.setOpen((Double) quote.get("OPEN"));

                System.out.println(res.getIsin() + " op " + res.getTime() + ": " + res.getVolume() + " | visible: " + res.getPrice());
            }
        }
    }
}