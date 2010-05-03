/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence;

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

    private static StockPlayDAO mDAO;
    private static GenericDAO<Exchange, String> exchDAO;
    private static GenericDAO<Security, String> secDAO;

    private static Exchange testExch = new Exchange("TESTSYMBL2");

    public SecurityDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        mDAO = StockPlayDAOFactory.getDAO();
        exchDAO = mDAO.getExchangeDAO();
        secDAO = mDAO.getSecurityDAO();
        
        //we genereren een tijdelijke exchange om te testen
        testExch.setName("Test Exchange");
        testExch.setLocation("Nergens");
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
        int expResult = 1;
        int result = secDAO.create(security);
        assertEquals(expResult, result);

    }

    /**
     * Test of findById method, of class SecurityDAO.
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        String symbol = "BE0000000000";
        Security result = secDAO.findById(symbol);
        assertNotNull(result);
        assertEquals("Test", result.getName());
        assertEquals("TESTSYMBL2", result.getExchange());

    }

    /**
     * Test of update method, of class SecurityDAO.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Security security = new Security("BE0000000000", "TEST", "TESTSYMBL2");
        security.setName("Test om up te daten");
        boolean expResult = true;
        boolean result = secDAO.update(security);
        assertEquals(expResult, result);

    }

    /**
     * Test of findAll method, of class SecurityDAO.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");

        Collection<Security> result = secDAO.findAll();
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
        
        boolean expResult = true;
        boolean result = secDAO.delete(security);
        assertEquals(expResult, result);

    }
}
