package com.kapti.data;

import java.util.Hashtable;

public class Index {
    //
    // Member data
    //

    public static enum Fields {
        ID, NAME, EXCHANGE
    }
    
    private int id;
    private String name;
    private String exchange;


    //
    // Construction
    //

    public Index(int id){
        this.id = id;
    }

    public Index(int id, String name, String exchange) {
        this.id = id;
        this.name = name;
        this.exchange = exchange;
    }


    //
    // Methods
    //

    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put("id", getId());
                    break;
                case NAME:
                    oStruct.put("name", getName());
                    break;
                case EXCHANGE:
                    oStruct.put("exchange", getExchange());
                    break;
            }
        }
        return oStruct;
    }

}