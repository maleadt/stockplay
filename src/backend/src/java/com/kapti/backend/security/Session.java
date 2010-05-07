/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.backend.security;

import com.kapti.data.User;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public class Session {


    protected String sessionid;

    /**
     * Get the value of sessionid
     *
     * @return the value of sessionid
     */
    public String getSessionid() {
        return sessionid;
    }

    protected User user;

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public User getUser() {
        return user;
    }

    public Session(String sessionid, User user) {
        this.sessionid = sessionid;
        this.user = user;
        this.lastActivity = Calendar.getInstance().getTime();
    }



    protected Date lastActivity;

    /**
     * Get the value of lastActivity
     *
     * @return the value of lastActivity
     */
    public Date getLastActivity() {
        return lastActivity;
    }


    /**
     * Deze functie maakt de sessie actief, en verzet de timeout
     */
    public void recordActivity() {
        lastActivity = Calendar.getInstance().getTime();
    }


    /**
     * Geeft aan of de sessie timed-out is of niet.. De timeout vindt plaats na 2 uur
     * @return
     */
    public boolean sessionTimedOut(){

        return (Calendar.getInstance().getTime().getTime() - lastActivity.getTime()) > 1000*60*60*2;

    }

}
