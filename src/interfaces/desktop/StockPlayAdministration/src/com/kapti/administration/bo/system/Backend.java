/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.system;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.Hashtable;
import java.util.logging.Logger;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import com.kapti.administration.bo.XmlRpcClientFactory;
import com.kapti.exceptions.StockPlayException;

/**
 *
 * @author Thijs
 */
public class Backend {

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
            Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
            return BackendStatus.UNKNOWN;
        }
    }

    private boolean updateStats() throws StockPlayException {
        try {
            XmlRpcClient client = XmlRpcClientFactory.getXmlRpcClient();
            Object result = client.execute("System.Backend.Stats", new Object[]{});

            if (result instanceof Hashtable) {
                Hashtable ht = (Hashtable) result;

                //users ophalen
                Object users = ht.get("users");
                if(users != null)
                    onlineUsers = (Integer) users;
                else
                    throw new StockPlayException("");
                processedRequests = (Integer) ht.get("req");
                uptime = (Long) ht.get("uptime");

                return true;
            } else
                return false;

        } catch (XmlRpcException ex) {
            Logger.getLogger(Backend.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

    }

    /**
     * Geeft het aantal gebruikers dat momenteel online is.
     * @return
     */
    public int getOnlineUsers() {

        //enkel opvragen als statistieken ouder dan een seconde zijn
        if (latestStatsRequest == null || Calendar.getInstance().getTime().getTime() - latestStatsRequest.getTime() > 1000) {
        }
        return 0;
    }

    /**
     * Geeft het aantal XML-RPC-requests dat is verwerkt sinds de laatste herstart van de backend.
     * @return
     */
    public int getProcessedRequests() {
        return 0;
    }

    /**
     * Geeft het aantal seconden dat de backend al online is aan 1 stuk door
     * @return
     */
    public long getUptime() {
        return 0;
    }
}
