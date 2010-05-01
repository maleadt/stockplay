/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.backend.xmlrpc;

import com.kapti.data.User;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Thijs
 */
public class SessionsHandler {




    Map<String, User> sessions = new HashMap<String,User>();

    private static SessionsHandler instance = new SessionsHandler();

    public static SessionsHandler getInstance() {
        return instance;
    }

    private SessionsHandler(){}

    public boolean verifyRequest(String sessionid, String methodName){

        if(sessions.containsKey(sessionid)){

            User u = sessions.get(sessionid);

            return true;


        }
        else return false;



    }

}
