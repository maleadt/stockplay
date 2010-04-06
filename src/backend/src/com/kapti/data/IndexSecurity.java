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

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class IndexSecurity {
    //
    // Member data
    //

    public static enum Fields {
        ISIN, INDEX
    }
    
    private int index;
    private String isin;


    //
    // Construction
    //

    public IndexSecurity(int index, String isin) {
        this.index = index;
        this.isin = isin;
    }


    //
    // Methods
    //

    public int getIndex() {
        return index;
    }

    public String getIsin() {
        return isin;
    }

    public void setIndex(int iIndex) {
        index = iIndex;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ISIN:
                    oStruct.put(tField.name(), getIsin());
                    break;
                case INDEX:
                    oStruct.put(tField.name(), getIndex());
                    break;
            }
        }
        return oStruct;
    }

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "No keys can be modified");
    }

    public static IndexSecurity fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
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
        if (tStructMap.containsKey(Fields.ISIN) && tStructMap.containsKey(Fields.INDEX)) {
            IndexSecurity tIndexSecurity = new IndexSecurity((Integer)iStruct.get(tStructMap.get(Fields.INDEX)), (String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            iStruct.remove(tStructMap.get(Fields.INDEX));
            return tIndexSecurity;
        } else
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
    }
}