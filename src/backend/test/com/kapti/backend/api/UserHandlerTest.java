/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.backend.api;

import java.net.URL;
import java.util.HashMap;
import java.util.HashMap;
import junit.framework.Assert;
import org.apache.xmlrpc.XmlRpcException;
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
public class UserHandlerTest {

    public static enum Fields {

        ID, NICKNAME, PASSWORD, LASTNAME, FIRSTNAME, REGDATE, ROLE, POINTS, STARTAMOUNT, CASH, RRN, EMAIL
    }

    public UserHandlerTest() {
    }
    private static XmlRpcClient client = null;

    @BeforeClass
    public static void setUpClass() throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setEnabledForExtensions(true);
        config.setServerURL(new URL("http://localhost:6800/backend/public"));

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
    public void createUser() throws XmlRpcException {


        HashMap<String, Object> hash = new HashMap<String, Object>();
        hash.put(Fields.EMAIL.toString(), "test@blaat.com");
        hash.put(Fields.NICKNAME.toString(), "TESTUser2");
        hash.put(Fields.PASSWORD.toString(), "TESTPass");
        hash.put(Fields.LASTNAME.toString(), "TestLast");
        hash.put(Fields.FIRSTNAME.toString(), "TestFirst");
        hash.put(Fields.ROLE.toString(), 1);
        hash.put(Fields.RRN.toString(), "89020112355");

        Object result = client.execute("User.Create", new Object[]{hash});

        Assert.assertTrue(result instanceof Integer);
        Assert.assertTrue((Integer) result > 0);


    }

    @Test
    public void findUser() throws XmlRpcException {
        Object[] result = (Object[]) client.execute("User.List", new Object[]{"nickname EQUALS 'TESTUser2'"});

        Assert.assertEquals("Geen gebruikers met aangemaakte nickname gevonden", 1, result.length);

        HashMap hash = (HashMap) result[0];
        for(Object s : hash.keySet())
            System.out.println(s.toString());

        Assert.assertEquals("TestLast", hash.get(Fields.LASTNAME.toString()));
        Assert.assertEquals("TestFirst", hash.get(Fields.FIRSTNAME.toString()));
        Assert.assertEquals(1, hash.get(Fields.ROLE.toString()));
        System.out.println("Cash: " + hash.get(Fields.CASH.toString()));


    }

    @Test
    public void deleteUser() throws XmlRpcException {


        Object result = (Object) client.execute("User.Remove", new Object[]{"nickname EQUALS 'TESTUser2'"});
        Assert.assertEquals(1, result);

    }
}
