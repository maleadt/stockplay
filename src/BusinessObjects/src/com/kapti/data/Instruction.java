package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class Instruction {
    //
    // Member data
    //

    public static enum Fields {
        ID, USER, SECURITY, AMOUNT, PRICE, TYPE
    }
    
    private int id = -1;
    private int user;
    private String security;
    private int amount;
    private double price;
    private InstructionType type;


    //
    // Construction
    //

    public Instruction() {
    }

    public Instruction(int id) {
        this.id = id;
    }

    public Instruction(int id, int user, String security, int amount, double price, InstructionType type) {
        this.id = id;
        this.user = user;
        this.security = security;
        this.amount = amount;
        this.price = price;
        this.type = type;
    }

    public Instruction (int user, String security, int amount, InstructionType type) {
        this.user = user;
        this.security = security;
        this.amount = amount;
        this.type = type;
    }

    //
    // Methods
    //

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public InstructionType getType() {
        return type;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setType(InstructionType type) {
        this.type = type;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }

    public String getSecurity() {
        return security;
    }

    public void setSecurity(String security) {
        this.security = security;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case USER:
                    oStruct.put(tField.name(), getUser());
                    break;
                case SECURITY:
                    oStruct.put(tField.name(), getSecurity());
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
                case USER:
                    setUser((Integer)tValue);
                    break;
                case SECURITY:
                    setSecurity((String)tValue);
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
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
}