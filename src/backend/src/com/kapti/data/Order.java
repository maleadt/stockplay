/*
 * Order.java
 * StockPlay - Orderklasse
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

public class Order extends Instruction {

    //
    // Member data
    //

    public static enum Fields {
        ID, USER, ISIN, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        STATUS, CREATIONTIME, EXPIRATIONTIME, EXECUTIONTIME, PARAMETERS
    }
    
    private OrderStatus status;
    private Date creationTime;
    private Date expirationTime;
    private Date executionTime;
    private String parameters;

    //
    // Construction
    //

    public Order(int user, String isin) {
        super(user, isin);
    }

    public Order(int id, int user, String isin) {
        super(id, user, isin);
    }

    //
    // Methods
    //

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public Date getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Date executionTime) {
        this.executionTime = executionTime;
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public String getParameters() {
        return parameters;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
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

                case STATUS:
                    oStruct.put(tField.name(), getStatus().name());
                    break;
                case CREATIONTIME:
                    oStruct.put(tField.name(), getCreationTime());
                    break;
                case EXPIRATIONTIME:
                    oStruct.put(tField.name(), getExpirationTime());
                    break;
                case EXECUTIONTIME:
                    oStruct.put(tField.name(), getExecutionTime());
                    break;
                case PARAMETERS:
                    oStruct.put(tField.name(), getParameters());
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
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                case PRICE:
                    setPrice((Double)tValue);
                    break;
                case TYPE:
                    setType(InstructionType.valueOf((String)tValue));
                    break;

                case STATUS:
                    setStatus(OrderStatus.valueOf((String)tValue));
                    break;
                case CREATIONTIME:
                    setCreationTime((Date)tValue);
                    break;
                case EXPIRATIONTIME:
                    setExpirationTime((Date)tValue);
                    break;
                case EXECUTIONTIME:
                    setExecutionTime((Date)tValue);
                    break;
                case PARAMETERS:
                    setParameters((String)tValue);
                    break;
                    
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY, "requested key '" + tKey + "' cannot be modified");
            }
        }
    }

    public static Order fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey);
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER) && tStructMap.containsKey(Fields.ISIN)) {
            Order tOrder = new Order((Integer)iStruct.get(tStructMap.get(Fields.USER)), (String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.USER));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            return tOrder;
        } else {
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
        }
    }
}