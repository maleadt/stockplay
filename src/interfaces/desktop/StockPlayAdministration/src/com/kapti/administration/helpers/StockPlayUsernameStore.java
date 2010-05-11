/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import java.util.HashSet;
import java.util.Set;
import org.jdesktop.swingx.auth.UserNameStore;

/**
 *
 * @author Thijs
 */
public class StockPlayUsernameStore extends UserNameStore {

    StockPlayPreferences prefs = new StockPlayPreferences();

    public StockPlayUsernameStore() {
    }



    Set<String> usernames = null;

    @Override
    public String[] getUserNames() {

        if(usernames== null)
            loadUserNames();

        String[] result = new String[usernames.size()];
        usernames.toArray(result);
        return result;
    }

    @Override
    public void setUserNames(String[] names) {
        usernames = new HashSet<String>();

        for(String name :names)
            usernames.add(name);
    }

    @Override
    public void loadUserNames() {
        usernames = prefs.getSavedUsernames();
    }

    @Override
    public void saveUserNames() {
        prefs.setSavedUsernames(usernames);
    }

    @Override
    public boolean containsUserName(String name) {
        for (String user : usernames) {
            if (user.equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addUserName(String userName) {
        usernames.add(userName);
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeUserName(String userName) {
        usernames.add(userName);
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}
