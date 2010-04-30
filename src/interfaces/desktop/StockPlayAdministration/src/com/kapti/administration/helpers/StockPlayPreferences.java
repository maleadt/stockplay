/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.helpers;

import java.util.Locale;
import java.util.prefs.Preferences;

/**
 *
 * @author Thijs
 */
public class StockPlayPreferences {

    Preferences prefs = Preferences.userRoot();
    private static final String LOCALEPREF = "locale";


    private static Locale[] supportedLocales = {new Locale("nl", "be"), Locale.ENGLISH};

    public Locale getLocale() {

        String local = prefs.get(LOCALEPREF, supportedLocales[0].getLanguage());

        for(Locale l : supportedLocales){
            if(l.getLanguage().equals(local))
                return l;
        }
        return supportedLocales[0];

    }

    public static Locale[] getSupportedLocales() {
        return supportedLocales;
    }

    

    public void setLocale(Locale l) {

        prefs.put(LOCALEPREF, l.getLanguage());
    }
}
