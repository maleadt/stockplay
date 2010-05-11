/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.finance;

import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class Exchange {

    public static final String SYMBOLFIELD = "SYMBOL";
    public static final String NAMEFIELD = "NAME";
    public static final String LOCATIONFIELD = "LOCATION";
    
    /**
     * Deze variabele geeft aan of de waarden van het object werden gewijzigd
     */
    Exchange(String symbol, String name, String location) {
        this.symbol = symbol;
        this.name = name;
        this.location = location;
    }

    protected String symbol;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getSymbol() {
        return symbol;
    }
    protected String name;

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    protected String location;

    /**
     * Get the value of location
     *
     * @return the value of location
     */
    public String getLocation() {
        return location;
    }


    public static Exchange fromStruct(Hashtable h) {
        return new Exchange((String) h.get(Exchange.SYMBOLFIELD),
                (String) h.get(Exchange.NAMEFIELD),
                (String) h.get(Exchange.LOCATIONFIELD));
    }

}
