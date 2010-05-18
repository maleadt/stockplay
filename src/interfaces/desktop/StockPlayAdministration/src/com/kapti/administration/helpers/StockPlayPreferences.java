/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 *
 * @author Thijs
 */
public class StockPlayPreferences {

    Preferences prefs = Preferences.userNodeForPackage(StockPlayPreferences.class);
    private static final String SERVERURLPREF = "serverurl";
    private static final String LOCALEPREF = "locale";
    private static final String USERNAMESPREF = "usernames";
    private static final String SAVEUSERNAMESPREF = "saveusernames";
    private static final String SAVEPASSWORDPREF = "savepassword";
    private static final String PASSWORDSNODE = "passwords";
    private static final String LOGINWITHEIDPREF = "loginwitheid";
    private static final String EIDADMINUSERNAMEPREF = "eid-username";
    private static final String EIDADMINPASSWORDPREF = "eid-password";
    private static Locale[] supportedLocales = {new Locale("nl", "be"), Locale.ENGLISH};



        public String getServerURL() {

        return prefs.get(SERVERURLPREF, "http://nernst.iii.hogent.be:6800/backend/public");
    }

    public void setServerURL(String serverurl) {
        prefs.put(SERVERURLPREF, serverurl);
    }

    public String getEidAdminUsername() {

        return prefs.get(EIDADMINUSERNAMEPREF, null);
    }

    public String getEidAdminPassword() {

        return prefs.get(EIDADMINPASSWORDPREF, null);
    }

    public void setEidAdminUsername(String username) {
        prefs.put(EIDADMINUSERNAMEPREF, username);
    }

    public void setEidAdminPassword(String password) {
        prefs.put(EIDADMINPASSWORDPREF, password);
    }

    public Locale getLocale() {

        String local = prefs.get(LOCALEPREF, supportedLocales[0].getLanguage());

        for (Locale l : supportedLocales) {
            if (l.getLanguage().equals(local)) {
                return l;
            }
        }
        return supportedLocales[0];

    }

    public static Locale[] getSupportedLocales() {
        return supportedLocales;
    }

    public void setLocale(Locale l) {

        prefs.put(LOCALEPREF, l.getLanguage());
    }

    public boolean getSavePasswords() {
        return prefs.getBoolean(SAVEPASSWORDPREF, false);
    }

    public void setSavePasswords(boolean savepasswords) {
        prefs.putBoolean(SAVEPASSWORDPREF, savepasswords);
    }

    public boolean getSaveUsernames() {
        return prefs.getBoolean(SAVEUSERNAMESPREF, true);
    }

    public void setSaveUsernames(boolean saveusernames) {
        prefs.putBoolean(SAVEUSERNAMESPREF, saveusernames);
    }

    public void setPassword(String username, String password) {
        Preferences passprefs = prefs.node(PASSWORDSNODE);

        passprefs.put(username, password);
    }

    public String getPassword(String username) {
        Preferences passprefs = prefs.node(PASSWORDSNODE);
        return passprefs.get(username, "");
    }

    public void removePassword(String username) {
        Preferences passprefs = prefs.node(PASSWORDSNODE);
        passprefs.remove(username);

    }

    public Set<String> getSavedUsernames() {
        Set<String> usernames = new HashSet<String>();
        String result = prefs.get(USERNAMESPREF, "");

        String[] names = result.split("\n");

        for (String username : names) {
            usernames.add(username);
        }

        return usernames;
    }

    public void setSavedUsernames(Set<String> usernames) {

        StringBuilder builder = new StringBuilder();
        for (String username : usernames) {
            builder.append(username);
            builder.append("\n");

        }

        if (builder.length() > 0) {
            builder.delete(builder.length() - 1, builder.length());
        }

        prefs.put(USERNAMESPREF, builder.toString());


    }

    public boolean getLoginWithEid() {
        return prefs.getBoolean(LOGINWITHEIDPREF, false);
    }

    public void setLoginWithEid(boolean loginwitheid) {
        prefs.putBoolean(LOGINWITHEIDPREF, loginwitheid);
    }
}
