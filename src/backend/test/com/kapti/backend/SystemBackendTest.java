/*
 * SystemBackandTest.java
 * StockPlay - Testcase voor de System Backand
 *
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kapti.backend;

import com.kapti.data.Security;
import com.kapti.exceptions.InternalException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class SystemBackendTest {

    public SystemBackendTest() {
    }
    private static XmlRpcClient client = null;

    @BeforeClass
    public static void setUpClass() throws Exception {

        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://localhost:6800/backend/public"));
        client = new XmlRpcClient();

        client.setConfig(config);

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

    @Test
    public void getStatsTest() throws Exception {
        ArrayList<Security> result = new ArrayList<Security>();


        Object obj = client.execute("System.Backend.Stats", new Object[]{});

        if (obj instanceof HashMap) {
            HashMap<String, Object> h = (HashMap<String, Object>) obj;

            for (String s : h.keySet()) {
                System.out.println(s + ": " + h.get(s).toString());
            }

        } else {
            throw new InternalException(InternalException.Type.INTERNAL_FAILURE, "Expected Hashtable, but got an " + obj.getClass());
        }
    }
}