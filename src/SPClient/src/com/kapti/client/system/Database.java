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
import com.kapti.client.SPClientFactory;
import com.kapti.exceptions.StockPlayException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Object waar we statistieken aan kunnen opvragen.
 *
 */

public class Database {

    static Logger logger = Logger.getLogger(Database.class);
    public Date latestStatsRequest = null;
    private double rate;
    private long uptime;

    public enum DatabaseStatus {
        UNKNOWN,
        MAINTANANCE,
        ONLINE;
    }

    public DatabaseStatus getStatus() throws StockPlayException {


        try {
            XmlRpcClient client = SPClientFactory.getPrivateClient();
            Integer result = (Integer) client.execute("System.Databse.Status", new Object[]{});

            if (result == 0) {
                return DatabaseStatus.MAINTANANCE;

            } else if (result == 1) {
                return DatabaseStatus.ONLINE;
            } else {
                return DatabaseStatus.UNKNOWN;
            }

        } catch (XmlRpcException ex) {
            logger.error("Error while fetching backend status", ex);
            return DatabaseStatus.UNKNOWN;
        }
    }

    private boolean updateStats() {
        //enkel opvragen als statistieken ouder dan een seconde zijn
        if (latestStatsRequest == null || Calendar.getInstance().getTime().getTime() - latestStatsRequest.getTime() > 1000) {

            try {
                XmlRpcClient client = SPClientFactory.getPrivateClient();
                Object result = client.execute("System.Database.Stats", new Object[]{});

                HashMap ht = (HashMap) result;

                rate = (Double) ht.get("rate");
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
     * Geeft het ratio van het aantal database-requests aan
     * @return
     */
    public double getRate() {
        updateStats();
        return rate;


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
