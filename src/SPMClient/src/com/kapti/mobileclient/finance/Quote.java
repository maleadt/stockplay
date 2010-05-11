/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.finance;

import com.kapti.mobileclient.exceptions.StockPlayException;
import java.util.Date;
import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class Quote {

    public static final String ISINFIELD = "ISIN";
    public static final String TIMEFIELD = "TIME";
    public static final String PRICEFIELD = "PRICE";
    public static final String VOLUMEFIELD = "VOLUME";
    public static final String BIDFIELD = "BID";
    public static final String ASKFIELD = "ASK";
    public static final String LOWFIELD = "LOW";
    public static final String HIGHFIELD = "HIGH";
    public static final String OPENFIELD = "OPEN";

    Quote(String isin, Date time, double price, int volume, double bid, double ask, double low, double high, double open) {
        this.isin = isin;
        this.time = time;
        this.price = price;
        this.volume = volume;
        this.bid = bid;
        this.ask = ask;
        this.low = low;
        this.high = high;
        this.open = open;
    }

    protected String isin;
    protected Security security;

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
        if (security == null && isin != null) {
            try {
                security = FinanceFactory.getInstance().getSecurityById(isin);
            } catch (StockPlayException ex) {
                ex.printStackTrace();
            }
        }
        return security;
    }
    protected Date time;

    /**
     * Get the value of time
     *
     * @return the value of time
     */
    public Date getTime() {
        return time;
    }
    protected double price;

    /**
     * Get the value of price
     *
     * @return the value of price
     */
    public double getPrice() {
        return price;
    }
    protected int volume;

    /**
     * Get the value of volume
     *
     * @return the value of volume
     */
    public int getVolume() {
        return volume;
    }
    protected double bid;

    /**
     * Get the value of bid
     *
     * @return the value of bid
     */
    public double getBid() {
        return bid;
    }
    protected double ask;

    /**
     * Get the value of ask
     *
     * @return the value of ask
     */
    public double getAsk() {
        return ask;
    }
    protected double low;

    /**
     * Get the value of low
     *
     * @return the value of low
     */
    public double getLow() {
        return low;
    }
    protected double high;

    /**
     * Get the value of high
     *
     * @return the value of high
     */
    public double getHigh() {
        return high;
    }
    protected double open;

    /**
     * Get the value of open
     *
     * @return the value of open
     */
    public double getOpen() {
        return open;
    }

    public static Quote fromStruct(Hashtable h) throws StockPlayException {
        try {
            return new Quote((String) h.get(ISINFIELD),
                    (Date) h.get(TIMEFIELD),
                    Double.parseDouble((String) h.get(PRICEFIELD)),
                    ((Integer) h.get(VOLUMEFIELD)).intValue(),
                    Double.parseDouble((String) h.get(BIDFIELD)),
                    Double.parseDouble((String) h.get(ASKFIELD)),
                    Double.parseDouble((String)  h.get(LOWFIELD)),
                    Double.parseDouble((String) h.get(HIGHFIELD)),
                    Double.parseDouble((String)  h.get(OPENFIELD)));
        } catch (Exception ex) {
            throw new StockPlayException("Error while parsing Quote-struct to quote-object");
        }
    }
}
