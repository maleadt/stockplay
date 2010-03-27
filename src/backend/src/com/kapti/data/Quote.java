/*
 * Quote.java
 * StockPlay - Prijsklasse
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

package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Date;
import java.util.Hashtable;

public class Quote {
    //
    // Member data
    //

    public static enum Fields {
        ISIN, TIME, PRICE, VOLUME, BID, ASK, LOW, HIGH, OPEN
    }

    private QuotePK pk = null;
    private double price = 0.0;
    private int volume = 0;
    private double bid = 0, ask = 0;
    private double low = 0, high = 0;
    private double open = 0;


    //
    // Construction
    //

    public Quote(String isin, Date time){
        this.pk = new QuotePK(isin, time);
    }


    //
    // Methods
    //

    public String getIsin() {
        return pk.getIsin();
    }

    public double getBid() {
        return bid;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getPrice() {
        return price;
    }

    public double getAsk() {
        return ask;
    }

    public Date getTime() {
        return pk.getTime();
    }

    public int getVolume() {
        return volume;
    }

    public void setBid(double buy) {
        this.bid = buy;
    }

    public void setHigh(double high) {
        this.high = high;
    }

    public void setLow(double low) {
        this.low = low;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setAsk(double sell) {
        this.ask = sell;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public QuotePK getPk() {
        return pk;
    }

    public double getOpen() {
        return open;
    }

    public void setOpen(double open) {
        this.open = open;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ISIN:
                    oStruct.put(tField.name(), getIsin());
                    break;
                case TIME:
                    oStruct.put(tField.name(), getTime());
                    break;
                case PRICE:
                    oStruct.put(tField.name(), getPrice());
                    break;
                case VOLUME:
                    oStruct.put(tField.name(), getVolume());
                    break;
                case BID:
                    oStruct.put(tField.name(), getBid());
                    break;
                case ASK:
                    oStruct.put(tField.name(), getAsk());
                    break;
                case LOW:
                    oStruct.put(tField.name(), getLow());
                    break;
                case HIGH:
                    oStruct.put(tField.name(), getHigh());
                    break;
                case OPEN:
                    oStruct.put(tField.name(), getOpen());
                    break;
            }
        }
        return oStruct;
    }

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case PRICE:
                    setPrice((Double)tValue);
                    break;
                case VOLUME:
                    setVolume((Integer)tValue);
                    break;
                case BID:
                    setBid((Double)tValue);
                    break;
                case ASK:
                    setAsk((Double)tValue);
                    break;
                case LOW:
                    setLow((Double)tValue);
                    break;
                case HIGH:
                    setHigh((Double)tValue);
                    break;
                case OPEN:
                    setOpen((Double)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Quote fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey);
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.TIME) && tStructMap.containsKey(Fields.ISIN)) {
            Quote tQuote = new Quote((String)iStruct.get(tStructMap.get(Fields.ISIN)), (Date)iStruct.get(tStructMap.get(Fields.TIME)));
            iStruct.remove(tStructMap.get(Fields.TIME));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            return tQuote;
        } else {
            throw new StockPlayException("not enough information to instantiate object");
        }
    }


    //
    // Subclasses
    //

    public class QuotePK {

        private String isin = null;
        private Date time = null;

        public QuotePK() {
        }

        public QuotePK(String isin, Date time){
            this.isin = isin;
            this.time = time;
        }

        public String getIsin() {
            return isin;
        }

        public Date getTime() {
            return time;
        }
    }
    
}