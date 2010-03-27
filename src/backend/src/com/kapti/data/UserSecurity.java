/*
 * UserSecurity.java
 * StockPlay - Koppeling tussen gebruikers en effecten
 *
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.kapti.data;

import com.kapti.exceptions.StockPlayException;
import java.util.Hashtable;

public class UserSecurity {
    //
    // Member data
    //

    public static enum Fields {
        USER, ISIN, AMOUNT
    }
    
    private UserSecurityPK pk;
    private int amount;


    //
    // Construction
    //

    public UserSecurity(int user, String isin){
        this.pk = new UserSecurityPK(user, isin);
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
                case ISIN:
                    oStruct.put(tField.name(), getPk().isin);
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
        private String isin;

        public String getSymbol() {
            return isin;
        }

        public int getUser() {
            return user;
        }

        public UserSecurityPK(int user, String symbol) {
            this.user = user;
            this.isin = symbol;
        }

        public UserSecurityPK() {
        }

        public String getIsin() {
            return isin;
        }
        
    }
}