package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class UserSecurity {
    //
    // Member data
    //

    public static enum Fields {
        USER, SYMBOL, AMOUNT
    }
    
    private UserSecurityPK pk;
    private int amount;


    //
    // Construction
    //

    public UserSecurity(UserSecurityPK pk, int amount) {
        this.pk = pk;
        this.amount = amount;
    }

    public UserSecurity(int user, String symbol, int amount){
        this.pk = new UserSecurityPK(user, symbol);
        this.amount = amount;
    }


    //
    // Methods
    //

    public int getAmount() {
        return amount;
    }

    public UserSecurityPK getPk() {
        return pk;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Hashtable<String, Object> toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case USER:
                    oStruct.put(tField.name(), getPk().user);
                    break;
                case SYMBOL:
                    oStruct.put(tField.name(), getPk().symbol);
                    break;
                case AMOUNT:
                    oStruct.put(tField.name(), getAmount());
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
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                default:
                    throw new StockPlayException("requested key '" + tKey + "' cannot be modified");
            }
        }
    }


    //
    // Subclasses
    //

    public class UserSecurityPK{
        private int user;
        private String symbol;

        public String getSymbol() {
            return symbol;
        }

        public int getUser() {
            return user;
        }

        public UserSecurityPK(int user, String symbol) {
            this.user = user;
            this.symbol = symbol;
        }

        public UserSecurityPK() {
        }
    }
}