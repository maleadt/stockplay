/*
 * IndexSecurity.java
 * StockPlay - Klasse die een index met een effect koppelt
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

public class IndexSecurity {
    //
    // Member data
    //

    public static enum Fields {
        ID, INDEX
    }
    
    private int index;
    private String symbol;


    //
    // Construction
    //

    public IndexSecurity(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }


    //
    // Methods
    //

    public int getIndex() {
        return index;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setIndex(int iIndex) {
        index = iIndex;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getSymbol());
                    break;
                case INDEX:
                    oStruct.put(tField.name(), getIndex());
                    break;
            }
        }
        return oStruct;
    }

    public void fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey);
            }
            catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case INDEX:
                    setIndex((Integer)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
}