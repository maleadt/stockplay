/*
 * Index.java
 * StockPlay - Indexklasse
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

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class Index {
    //
    // Member data
    //

    public static enum Fields {
        ID, NAME, EXCHANGE
    }
    
    private int id;
    private String name;
    private String exchange;


    //
    // Construction
    //

    public Index(String name, String exchange) {
        this.name = name;
        this.exchange = exchange;
    }

    public Index(int id, String name, String exchange) {
        this(name, exchange);
        this.id = id;
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

    public int getId() {
        return id;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
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

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                default:
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Index fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.NAME) && tStructMap.containsKey(Fields.EXCHANGE)) {
            Index tIndex = new Index(
                    (String)iStruct.get(tStructMap.get(Fields.NAME)),
                    (String)iStruct.get(tStructMap.get(Fields.EXCHANGE))
                );
            iStruct.remove(tStructMap.get(Fields.NAME));
            iStruct.remove(tStructMap.get(Fields.EXCHANGE));
            return tIndex;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }

}