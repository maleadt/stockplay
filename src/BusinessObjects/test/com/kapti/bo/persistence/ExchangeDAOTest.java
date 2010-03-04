/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.bo.persistence;

import com.kapti.data.persistence.oracle.ExchangeDAO;
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

    public ExchangeDAOTest() {
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
     * Test of create method, of class ExchangeDAO.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        Exchange entity = new Exchange("TESTSYMBOL", "Test exchange", "Nowhere");
        ExchangeDAO instance = ExchangeDAO.getInstance();
        boolean expResult = true;
        boolean result = instance.create(entity);
        assertEquals(expResult, result);
    }

    /**
     * Test of findById method, of class ExchangeDAO.
     */
    @Test
    public void testFindById() throws Exception {
        System.out.println("findById");
        String symbol = "TESTSYMBOL";
        ExchangeDAO instance = ExchangeDAO.getInstance();

        Exchange result = instance.findById(symbol);
        assertNotNull(result);
        assertEquals(result.getSymbol(), "TESTSYMBOL");

    }

    /**
     * Test of findByExample method, of class ExchangeDAO.
     */
    @Test
    public void testFindByExample() throws Exception {
        System.out.println("findByExample");
        Exchange example = new Exchange();
        example.setLocation("Nowhere");
        ExchangeDAO instance = ExchangeDAO.getInstance();

        Collection<Exchange> result = instance.findByExample(example);
        for (Exchange ex : result) {
            System.out.println("\t " + ex.getSymbol() + ": " + ex.getName() + " - "+ ex.getLocation());
        }
        assertTrue(!result.isEmpty());
    }


        /**
     * Test of update method, of class ExchangeDAO.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        Exchange entity = new Exchange("TESTSYMBOL","Test exchange","Elsewhere");
        ExchangeDAO instance = ExchangeDAO.getInstance();
        boolean expResult = true;
        boolean result = instance.update(entity);
        assertEquals(expResult, result);
    }


    /**
     * Test of findAll method, of class ExchangeDAO.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        ExchangeDAO instance = ExchangeDAO.getInstance();

        Collection<Exchange> result = instance.findAll();
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
        ExchangeDAO instance = ExchangeDAO.getInstance();
        boolean expResult = true;
        boolean result = instance.delete(entity);
        assertEquals(expResult, result);
    }
}
