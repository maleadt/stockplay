/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data;

/**
 *
 * @author Thijs
 */


public class IndexSecurity {

    private int index;
    private String symbol;

    public IndexSecurity(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }

    public int getIndex() {
        return index;
    }

    public String getSymbol() {
        return symbol;
    }

    

}
