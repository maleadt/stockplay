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
 * \brief   Basisobject voor transactie-gerelateerde data
 *
 * Deze klasse wordt gebruikt om alle transactie-gerelateerde data te verpakken
 * in een object dat dan verder intern gebruikt wordt binnen de backend. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class Transaction extends Instruction implements Serializable {
    //
    // Member data
    //

    public static enum Fields {
        ID, USER, ISIN, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        TIME, COMMENTS
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.ID, Integer.class);
            put(Fields.USER, Integer.class);
            put(Fields.ISIN, String.class);
            put(Fields.AMOUNT, Integer.class);
            put(Fields.PRICE, Double.class);
            put(Fields.TYPE, String.class); // Wordt ingelezen via InstructionType.valueOf

            put(Fields.TIME, Date.class);
            put(Fields.COMMENTS, String.class);
    } };
    
    private Date time;
    private String comments;

    public Transaction(int user) {
        super(user);
    }


    //
    // Construction
    //    

    public Transaction(int user, String isin) {
        super(user, isin);
    }

    public Transaction(int id, int user, String isin) {
        super(id, user, isin);
    }


    //
    // Methods
    //

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    

    public HashMap<String, Object> toStruct(Fields... iFields) {
        HashMap<String, Object> oStruct = new HashMap<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case USER:
                    oStruct.put(tField.name(), getUser());
                    break;
                case ISIN:
                    oStruct.put(tField.name(), getIsin());
                    break;
                case AMOUNT:
                    oStruct.put(tField.name(), getAmount());
                    break;
                case PRICE:
                    oStruct.put(tField.name(), getPrice());
                    break;
                case TYPE:
                    if (getType() != null)
                        oStruct.put(tField.name(), getType().toString());
                    break;

                case TIME:
                    if (getTime() != null)
                        oStruct.put(tField.name(), getTime());
                    break;
                case COMMENTS:
                    if(getComments() != null)
                        oStruct.put(tField.name(), getComments());
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
                case ISIN:
                    setIsin((String)tValue);
                    break;
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                case PRICE:
                    setPrice((Double)tValue);
                    break;
                case TYPE:
                    setType(InstructionType.valueOf((String)tValue));
                    break;

                case TIME:
                    setTime((Date)tValue);
                    break;
                case COMMENTS:
                    setComments((String) tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Transaction fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        HashMap<Fields, String> tStructMap = new HashMap<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER)) {// && tStructMap.containsKey(Fields.ISIN)) {
            Transaction tTransaction = new Transaction((Integer)iStruct.get(tStructMap.get(Fields.USER)));//, (String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.USER));
            //iStruct.remove(tStructMap.get(Fields.ISIN));
            return tTransaction;
        } else
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
    }
   
}