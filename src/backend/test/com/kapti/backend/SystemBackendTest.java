/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.backend;

import com.kapti.data.Security;
import com.kapti.exceptions.StockPlayException;
import java.net.URL;
import java.util.ArrayList;
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
public class SystemBackendTest {

    public SystemBackendTest() {
    }
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
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void getStatsTest() throws Exception {
        ArrayList<Security> result = new ArrayList<Security>();


        Object obj = client.execute("System.Backend.Stats", new Object[]{});

        if (obj instanceof HashMap) {
            HashMap<String, Object> h = (HashMap<String, Object>) obj;

            for (String s : h.keySet()) {
                System.out.println(s + ": " + h.get(s).toString());
            }

        } else {
            throw new StockPlayException("Expected Hashtable, but got an " + obj.getClass());
        }
    }
}
