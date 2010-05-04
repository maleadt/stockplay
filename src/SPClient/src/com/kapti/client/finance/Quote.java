/*
 * Quote.java
 * StockPlay - Quote object wat we verder kunnen gebruiken.
 *
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kapti.client.finance;

import com.kapti.exceptions.StockPlayException;
import java.util.Date;
import java.util.HashMap;

/**
 *
 * \brief   Quote object wat we verder kunnen gebruiken.
 *
 */

public class Quote {

    public static enum Fields {

        ISIN, TIME, PRICE, VOLUME, BID, ASK, LOW, HIGH, OPEN
    }

    Quote(Security security, Date time, double price, int volume, double bid, double ask, double low, double high, double open) {
        this.security = security;
        this.time = time;
        this.price = price;
        this.volume = volume;
        this.bid = bid;
        this.ask = ask;
        this.low = low;
        this.high = high;
        this.open = open;
    }
    protected Security security;

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
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

    public static Quote fromStruct(HashMap h) throws StockPlayException {
        try {
            return new Quote(FinanceFactory.getInstance().getSecurityById((String) h.get(Fields.ISIN.toString())),
                    (Date) h.get(Fields.TIME.toString()),
                    (Double) h.get(Fields.PRICE.toString()),
                    (Integer) h.get(Fields.VOLUME.toString()),
                    (Double) h.get(Fields.BID.toString()),
                    (Double) h.get(Fields.ASK.toString()),
                    (Double) h.get(Fields.LOW.toString()),
                    (Double) h.get(Fields.HIGH.toString()),
                    (Double) h.get(Fields.OPEN.toString()));
        } catch (Exception ex) {
            throw new StockPlayException("Error while parsing Quote-struct to quote-object");
        }
    }
}
