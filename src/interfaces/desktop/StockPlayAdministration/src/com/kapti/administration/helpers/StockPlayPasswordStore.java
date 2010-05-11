/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.administration.helpers;

import java.util.Map;
import org.jdesktop.swingx.auth.PasswordStore;

/**
 *
 * @author Thijs
 */
public class StockPlayPasswordStore extends PasswordStore{

    StockPlayPreferences prefs = new StockPlayPreferences();

    @Override
    public boolean set(String username, String server, char[] password) {
        prefs.setPassword(username, new String(password));
        return true;
    }

    @Override
    public char[] get(String username, String server) {
        return prefs.getPassword(username).toCharArray();
    }

    @Override
    public void removeUserPassword(String username) {
        prefs.removePassword(username);
    }

}
