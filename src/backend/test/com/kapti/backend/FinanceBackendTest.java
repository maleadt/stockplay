/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.backend;

import com.kapti.data.Exchange;
import com.kapti.data.Quote;
import com.kapti.data.Security;
import com.kapti.exceptions.ErrorException;
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

/**
 *
 * @author Thijs
 */
public class FinanceBackendTest {

    private static XmlRpcClient client = null;

    @BeforeClass
    public static void setUpClass() throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:8080/xmlrpc"));
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

                Security res = new Security((String) security.get("ISIN"),
                        (String) security.get("SYMBOL"),
                        (String) security.get("NAME"),
                        (String) security.get("EXCHANGE"),
                        (Boolean) security.get("VISIBLE"),
                        (Boolean) security.get("SUSPENDED"));

                System.out.println(res.getSymbol() + ": " + res.getName() + " | visible: " + res.isVisible() + " | suspended: " + res.isSuspended());
            }
        }

    }

    @Test
    public void getSecurityTest() throws Exception {
        Object[] securities = (Object[]) client.execute("Finance.Security.List", new Object[]{"symbol EQUALS 'AMZN'"});

        Assert.assertTrue(securities.length == 1);

        for (Object sec : securities) {
            if (sec instanceof HashMap) {

                HashMap security = (HashMap) sec;

                Security res = new Security((String) security.get("ISIN"),
                        (String) security.get("SYMBOL"),
                        (String) security.get("NAME"),
                        (String) security.get("EXCHANGE"),
                        (Boolean) security.get("VISIBLE"),
                        (Boolean) security.get("SUSPENDED"));

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

                Exchange res = new Exchange((String) hashObj.get("ID"),
                        (String) hashObj.get("NAME"),
                        (String) hashObj.get("LOCATION"));
            } else {
                throw new ErrorException("Expected Hashtable, but got an " + exch.getClass());
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

                Exchange res = new Exchange((String) hashObj.get("ID"),
                        (String) hashObj.get("NAME"),
                        (String) hashObj.get("LOCATION"));
            } else {
                throw new ErrorException("Expected Hashtable, but got an " + exch.getClass());
            }
        }
    }

    @Test
    public void getSecurityDetailsTest() throws Exception {
        Object[] quotes = (Object[]) client.execute("Finance.Security.Details", new Object[]{"symbol EQUALS 'AMZN'"});

        //Assert.assertTrue(securities.length == 1);

        for (Object q : quotes) {
            if (q instanceof HashMap) {

                HashMap quote = (HashMap) q;

                Quote res = new Quote("AMZN",
                        (Date) quote.get("TIME"),
                        (Double) quote.get("PRICE"),
                        (Integer) quote.get("VOLUME"),
                        (Double) quote.get("BID"),
                        (Double) quote.get("ASK"),
                        (Double) quote.get("LOW"),
                        (Double) quote.get("HIGH"),
                        (Double) quote.get("OPEN"));

                System.out.println(res.getIsin() + " op " + res.getTime() + ": " + res.getVolume() + " | visible: " + res.getPrice());
            }
        }
    }



    @Test
    public void getSecurityQuotesTest() throws Exception {
        Object[] quotes = (Object[]) client.execute("Finance.Security.Quotes", new Object[]{"symbol EQUALS 'EBAY'"});

        //Assert.assertTrue(quotes.length == 1);

        for (Object q : quotes) {
            if (q instanceof HashMap) {

                HashMap quote = (HashMap) q;

                Quote res = new Quote("EBAY",
                        (Date) quote.get("TIME"),
                        (Double) quote.get("PRICE"),
                        (Integer) quote.get("VOLUME"),
                        (Double) quote.get("BID"),
                        (Double) quote.get("ASK"),
                        (Double) quote.get("LOW"),
                        (Double) quote.get("HIGH"),
                        (Double) quote.get("OPEN"));

                System.out.println(res.getIsin() + " op " + res.getTime() + ": " + res.getVolume() + " | visible: " + res.getPrice());
            }
        }
    }
}
