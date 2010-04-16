/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.user;

import java.util.Calendar;
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
public class UserFactoryTest {

    private static UserFactory instance;
    private static User user;

    public UserFactoryTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
        instance = new UserFactory();
        user = instance.createUser();
        user.setNickname("UserFactoryTestUser");
        user.setPassword("password");
        user.setLastname("Test");
        user.setFirstname("test");
        user.setEmail("test@test.com");
        //user.setRijksregisternummer(1234567890L);
        user.setRole(User.Role.USER);
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
        boolean result = instance.makePersistent(user);
        assertEquals(expResult, result);
        assertTrue(user.getId() > 0);
        System.out.println("Generated id: " + user.getId());

        System.out.println("makePersistent - update");
        user.setLastname("Test2");

        boolean resultUpdate = instance.makePersistent(user);
        assertEquals(true, resultUpdate);
    }

    /**
     * Test of getAllUsers method, of class UserFactory.
     */
    @Test
    public void testGetAllUsers() throws Exception {
        System.out.println("getAllUsers");
        Collection result = instance.getAllUsers();
        assertTrue(!result.isEmpty());
    }

    /**
     * Test of getUsersByFilter method, of class UserFactory.
     */
    @Test
    public void testGetUsersByFilter() throws Exception {
        System.out.println("getUsersByFilter");
        String filter = "nickname EQUALS 'UserFactoryTestUser'";

        Collection result = instance.getUsersByFilter(filter);
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
        boolean result = instance.verifyLogin(nickname, password);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeUser method, of class UserFactory.
     */
    @Test
    public void testRemoveUser() throws Exception {
        System.out.println("removeUser");
        boolean expResult = true;
        boolean result = instance.removeUser(user);
        assertEquals(expResult, result);
    }
}
