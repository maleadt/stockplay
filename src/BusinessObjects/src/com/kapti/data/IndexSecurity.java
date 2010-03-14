package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

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

    public void setIndex(int iIndex) {
        index = iIndex;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getSymbol());
                    break;
                case INDEX:
                    oStruct.put(tField.name(), getIndex());
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
                case INDEX:
                    setIndex((Integer)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
}