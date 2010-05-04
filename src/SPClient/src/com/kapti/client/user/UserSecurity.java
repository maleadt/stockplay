/*
 * UserSecurity.java
 * StockPlay - Effectenobject
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

package com.kapti.client.user;

import com.kapti.client.finance.FinanceFactory;
import com.kapti.client.finance.Security;
import com.kapti.exceptions.StockPlayException;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Effectenobject
 *
 */

public class UserSecurity {

    private static Logger logger = Logger.getLogger(Transaction.class);

    public static enum Fields {
        USER, ISIN, AMOUNT
    }

    protected User user;
    public static final String PROP_USER = "user";

    UserSecurity() {}

    /**
     * Get the value of userid
     *
     * @return the value of userid
     */
    public User getUser() {
        return user;
    }

    void setUser(User user) {
        this.user = user;
    }

    protected int amount;
    public static final String PROP_AMOUNT = "amount";

    /**
     * Get the value of amount
     *
     * @return the value of amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Set the value of amount
     *
     * @param time new value of amount
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    protected Security security;
    public static final String PROP_SECURITY = "security";

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
        return security;
    }

    /**
     * Set the value of security
     *
     * @param time new value of security
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UserSecurity other = (UserSecurity) obj;
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
        if (this.amount != other.amount) {
            return false;
        }
        if (this.security != other.security && (this.security == null || !this.security.equals(other.security))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 47 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 47 * hash + this.amount;
        hash = 47 * hash + (this.security != null ? this.security.hashCode() : 0);
        return hash;
    }

    public static UserSecurity fromStruct(HashMap h) {
        UserSecurity t = new UserSecurity();

        try {
            //Om onnodig ophalen van het Userobject te vermijden, kan dit ook toegevoegd
            //worden aan de hashmap (als "USEROBJECT"), anders wordt het opgehaald via XML-RPC
            if(h.containsKey("USEROBJECT"))
                t.setUser((User) h.get("USEROBJECT"));
            else
                t.setUser(UserFactory.getInstance().getUserById((Integer) h.get(Fields.USER.toString())));
        }
        catch(StockPlayException ex) {
            t.setUser(null);
            logger.error("Error while retrieving user with id " + h.get(Fields.USER.toString()), ex);
        }

        try {
            t.setSecurity(FinanceFactory.getInstance().getSecurityById((String) h.get(Fields.ISIN.toString())));
        }
        catch(StockPlayException e) {
            t.setSecurity(null);
            logger.error("Error while retrieving security with id " + h.get(Fields.ISIN.toString()));
        }

        t.setAmount((Integer) h.get(Fields.AMOUNT.toString()));

        return t;
    }

    public HashMap toStruct() {

        HashMap h = new HashMap();

        h.put(Fields.USER.toString(), getUser().getId());
        if(getSecurity() != null)
        h.put(Fields.ISIN.toString(), getSecurity().getISIN());
        h.put(Fields.AMOUNT.toString(), getAmount());

        return h;
    }

}
