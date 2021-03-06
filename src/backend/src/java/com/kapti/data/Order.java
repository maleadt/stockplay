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
 * \brief   Basisobject voor order-gerelateerde data
 *
 * Deze klasse wordt gebruikt om alle order-gerelateerde data te verpakken
 * in een object dat dan verder intern gebruikt wordt binnen de backend. Het
 * biedt ook de nodige functionaliteit om zichzelf terug te converteren naar
 * een object dat over XML-RPC verstuurd kan worden, of om net zichzelf te
 * construeren of aan te passen aan de hand van dergelijke data.
 */
public class Order extends Instruction implements Serializable {

    //
    // Member data
    //

    public static enum Fields {
        ID, USER, ISIN, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        STATUS, CREATIONTIME, EXPIRATIONTIME, EXECUTIONTIME, PARAMETERS, SECONDAIRYLIMIT
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.ID, Integer.class);
            put(Fields.USER, Integer.class);
            put(Fields.ISIN, String.class);
            put(Fields.AMOUNT, Integer.class);
            put(Fields.PRICE, Double.class);
            put(Fields.SECONDAIRYLIMIT, Double.class);
            put(Fields.TYPE, String.class); // Wordt ingelezen via InstructionType.valueOf
            put(Fields.STATUS, String.class); // Wordt ingelezen via OrderStatus.valueOf
            put(Fields.CREATIONTIME, Date.class);
            put(Fields.EXPIRATIONTIME, Date.class);
            put(Fields.EXECUTIONTIME, Date.class);
            put(Fields.PARAMETERS, String.class);
    } };
    
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
                case SECONDAIRYLIMIT:
                    oStruct.put(tField.name(), getSecondairyLimit());
                    break;
                case TYPE:
                    if (getType() != null)
                        oStruct.put(tField.name(), getType().name());
                    break;

                case STATUS:
                    if (getStatus() != null)
                        oStruct.put(tField.name(), getStatus().name());
                    break;
                case CREATIONTIME:
                    if (getCreationTime() != null)
                        oStruct.put(tField.name(), getCreationTime());
                    break;
                case EXPIRATIONTIME:
                    if (getExpirationTime() != null)
                        oStruct.put(tField.name(), getExpirationTime());
                    break;
                case EXECUTIONTIME:
                    if (getExecutionTime() != null)
                        oStruct.put(tField.name(), getExecutionTime());
                    break;
                case PARAMETERS:
                    if (getParameters() != null)
                        oStruct.put(tField.name(), getParameters());
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
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                case PRICE:
                    setPrice((Double)tValue);
                    break;
                case SECONDAIRYLIMIT:
                    setSecondairyLimit((Double)tValue);
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

    public static Order fromStruct(HashMap<String, Object> iStruct) throws StockPlayException {
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