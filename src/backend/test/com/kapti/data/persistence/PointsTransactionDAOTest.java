/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence;

import com.kapti.data.PointsTransaction;
import com.kapti.data.PointsTransaction.PointsTransactionPK;
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

    private static StockPlayDAO mDAO;
    private static GenericDAO<User, Integer> userDAO;
    private static GenericDAO<PointsTransaction, PointsTransactionPK> pointsDAO;
    private static Date time;
    private static int userid;

    public PointsTransactionDAOTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        mDAO = StockPlayDAOFactory.getDAO();
        pointsDAO = mDAO.getPointsTransactionDAO();
        userDAO = mDAO.getUserDAO();
        
        time = Calendar.getInstance().getTime();

        User u = new User(-1);
        u.setNickname("TESTPoints");
        u.setEmail("t@t.com");
        u.setFirstname("az");
        u.setLastname("be");
        u.setPassword("test");
        u.setRegdate(Calendar.getInstance().getTime());
        userid = userDAO.create(u);

        System.out.println("Created user " + userid);



    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        userDAO.delete(userDAO.findById(userid));
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
        int result = pointsDAO.create(entity);
        assertEquals(expResult, result);
    }
    
    /**
     * Test of findByFilter method, of class PointsTransactionDAO.
     */
    @Test
    public void testFindByFilter() throws Exception {
        System.out.println("findByFilter");

        Filter iFilter = Parser.getInstance().parse("userid EQUALS '" + userid + "'");
        Collection result = pointsDAO.findByFilter(iFilter);
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
        Collection result = pointsDAO.findAll();
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
        boolean result = pointsDAO.update(entity);
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
        boolean result = pointsDAO.delete(entity);
        assertEquals(expResult, result);
    }
}
