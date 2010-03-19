package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class Security {
    //
    // Member data
    //

    public static enum Fields {

        ID, NAME, EXCHANGE, VISIBLE, SUSPENDED
    }
    private String symbol = "";
    private String name = "";
    private String exchange = "";
    private boolean visible = true;
    private boolean suspended = false;

    //
    // Construction
    //
    public Security() {
    }

    public Security(String symbol) {
        this.symbol = symbol;
    }

    public Security(String symbol, String name, String exchange) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

    public Security(String symbol, String name, String exchange, boolean visible, boolean suspended) {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
        this.visible = visible;
        this.suspended = suspended;
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

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
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
                case EXCHANGE:
                    oStruct.put(tField.name(), getExchange());
                    break;
                case VISIBLE:
                    oStruct.put(tField.name(), isVisible());
                    break;
                case SUSPENDED:
                    oStruct.put(tField.name(), isSuspended());
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
            } catch (IllegalArgumentException e) {
                throw new StockPlayException("requested key '" + tKey + "' does not exist");
            }

            switch (tField) {
                case NAME:
                    setName((String) tValue);
                    break;
                case EXCHANGE:
                    setExchange((String) tValue);
                    break;
                case VISIBLE:
                    setVisible((Boolean) tValue);
                    break;
                case SUSPENDED:
                    setSuspended((Boolean) tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }
}
