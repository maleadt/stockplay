/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.bo;

import com.kapti.bo.interfaces.IExchange;

/**
 *
 * @author Thijs
 */
public class Exchange implements IExchange {
    private String symbol;
    private String name ="";
    private String location = "";

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    
}
