/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.user;

import com.kapti.administration.bo.finance.FinanceFactory;
import com.kapti.administration.bo.finance.Security;
import java.util.Calendar;
import java.util.Collection;
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
public class UserFactoryTest {

    private static UserFactory userFactory;
    private static User user;
    private static Security security = null;
    private static Transaction transaction = null;
    private static Order order = null;

    public UserFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        userFactory = UserFactory.getInstance();
        user = userFactory.createUser();
        user.setNickname("UserFactoryTestUser");
        user.setPassword("password");
        user.setLastname("Test");
        user.setFirstname("test");
        user.setEmail("test@test.com");
        //user.setRijksregisternummer(1234567890L);
        user.setRole(User.Role.USER);

        Iterator<Security> it = FinanceFactory.getInstance().getAllSecurities().iterator();
        if (it.hasNext()) {
            security = it.next();
        }
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
     * Test of makePersistent method, of class UserFactory.
     */
    @Test
    public void testCreateandUpdateUser() throws Exception {
        System.out.println("makePersistent - insert");

        boolean expResult = true;
        boolean result = userFactory.makePersistent(user);
        assertEquals(expResult, result);
        assertTrue(user.getId() > 0);
        System.out.println("Generated id: " + user.getId());

        System.out.println("makePersistent - update");
        user.setLastname("Test2");

        boolean resultUpdate = userFactory.makePersistent(user);
        assertEquals(true, resultUpdate);
    }

    /**
     * Test of getAllUsers method, of class UserFactory.
     */
    @Test
    public void testGetAllUsers() throws Exception {
        System.out.println("getAllUsers");
        Collection result = userFactory.getAllUsers();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getUsersByFilter method, of class UserFactory.
     */
    @Test
    public void testGetUsersByFilter() throws Exception {
        System.out.println("getUsersByFilter");
        String filter = "nickname EQUALS 'UserFactoryTestUser'";

        Collection result = userFactory.getUsersByFilter(filter);
        assertTrue(!result.isEmpty());

    }

    /**
     * Test of verifyLogin method, of class UserFactory.
     */
    @Test
    public void testVerifyLogin() throws Exception {
        System.out.println("verifyLogin");
        String nickname = "UserFactoryTestUser";
        String password = "password";
        boolean expResult = true;
        boolean result = userFactory.verifyLogin(nickname, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of makePersistent method, of class TransactionFactory.
     */
    @Test
    public void testOrderMakePersistent() throws Exception {
        System.out.println("makePersistent");

        OrderFactory instance = OrderFactory.getInstance();
        Order t = instance.createOrder();
        t.setUser(user);
        t.setSecurity(security);
        t.setType(Order.Type.MANUAL);
        t.setAmount(10);
        t.setPrice(15.5);
        Calendar expCal = Calendar.getInstance();
        expCal.add(Calendar.DAY_OF_YEAR, 5);
        t.setExpirationTime(expCal.getTime());


        boolean expResult = true;
        boolean result = instance.makePersistent(t);
        assertEquals(expResult, result);
        order = t;
    }

    /**
     * Test of getAllOrders method, of class OrderFactory.
     */
    @Test
    public void testGetAllOrders() throws Exception {
        System.out.println("getAllOrders");
        OrderFactory instance = OrderFactory.getInstance();
        Collection result = instance.getAllOrders();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getOrdersByFilter method, of class OrderFactory.
     */
    @Test
    public void testGetOrdersByFilter() throws Exception {
        System.out.println("getOrdersByFilter");
        String filter = "userid =='" + user.getId() + "'";
        OrderFactory instance = OrderFactory.getInstance();

        Collection result = instance.getOrdersByFilter(filter);
        assertTrue(!result.isEmpty());
        Iterator<Order> it = result.iterator();
        assertTrue(it.hasNext());
        assertEquals(order, it.next());
    }

    /**
     * Test of getOrderById method, of class OrderFactory.
     */
    @Test
    public void testGetOrderById() throws Exception {
        System.out.println("getOrderById");
        int id = order.getId();
        OrderFactory instance = OrderFactory.getInstance();
        Order expResult = order;
        Order result = instance.getOrderById(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of makePersistent method, of class TransactionFactory.
     */
    @Test
    public void testTransactionMakePersistent() throws Exception {
        System.out.println("makePersistent");

        TransactionFactory instance = TransactionFactory.getInstance();
        Transaction t = instance.createTransaction();
        t.setUser(user);
        t.setSecurity(security);
        t.setType(Transaction.Type.MANUAL);
        t.setAmount(10);
        t.setPrice(15.5);
        t.setComment("test");
        t.setTime(Calendar.getInstance().getTime());

        boolean expResult = true;
        boolean result = instance.execute(t);
        assertEquals(expResult, result);
        transaction = t;
    }

    /**
     * Test of getAllTransactions method, of class TransactionFactory.
     */
    @Test
    public void testGetAllTransactions() throws Exception {
        System.out.println("getAllTransactions");
        TransactionFactory instance = TransactionFactory.getInstance();
        Collection result = instance.getAllTransactions();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getTransactionByFilter method, of class TransactionFactory.
     */
    @Test
    public void testGetTransactionByFilter() throws Exception {
        System.out.println("getTransactionByFilter");
        String filter = "userid == '" + user.getId() + "'";
        TransactionFactory instance = TransactionFactory.getInstance();
        Collection result = instance.getTransactionByFilter(filter);
        assertTrue(!result.isEmpty());
        Iterator it = result.iterator();

        assertTrue(it.hasNext());
        assertEquals(transaction, it.next());

    }

    /**
     * Test of getTransactionById method, of class TransactionFactory.
     */
    @Test
    public void testGetTransactionById() throws Exception {
        System.out.println("getTransactionById");
        int id = transaction.getId();
        TransactionFactory instance = TransactionFactory.getInstance();
        Transaction expResult = transaction;
        Transaction result = instance.getTransactionById(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeUser method, of class UserFactory.
     */
    @Test
    public void testRemoveUser() throws Exception {
        System.out.println("removeUser");
        boolean expResult = true;
        boolean result = userFactory.removeUser(user);
        assertEquals(expResult, result);
    }
}
