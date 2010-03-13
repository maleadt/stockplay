package com.kapti.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class Order extends Instruction {
    //
    // Member data
    //

    public static enum Fields {
        ID, USER, SECURITY, AMOUNT, PRICE, TYPE,    // Instruction.Fields
        STATUS, CREATIONTIME, EXPIRATIONTIME, EXECUTIONTIME
    }
    
    private OrderStatus status;
    private Date creationTime;
    private Date expirationTime;
    private Date executionTime;


    //
    // Construction
    //


    public Order(int id){
        super(id);
    }

    public Order(int id, int user, String security, int amount, double price, InstructionType type, OrderStatus status, Date creationTime, Date expirationTime, Date executionTime) {
        super(id, user, security, amount, price, type);
        this.status =status;
        this.creationTime = creationTime;
        this.expirationTime = expirationTime;
        this.executionTime = executionTime;
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

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                // Instruction.Fields
                case ID:
                    oStruct.put("id", getId());
                    break;
                case USER:
                    oStruct.put("user", getUser());
                    break;
                case SECURITY:
                    oStruct.put("security", getSecurity());
                    break;
                case AMOUNT:
                    oStruct.put("amount", getAmount());
                    break;
                case PRICE:
                    oStruct.put("price", getPrice());
                    break;
                case TYPE:
                    oStruct.put("type", getType());
                    break;

                case STATUS:
                    oStruct.put("status", getStatus());
                    break;
                case CREATIONTIME:
                    oStruct.put("creationtime", getCreationTime());
                    break;
                case EXPIRATIONTIME:
                    oStruct.put("expirationtime", getExpirationTime());
                    break;
                case EXECUTIONTIME:
                    oStruct.put("executiontime", getExecutionTime());
                    break;
            }
        }
        return oStruct;
    }

}