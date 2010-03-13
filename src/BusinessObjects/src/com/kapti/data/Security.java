package com.kapti.data;

import java.util.Hashtable;

public class Security  {
    //
    // Member data
    //

    public static enum Fields {
        ID, NAME, EXCHANGE
    }
    
    private String symbol = "";
    private String name = "";

    private String exchange = "";


    //
    // Construction
    //

    public Security(){
    }

    public Security(String symbol){
        this.symbol = symbol;
    }

    public Security(String symbol, String name, String exchange){
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }


    //
    // Methods
    //

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    /**
     * Haalt beurskoers op van effect die het dichtst bij de opgegeven datum zit
     * @param time
     * @return
     */
    public String getExchange() {
        return exchange;
    }

    public void setExchange(String exchange) {
        this.exchange = exchange;
    }

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put("id", getSymbol());
                    break;
                case NAME:
                    oStruct.put("name", getName());
                    break;
                case EXCHANGE:
                    oStruct.put("location", getExchange());
                    break;
            }
        }
        return oStruct;
    }
}