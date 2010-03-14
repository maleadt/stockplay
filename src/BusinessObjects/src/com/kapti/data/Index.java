package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
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

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getId());
                    break;
                case NAME:
                    oStruct.put(tField.name(), getName());
                    break;
                case EXCHANGE:
                    oStruct.put(tField.name(), getExchange());
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
                case NAME:
                    setName((String)tValue);
                    break;
                case EXCHANGE:
                    setExchange((String)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }

}