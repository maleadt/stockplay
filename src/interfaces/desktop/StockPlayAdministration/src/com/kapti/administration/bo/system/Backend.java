/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import com.kapti.administration.bo.XmlRpcClientFactory;
import com.kapti.exceptions.StockPlayException;
import java.util.HashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Thijs
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
