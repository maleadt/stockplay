/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence;

import com.kapti.data.persistence.oracle.SecurityDAO;
import com.kapti.data.persistence.oracle.ExchangeDAO;
import com.kapti.data.Exchange;
import com.kapti.data.Security;
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
public class SecurityDAOTest {

    private static Exchange testExch = new Exchange("TESTSYMBL2");
    static {
        testExch.setName("Test Exchange");
        testExch.setLocation("Nergens");
    }
    private static ExchangeDAO exchDAO = ExchangeDAO.getInstance();

    public SecurityDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        //we genereren een tijdelijke exchange om te testen
        exchDAO.create(testExch);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        //tijdelijke exchange verwijderen
        exchDAO.delete(testExch);
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class SecurityDAO.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Security security = new Security("BE0000000000", "TEST", "TESTSYMBL2");
        security.setName("Test");
        SecurityDAO instance = SecurityDAO.getInstance();
        int expResult = 1;
        int result = instance.create(security);
        assertEquals(expResult, result);

    }

    /**
     * Test of findById method, of class SecurityDAO.
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        String symbol = "BE0000000000";
        SecurityDAO instance = SecurityDAO.getInstance();
        Security result = instance.findById(symbol);
        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals("TESTSYMBL2", result.getExchange());

    }

//    /**
//     * Test of findByExample method, of class SecurityDAO.
//     */
//    @Test
//    public void testFindByExample() throws Exception {
//        System.out.println("findByExample");
//        Security example = new Security("DIT HEEFT EEN SYMBOOL NODIG"); // FIXME: findByExample << findByFilter??
//        example.setName("es");
//        SecurityDAO instance = SecurityDAO.getInstance();
//        Collection<Security> result = instance.findByExample(example);
//        assertTrue(!result.isEmpty());
//
//    }

    /**
     * Test of update method, of class SecurityDAO.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Security security = new Security("BE0000000000", "TEST", "TESTSYMBL2");
        security.setName("Test om up te daten");
        SecurityDAO instance = SecurityDAO.getInstance();
        boolean expResult = true;
        boolean result = instance.update(security);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAll method, of class SecurityDAO.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        SecurityDAO instance = SecurityDAO.getInstance();

        Collection<Security> result = instance.findAll();
        assertTrue(!result.isEmpty());

    }

    /**
     * Test of delete method, of class SecurityDAO.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        Security security = new Security("BE0000000000", "TEST", "TESTSYMBL2");
        security.setName("Test");
        SecurityDAO instance = SecurityDAO.getInstance();
        boolean expResult = true;
        boolean result = instance.delete(security);
        assertEquals(expResult, result);

    }
}
