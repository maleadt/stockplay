/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.backend;

import com.kapti.data.Exchange;
import com.kapti.data.Security;
import com.kapti.exceptions.ErrorException;
import java.net.URL;
import java.util.HashMap;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

                Security res = new Security((String) security.get("ID"),
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
}
