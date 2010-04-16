/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import org.jdesktop.swingx.auth.UserNameStore;

/**
 *
 * @author Thijs
 */
public class StockPlayUsernameStore extends UserNameStore {

    String[] usernames = new String[]{"Thijs", "Dieter", "Tim", "Laurens"};

    @Override
    public String[] getUserNames() {
        return usernames;
    }

    @Override
    public void setUserNames(String[] names) {
        usernames = names;
    }

    @Override
    public void loadUserNames() {
    }

    @Override
    public void saveUserNames() {
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
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeUserName(String userName) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}
