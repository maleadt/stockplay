/*
 * Security.java
 * StockPlay - Effectenklasse
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
import java.util.Hashtable;

public class Security {
    //
    // Member data
    //

    public static enum Fields {

        ISIN, SYMBOL, NAME, EXCHANGE, VISIBLE, SUSPENDED
    }
    private String isin = "";
    private String symbol = "";
    private String name = "";
    private String exchange = "";
    private boolean visible = true;
    private boolean suspended = false;
    

    //
    // Construction
    //

    public Security(String isin) {
        this.isin = isin;
    }


    public Security(String isin, String symbol, String name, String exchange) {
        this.isin = isin;
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

    public Security(String isin, String symbol, String name, String exchange, boolean visible, boolean suspended) {
        this.isin = isin;
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.visible = visible;
        this.suspended = suspended;
    }


    //
    // Methods
    //
    public String getIsin() {
        return isin;
    }

    public void setIsin(String isin) {
        this.isin = isin;
    }

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

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ISIN:
                    oStruct.put(tField.name(), getIsin());
                    break;
                case SYMBOL:
                    oStruct.put(tField.name(), getSymbol());
                    break;
                case NAME:
                    oStruct.put(tField.name(), getName());
                    break;
                case EXCHANGE:
                    oStruct.put(tField.name(), getExchange());
                    break;
                case VISIBLE:
                    oStruct.put(tField.name(), isVisible());
                    break;
                case SUSPENDED:
                    oStruct.put(tField.name(), isSuspended());
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
            } catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case ISIN:
                    setIsin((String) tValue);
                    break;
                case SYMBOL:
                    setSymbol((String) tValue);
                    break;
                case NAME:
                    setName((String) tValue);
                    break;
                case EXCHANGE:
                    setExchange((String) tValue);
                    break;
                case VISIBLE:
                    setVisible((Boolean) tValue);
                    break;
                case SUSPENDED:
                    setSuspended((Boolean) tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Security fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
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
        if (tStructMap.containsKey(Fields.ISIN)) {
            Security tSecurity = new Security((String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            return tSecurity;
        } else {
            throw new StockPlayException("not enough information to instantiate object");
        }
    }
}