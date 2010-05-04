/*
 * Backend.java
 * StockPlay - Object waar we statistieken aan kunnen opvragen.
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

package com.kapti.client.system;

import java.util.Calendar;
import java.util.Date;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import com.kapti.client.XmlRpcClientFactory;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Object waar we statistieken aan kunnen opvragen.
 *
 */

public class Backend {

    static Logger logger = Logger.getLogger(Backend.class);
    public Date latestStatsRequest = null;
    private int onlineUsers;
    private int processedRequests;
    private long uptime;

    public enum BackendStatus {
        UNKNOWN,
        MAINTANANCE,
        ONLINE;
    }

    public BackendStatus getStatus() {


        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Integer result = (Integer) client.execute("System.Backend.Status", new Object[]{});

            if (result == 0) {
                return BackendStatus.MAINTANANCE;

            } else if (result == 1) {
                return BackendStatus.ONLINE;
            } else {
                return BackendStatus.UNKNOWN;
            }

        } catch (XmlRpcException ex) {
            logger.error("Error while fetching backend status", ex);
            return BackendStatus.UNKNOWN;
        }
    }

    private boolean updateStats() {
        //enkel opvragen als statistieken ouder dan een seconde zijn
        if (latestStatsRequest == null || Calendar.getInstance().getTime().getTime() - latestStatsRequest.getTime() > 1000) {

            try {
                XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
                Object result = client.execute("System.Backend.Stats", new Object[]{});

                HashMap ht = (HashMap) result;

                //users ophalen
                onlineUsers = (Integer) ht.get("users");
                processedRequests = (Integer) ht.get("req");
                uptime = Long.parseLong((String) ht.get("uptime"));

                latestStatsRequest = Calendar.getInstance().getTime();
                return true;

            } catch (Exception ex) {
                logger.error("Error while fetching statistics", ex);
                return false;
            }
        } else {
            return true;


        }

    }

    /**
     * Geeft het aantal gebruikers dat momenteel online is.
     * @return
     */
    public int getOnlineUsers() {
        updateStats();
        return onlineUsers;


    }

    /**
     * Geeft het aantal XML-RPC-requests dat is verwerkt sinds de laatste herstart van de backend.
     * @return
     */
    public int getProcessedRequests() {
        updateStats();
        return processedRequests;


    }

    /**
     * Geeft het aantal seconden dat de backend al online is aan 1 stuk door
     * @return
     */
    public long getUptime() {
        updateStats();
        return uptime;

    }
}
