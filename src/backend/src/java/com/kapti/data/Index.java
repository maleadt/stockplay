/*
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

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashMap;
import java.util.Map;

/**
 * \brief   Basisobject voor index-gerelateerde data
 *
 * Deze klasse wordt gebruikt om alle index-gerelateerde data te verpakken
 * in een object dat dan verder intern gebruikt wordt binnen de backend. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class Index implements Serializable {
    //
    // Member data
    //

    public static enum Fields {
        ISIN, NAME, EXCHANGE, SYMBOL
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.ISIN, String.class);
            put(Fields.EXCHANGE, String.class);
            put(Fields.EXCHANGE, String.class);
            put(Fields.SYMBOL, String.class);
    } };
    
    private String name;
    private String exchange;
    private String isin;
    private String symbol;


    //
    // Construction
    //

    public Index(String isin, String symbol, String exchange) {
        this.isin = isin;
        this.symbol = symbol;
        this.exchange = exchange;
    }


    //
    // Methods
    //

    public String getExchange() {
        return exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsin() {
        return isin;
    }

    public String getSymbol() {
        return symbol;
    }

    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
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
            }
        }
        return oStruct;
    }

    public void applyStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());

            switch (tField) {
                case NAME:
                    setName((String)tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Index fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        HashMap<Fields, String> tStructMap = new HashMap<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.ISIN) && tStructMap.containsKey(Fields.SYMBOL) && tStructMap.containsKey(Fields.EXCHANGE)) {
            Index tIndex = new Index(
                    (String)iStruct.get(tStructMap.get(Fields.ISIN)),
                    (String)iStruct.get(tStructMap.get(Fields.SYMBOL)),
                    (String)iStruct.get(tStructMap.get(Fields.EXCHANGE))
                );
            iStruct.remove(tStructMap.get(Fields.ISIN));
            iStruct.remove(tStructMap.get(Fields.SYMBOL));
            iStruct.remove(tStructMap.get(Fields.EXCHANGE));
            return tIndex;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }

}