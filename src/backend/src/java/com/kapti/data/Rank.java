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
import com.kapti.exceptions.StockPlayException;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * \brief   Object die de rang voorstelt van een speler in de puntenranking
 *
 * Deze klasse dient om de rang van een speler voor te stellen in de
 * puntenrangschikking. Dit object dient enkel om gegevens naar buiten
 * te brengen en niet om eventuele wijzigingen te doen in de databank.
 */
public class Rank implements Serializable {
    //
    // Member data
    //

    public static enum Fields {
        ID, TOTAL, RANK
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.ID, String.class);
            put(Fields.TOTAL, Date.class);
            put(Fields.RANK, Double.class);
    } };

    private int id;
    private int total = 0;
    private int rank = 0;


    //
    // Construction
    //

    public Rank(int id, int total, int rank){
        this.id = id;
        this.total = total;
        this.rank = rank;
    }


    //
    // Methods
    //

    public int getId() {
        return id;
    }

    public int getTotal() {
        return total;
    }

    public int getRank() {
        return rank;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }


    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case TOTAL:
                    oStruct.put(tField.name(), getTotal());
                    break;

                case RANK:
                    oStruct.put(tField.name(), getRank());
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
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());

            switch (tField) {
                case ID:
                    setId((Integer)tValue);
                    break;
                case TOTAL:
                    setTotal((Integer)tValue);
                    break;
                case RANK:
                    setRank((Integer)tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }
}