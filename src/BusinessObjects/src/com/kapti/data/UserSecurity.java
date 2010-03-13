package com.kapti.data;

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

    public Hashtable toStruct(Fields... iFields) {
        Hashtable<String, Object> oStruct = new Hashtable<String, Object>();
        for (Fields tField : iFields) {
            switch (tField) {
                case USER:
                    oStruct.put("user", getPk().user);
                    break;
                case SYMBOL:
                    oStruct.put("symbol", getPk().symbol);
                    break;
                case AMOUNT:
                    oStruct.put("amount", getAmount());
                    break;
            }
        }
        return oStruct;
    }
}