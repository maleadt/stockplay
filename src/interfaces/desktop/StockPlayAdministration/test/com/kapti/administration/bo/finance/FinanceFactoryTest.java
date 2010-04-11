/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.bo.finance;

import java.util.Collection;
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
public class FinanceFactoryTest {

    public FinanceFactoryTest() {
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
     * Test of getAllSecurities method, of class FinanceFactory.
     */
    @Test
    public void testGetAllSecurities() throws Exception {
        System.out.println("getAllSecurities");
        FinanceFactory instance = new FinanceFactory();
        Collection expResult = null;
        Collection result = instance.getAllSecurities();
        assertTrue(!result.isEmpty());

    }

    /**
     * Test of getAllExchanges method, of class FinanceFactory.
     */
    @Test
    public void testGetAllExchanges() throws Exception {
        System.out.println("getAllExchanges");
        FinanceFactory instance = new FinanceFactory();
        Collection result = instance.getAllExchanges();
        assertTrue(!result.isEmpty());

    }

    /**
     * Test of getExchange method, of class FinanceFactory.
     */
    @Test
    public void testGetExchange() throws Exception {
        System.out.println("getExchange");
        String symbol = "BSE";
        FinanceFactory instance = new FinanceFactory();
        Exchange result = instance.getExchange(symbol);
        assertEquals("Euronext Brussels", result.getName());
    }

//    /**
//     * Test of makePersistent method, of class FinanceFactory.
//     */
//    @Test
//    public void testMakePersistent_Exchange() throws Exception {
//        System.out.println("makePersistent");
//        Exchange exch = null;
//        FinanceFactory instance = new FinanceFactory();
//        instance.makePersistent(exch);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of makePersistent method, of class FinanceFactory.
//     */
//    @Test
//    public void testMakePersistent_Security() throws Exception {
//        System.out.println("makePersistent");
//        Security security = null;
//        FinanceFactory instance = new FinanceFactory();
//        instance.makePersistent(security);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

}