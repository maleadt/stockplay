package com.kapti.data;

import java.util.Hashtable;

public class IndexSecurity {
    //
    // Member data
    //

    public static enum Fields {
        ID, INDEX
    }
    
    private int index;
    private String symbol;


    //
    // Construction
    //

    public IndexSecurity(int index, String symbol) {
        this.index = index;
        this.symbol = symbol;
    }


    //
    // Methods
    //

    public int getIndex() {
        return index;
    }

    public String getSymbol() {
        return symbol;
    }

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put("id", getSymbol());
                    break;
                case INDEX:
                    oStruct.put("index", getIndex());
                    break;
            }
        }
        return oStruct;
    }
}