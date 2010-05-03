/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence;

import com.kapti.data.Exchange;
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
public class ExchangeDAOTest {

    private static StockPlayDAO mDAO;
    private static GenericDAO<Exchange, String> exchDAO;

    public ExchangeDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        mDAO = StockPlayDAOFactory.getDAO();
        exchDAO = mDAO.getExchangeDAO();
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
     * Test of create method, of class ExchangeDAO.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Exchange entity = new Exchange("TESTSYMBOL");
        entity.setName("Test exchange");
        entity.setLocation("Nowhere");
        int expResult = 1;
        int result = exchDAO.create(entity);
        assertEquals(expResult, result);
    }

    /**
     * Test of findById method, of class ExchangeDAO.
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        String symbol = "TESTSYMBOL";

        Exchange result = exchDAO.findById(symbol);
        assertNotNull(result);
        assertEquals(result.getSymbol(), "TESTSYMBOL");

    }

    /**
     * Test of update method, of class ExchangeDAO.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Exchange entity = new Exchange("TESTSYMBOL");
        entity.setName("Test exchange");
        entity.setLocation("Elsewhere");
        boolean expResult = true;
        boolean result = exchDAO.update(entity);
        assertEquals(expResult, result);
    }


    /**
     * Test of findAll method, of class ExchangeDAO.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");

        Collection<Exchange> result = exchDAO.findAll();
        for (Exchange ex : result) {
            System.out.println("\t " + ex.getSymbol() + ": " + ex.getName() + " - " + ex.getLocation());
        }
        assertTrue(!result.isEmpty());
    }


    /**
     * Test of delete method, of class ExchangeDAO.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        Exchange entity = new Exchange("TESTSYMBOL");
        boolean expResult = true;
        boolean result = exchDAO.delete(entity);
        assertEquals(expResult, result);
    }
}
