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

import com.kapti.exceptions.InvocationException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class UserSecurity {
    //
    // Member data
    //

    public static enum Fields {
        USER, ISIN, AMOUNT
    }
    public static Map<Fields, Class> Types = new HashMap<Fields, Class>() { {
            put(Fields.USER, Integer.class);     // Deel van de
            put(Fields.ISIN, String.class);     // UserSecurityPK
            put(Fields.AMOUNT, Integer.class);
    } };
    
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

    public static UserSecurity fromStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        // Create case mapping
        Hashtable<Fields, String> tStructMap = new Hashtable<Fields, String>();
        for (String tKey : iStruct.keySet()) {
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());
            tStructMap.put(tField, tKey);
        }

        // Check needed keys
        if (tStructMap.containsKey(Fields.USER) && tStructMap.containsKey(Fields.ISIN)) {
            UserSecurity tUserSecurity = new UserSecurity((Integer)iStruct.get(tStructMap.get(Fields.USER)), (String)iStruct.get(tStructMap.get(Fields.ISIN)));
            iStruct.remove(tStructMap.get(Fields.USER));
            iStruct.remove(tStructMap.get(Fields.ISIN));
            return tUserSecurity;
        } else
            throw new ServiceException(ServiceException.Type.NOT_ENOUGH_INFORMATION);
    }

    public void applyStruct(Hashtable<String, Object> iStruct) throws StockPlayException {
        for (String tKey : iStruct.keySet()) {
            Object tValue = iStruct.get(tKey);
            Fields tField = null;
            try {
                tField = Fields.valueOf(tKey.toUpperCase());
            }
            catch (IllegalArgumentException e) {
                throw new InvocationException(InvocationException.Type.KEY_DOES_NOT_EXIST, "requested key '" + tKey + "' does not exist");
            }
            if (!Types.get(tField).isInstance(iStruct.get(tKey)))
                throw new InvocationException(InvocationException.Type.BAD_REQUEST, "provided key '" + tKey + "' requires a " + Types.get(tField) + " instead of an " + iStruct.get(tKey).getClass());

            switch (tField) {
                case AMOUNT:
                    setAmount((Integer)tValue);
                    break;
                default:
                    throw new InvocationException(InvocationException.Type.READ_ONLY_KEY,  "requested key '" + tKey + "' cannot be modified");
            }
        }
    }


    //
    // Subclasses
    //

    public class UserSecurityPK {
        private int user;
        private String isin;

        public UserSecurityPK(int user, String symbol) {
            this.user = user;
            this.isin = symbol;
        }

        public int getUser() {
            return user;
        }

        public String getIsin() {
            return isin;
        }
        
    }
}