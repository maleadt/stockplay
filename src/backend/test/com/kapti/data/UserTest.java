/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data;

import com.kapti.data.User.Fields;
import java.util.Date;
import java.util.Hashtable;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thijs
 */
public class UserTest {

    public UserTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
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



    /**
     * Test of checkPassword method, of class User.
     */
    @Test
    public void testCheckPassword() {
        System.out.println("checkPassword");
        String password = "test";
        User instance = new User("nick","last","first", null);

        instance.setPassword("test");
        boolean expResult = true;
        boolean result = instance.checkPassword(password);
        assertEquals(expResult, result);

    }


}