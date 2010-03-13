package com.kapti.data;

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

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
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
            }
        }
        return oStruct;
    }
}