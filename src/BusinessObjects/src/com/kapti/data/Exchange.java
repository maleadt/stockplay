package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class Exchange  {
    //
    // Member data
    //

    public static enum Fields {
        ID, NAME, LOCATION
    }

    private String symbol = "";
    private String name ="";
    private String location = "";


    //
    // Construction
    //

    public Exchange(){}

    public Exchange(String symbol) {
        this.symbol = symbol;
    }

    public Exchange(String symbol, String name){
        this.symbol = symbol;
        this.name = name;
    }

    public Exchange(String symbol, String name, String location){
        this.symbol = symbol;
        this.name = name;
        this.location = location;
    }


    //
    // Methods
    //

    public String getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String iSymbol) {
        symbol = iSymbol;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Exchange other = (Exchange) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        return hash;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case ID:
                    oStruct.put(tField.name(), getSymbol());
                    break;
                case NAME:
                    oStruct.put(tField.name(), getName());
                    break;
                case LOCATION:
                    oStruct.put(tField.name(), getLocation());
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
                case ID:
                    setSymbol((String)tValue);
                    break;
                case NAME:
                    setName((String)tValue);
                    break;
                case LOCATION:
                    setLocation((String)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
   
}