/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence.oracle;

import com.kapti.data.PointsTransaction;
import com.kapti.data.User;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
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
public class PointsTransactionDAOTest {

    private static UserDAO uDAO;
    private static PointsTransactionDAO instance;
    private static Date time;
    private static int userid;

    public PointsTransactionDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = PointsTransactionDAO.getInstance();
        time = Calendar.getInstance().getTime();
        uDAO = UserDAO.getInstance();

        User u = new User(-1, "TESTPoints", "t@t.com", "az", "be", time);
        u.setPassword("test");
        userid = uDAO.create(u);

        System.out.println("Created user " + userid);



    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        uDAO.delete(uDAO.findById(userid));
        System.out.println("Deleted user " + userid);

    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of create method, of class PointsTransactionDAO.
     */
    @Test
    public void testCreate() throws Exception {
        System.out.println("create");
        PointsTransaction entity = new PointsTransaction(userid, time);
        entity.setDelta(100);
        entity.setComments("testsuite");

        int expResult = 1;
        int result = instance.create(entity);
        assertEquals(expResult, result);
    }

    /**
     * Test of findById method, of class PointsTransactionDAO.
     */
//    @Test
//    public void testFindById() throws Exception {
//        System.out.println("findById");
//        PointsTransactionPK pk = new PointsTransactionPK(userid,time);
//        PointsTransaction expResult = null;
//        PointsTransaction result = instance.findById(pk);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
    /**
     * Test of findByFilter method, of class PointsTransactionDAO.
     */
    @Test
    public void testFindByFilter() throws Exception {
        System.out.println("findByFilter");

        Filter iFilter = Parser.getInstance().parse("userid EQUALS '" + userid + "'");
        Collection result = instance.findByFilter(iFilter);
        assertTrue(!result.isEmpty());

        Iterator it = result.iterator();
        while (it.hasNext()) {
            PointsTransaction pt = (PointsTransaction) it.next();
            assertEquals(userid, pt.getUser());
            assertEquals(time, new Date(pt.getTimestamp().getTime()));
        }
    }

    /**
     * Test of findAll method, of class PointsTransactionDAO.
     */
    @Test
    public void testFindAll() throws Exception {
        System.out.println("findAll");
        Collection result = instance.findAll();
        assertTrue(!result.isEmpty());

        Iterator it = result.iterator();
        while (it.hasNext()) {
            PointsTransaction pt = (PointsTransaction) it.next();
            if (userid == pt.getUser()) {
                assertEquals(time, pt.getTimestamp());
            }
        }
    }

    /**
     * Test of update method, of class PointsTransactionDAO.
     */
    @Test
    public void testUpdate() throws Exception {
        System.out.println("update");
        PointsTransaction entity = new PointsTransaction(userid, time);
        entity.setDelta(200);
        entity.setComments("testsuite2");
        boolean expResult = true;
        boolean result = instance.update(entity);
        assertEquals(expResult, result);
    }

    /**
     * Test of delete method, of class PointsTransactionDAO.
     */
    @Test
    public void testDelete() throws Exception {
        System.out.println("delete");
        PointsTransaction entity = new PointsTransaction(userid, time);
        boolean expResult = true;
        boolean result = instance.delete(entity);
        assertEquals(expResult, result);
    }
}
