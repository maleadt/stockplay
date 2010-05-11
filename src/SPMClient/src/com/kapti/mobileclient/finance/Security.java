/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.finance;

import com.kapti.mobileclient.exceptions.StockPlayException;
import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class Security {

    public static final String ISINFIELD = "ISIN";
    public static final String SYMBOLFIELD = "SYMBOL";
    public static final String NAMEFIELD = "NAME";
    public static final String EXCHANGEFIELD = "EXCHANGE";
    public static final String VISIBLEFIELD = "VISIBLE";
    public static final String SUSPENDEDFIELD = "SUSPENDED";

    public Security(String isin, String symbol, String exchange, String name, boolean visible, boolean suspended) {
        this.ISIN = isin;
        this.symbol = symbol;
        this.exchange = exchange;
        this.name = name;
        this.visible = visible;
        this.suspended = suspended;
    }

    public Security(String isin, String symbol, String exchange) {
        this.ISIN = isin;
        this.symbol = symbol;
        this.exchange = exchange;
    }

    protected String ISIN;

    /**
     * Get the value of ISIN
     *
     * @return the value of ISIN
     */
    public String getISIN() {
        return ISIN;
    }
    protected String symbol;

    /**
     * Get the value of symbol
     *
     * @return the value of symbol
     */
    public String getSymbol() {
        return symbol;
    }
    protected String exchange;

    /**
     * Get the value of exchange
     *
     * @return the value of exchange
     */
    public Exchange getExchange() {
        try {
            return FinanceFactory.getInstance().getExchange(exchange);
        } catch (StockPlayException ex) {
            return null;
        }
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

   
    protected boolean visible;

    /**
     * Get the value of visible
     *
     * @return the value of visible
     */
    public boolean isVisible() {
        return visible;
    }
    protected boolean suspended;

    /**
     * Get the value of suspended
     *
     * @return the value of suspended
     */
    public boolean isSuspended() {
        return suspended;
    }

    public static class SecurityType {

        public static final String STOCK = "STOCK";
        public static final String FUND = "FUND";
        public static final String TRACKER = "TRACKER";
    }
    protected String type = SecurityType.STOCK;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }


    public static Security fromStruct(Hashtable h) {

        return new Security(
                (String) h.get(ISINFIELD),
                (String) h.get(SYMBOLFIELD),
                (String) h.get(EXCHANGEFIELD),
                (String) h.get(NAMEFIELD),
                ((Boolean) h.get(VISIBLEFIELD)).booleanValue(),
                ((Boolean) h.get(SUSPENDEDFIELD)).booleanValue());
    }
}
