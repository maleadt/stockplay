/*
 * Transaction.java
 * StockPlay - Transactieklasse
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
import java.util.Date;
import java.util.Hashtable;

public class Transaction extends Instruction {
    //
    // Member data
    //

    public static enum Fields {
        ID, USER, ISIN, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        TIME
    }
    
    private Date time;


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

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
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
                    oStruct.put(tField.name(), getType());
                    break;

                case TIME:
                    oStruct.put(tField.name(), getTime());
                    break;
            }
        }
        return oStruct;
    }
    
    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey.toUpperCase());
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
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

                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Transaction fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            tStructMap.put(tField, tKey.toUpperCase());
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER) && tStructMap.containsKey(Fields.ISIN)) {
            Transaction tTransaction = new Transaction((Integer)iStruct.get(tStructMap.get(Fields.USER)), (String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.USER));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            return tTransaction;
        } else
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
    }
   
}