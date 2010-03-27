package com.kapti.data.persistence;

import com.kapti.data.Exchange;
import com.kapti.data.Security;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Dieter
 */
public class SecurityXMLTest {

    private static XmlRpcClient client;

    public SecurityXMLTest() {
    }

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
    public void createSecurity() throws Exception {
        //Dummygegevens genereren
        Hashtable<String, Object> iDetails = new Hashtable<String, Object>();
        iDetails.put(Security.Fields.ISIN.name(), "BE123456789");
        iDetails.put(Security.Fields.SYMBOL.name(), "AAAA");
        iDetails.put(Security.Fields.NAME.name(), "JUnit testaandeel");

        iDetails.put(Security.Fields.EXCHANGE.name(), "PA");

        iDetails.put(Security.Fields.VISIBLE.name(), true);
        iDetails.put(Security.Fields.SUSPENDED.name(), false);

        client.execute("Finance.Security.Create", new Object[] {iDetails});
    }

    @Test
    public void getSecurity() throws Exception {
        Object[] result = (Object[]) client.execute("Finance.Security.List", new Object[] {"isin EQUALS 'BE123456789'"});

        if(result.length != 1)
            fail("Expected one object, got " + result.length);

        if(result[0] instanceof HashMap) {
            HashMap securityMap = (HashMap) result[0];

            assertEquals(securityMap.get("ISIN"), "BE123456789");
            assertEquals(securityMap.get("SYMBOL"), "AAAA");
            assertEquals(securityMap.get("NAME"), "JUnit testaandeel");
            assertEquals(securityMap.get("EXCHANGE"), "PA");
            assertEquals(securityMap.get("VISIBLE"), true);
            assertEquals(securityMap.get("SUSPENDED"), false);
        }
        else
            fail("Expected HashMap, got " + result[0].getClass().getName());
    }

    @Test
    public void modifySecurity() throws Exception {
        Hashtable<String, Object> iDetails = new Hashtable<String, Object>();

        iDetails.put("SYMBOL", "AAAB");
        iDetails.put("NAME", "JUnit aangepast testaandeel");
        iDetails.put("EXCHANGE", "NASDAQ");
        iDetails.put("VISIBLE", false);
        iDetails.put("SUSPENDED", true);

        int result = (Integer) client.execute("Finance.Security.Modify", new Object[] {"isin EQUALS 'BE123456789", iDetails});

        assertTrue(result + " items affected, expected only one change",result == 1);
    }

    @Test
    public void deleteSecurity() throws Exception {
        int result = (Integer) client.execute("Finance.Security.Remove", new Object[] {"isin EQUALS 'BE123456789'"});

        assertTrue(result + " items deleted, expected only one deleted item", result == 1);


    }

}