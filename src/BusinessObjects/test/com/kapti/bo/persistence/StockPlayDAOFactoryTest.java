/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo.persistence;

import com.kapti.data.persistence.StockPlayDAO;
import com.kapti.data.persistence.StockPlayDAOFactory;
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
public class StockPlayDAOFactoryTest {

    public StockPlayDAOFactoryTest() {
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
     * Test of getDAO method, of class StockPlayDAOFactory.
     */
    @Test
    public void testGetDAO() throws Exception {
        System.out.println("getDAO");

        StockPlayDAO result = StockPlayDAOFactory.getDAO();
        assertTrue(result instanceof com.kapti.data.persistence.oracle.OracleStockPlayDAO);

    }

}