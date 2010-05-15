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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


/**
 * \brief   Basisobject voor transactie-gerelateerde data, met name de puntenberekening
 *
 * Deze klasse wordt gebruikt om data over de punten van een transactie te verpakken
 * in een object dat dan verder intern gebruikt wordt binnen de backend. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class PointsTransaction implements Serializable {
    //
    // Member data
    //

    public static enum Fields {
        USER, TIMESTAMP, TYPE, DELTA, COMMENTS
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.USER, Integer.class);        // Deel van de
            put(Fields.TIMESTAMP, Date.class);      // PointsTransactionPK
            put(Fields.TYPE, String.class);
            put(Fields.DELTA, Integer.class);
            put(Fields.COMMENTS, String.class);
    } };

    private PointsType type;
    private int delta;
    private String comments;
    private PointsTransactionPK pk;


    //
    // Construction
    //

    public PointsTransaction(int user, PointsType type, Date timestamp) {
        this.pk = new PointsTransactionPK(user, type, timestamp);
    }

    public PointsType getType() {
        return type;
    }

    public void setType(PointsType type) {
        this.type = type;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getDelta() {
        return delta;
    }

    public void setDelta(int delta) {
        this.delta = delta;
    }

    public Date getTimestamp() {
        return pk.getTimestamp();
    }

    public int getUser() {
        return pk.getUser();
    }

    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case USER:
                    oStruct.put(tField.name(), getUser());
                    break;
                case TIMESTAMP:
                    oStruct.put(tField.name(), getTimestamp());
                    break;
                case TYPE:
                    oStruct.put(tField.name(), getType().name());
                    break;
                case DELTA:
                    oStruct.put(tField.name(), getDelta());
                    break;
                case COMMENTS:
                    oStruct.put(tField.name(), getComments());
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
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());

            switch (tField) {
                case TYPE:
                    setType(PointsType.valueOf((String)tValue));
                    break;
                case DELTA:
                    setDelta((Integer) tValue);
                    break;
                case COMMENTS:
                    setComments((String) tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static PointsTransaction fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        HashMap<Fields, String> tStructMap = new HashMap<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER) && tStructMap.containsKey(Fields.TIMESTAMP) && tStructMap.containsKey(Fields.TYPE)) {
            PointsTransaction tTransaction = new PointsTransaction((Integer) iStruct.get(tStructMap.get(Fields.USER)), PointsType.valueOf((String)iStruct.get(tStructMap.get(Fields.TYPE))), (Date) iStruct.get(tStructMap.get(Fields.TIMESTAMP)));
            iStruct.remove(tStructMap.get(Fields.USER));
            iStruct.remove(tStructMap.get(Fields.TIMESTAMP));
            return tTransaction;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }
    
    
    //
    // Classes
    //

    public class PointsTransactionPK implements Serializable {

        private PointsType type;
        private int user;
        private Date timestamp;

        public PointsTransactionPK(int user, PointsType type, Date timestamp) {
            this.user = user;
            this.timestamp = timestamp;
        }

        public PointsType getType() {
            return type;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public int getUser() {
            return user;
        }
    }
}
