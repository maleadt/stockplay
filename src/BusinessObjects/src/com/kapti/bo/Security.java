package com.kapti.bo;

import com.kapti.bo.interfaces.ISecurity;
import java.util.Date;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Thijs
 */
public class Security implements ISecurity {
    private String symbol = "";
    private String name = "";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public SharePrice getSharePrice(){
        return null;
    }

    /**
     * Haalt beurskoers op van effect die het dichtst bij de opgegeven datum zit
     * @param time
     * @return
     */
    public SharePrice getSharePrice(Date time){
        return null;
    }
}
