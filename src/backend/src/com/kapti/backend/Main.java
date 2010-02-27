/*
 * Main.java
 * StockPlay - Main class
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

import com.kapti.backend.xmlrpc.Servlet;
import org.apache.log4j.Logger;
import org.apache.xmlrpc.webserver.ServletWebServer;
import org.apache.xmlrpc.webserver.XmlRpcServlet;

/**
 *
 * @author tim
 */
public class Main {

    private static final int mPort = 8080;
    static Logger mLogger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        /*
         * Start de servlet server.
         * Door het gebruik van servlets kan er later makkelijk omgeschakeld
         * worden naar een volwaardige webserver. Tot we dit doen zorgt de
         * servlet-api-x.y.jar library voor de nodige servlet klassen.
         */
        mLogger.info("Starting servlet server.");
        XmlRpcServlet servlet = new Servlet();
        ServletWebServer webServer = new ServletWebServer(servlet, mPort);
        webServer.start();
    }
}
