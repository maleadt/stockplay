/*
 * Transaction.java
 * StockPlay - Transaction Fabriek
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
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Date;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * \brief   Transaction Fabriek.
 *
 */

public class Transaction {

    private static Logger logger = Logger.getLogger(Transaction.class);

    public static enum Fields {

        ID, USER, ISIN, AMOUNT, PRICE, TYPE, // Instruction.Fields
        TIME, COMMENTS
    }
    protected int id;
    public static final String PROP_ID = "id";

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    Transaction(int id) {
        this.id = id;
    }
    protected User user;
    public static final String PROP_USER = "user";

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(User user) {
        User oldUser = this.user;
        this.user = user;
        propertyChangeSupport.firePropertyChange(PROP_USER, oldUser, user);
    }
    protected Date time;
    public static final String PROP_TIME = "time";

    /**
     * Get the value of time
     *
     * @return the value of time
     */
    public Date getTime() {
        return time;
    }

    /**
     * Set the value of time
     *
     * @param time new value of time
     */
    public void setTime(Date time) {
        Date oldTime = this.time;
        this.time = time;
        propertyChangeSupport.firePropertyChange(PROP_TIME, oldTime, time);
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
     * @param security new value of security
     */
    public void setSecurity(Security security) {
        Security oldSecurity = this.security;
        this.security = security;
        propertyChangeSupport.firePropertyChange(PROP_SECURITY, oldSecurity, security);
    }

    public enum Type {

        BUY,
        SELL,
        MANUAL;
 
    }
    protected Type type;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(Type type) {
        this.type = type;
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
     * @param amount new value of amount
     */
    public void setAmount(int amount) {
        int oldAmount = this.amount;
        this.amount = amount;
        propertyChangeSupport.firePropertyChange(PROP_AMOUNT, oldAmount, amount);
    }
    protected double price;
    public static final String PROP_PRICE = "price";

    /**
     * Get the value of price
     *
     * @return the value of price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Set the value of price
     *
     * @param price new value of price
     */
    public void setPrice(double price) {
        double oldPrice = this.price;
        this.price = price;
        propertyChangeSupport.firePropertyChange(PROP_PRICE, oldPrice, price);
    }
    protected String comment;
    public static final String PROP_COMMENT = "comment";

    /**
     * Get the value of comment
     *
     * @return the value of comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * Set the value of comment
     *
     * @param comment new value of comment
     */
    public void setComment(String comment) {
        String oldComment = this.comment;
        this.comment = comment;
        propertyChangeSupport.firePropertyChange(PROP_COMMENT, oldComment, comment);
    }
    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    /**
     * Add PropertyChangeListener.
     *
     * @param listener
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Remove PropertyChangeListener.
     *
     * @param listener
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Transaction other = (Transaction) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.user != other.user && (this.user == null || !this.user.equals(other.user))) {
            return false;
        }
//        if (this.time != other.time && (this.time == null || !this.time.equals(other.time))) {
//            return false;
//        }
//        if (this.security != other.security && (this.security == null || !this.security.equals(other.security))) {
//            return false;
//        }
//        if (this.type != other.type && (this.type == null || !this.type.equals(other.type))) {
//            return false;
//        }
//        if (this.amount != other.amount) {
//            return false;
//        }
//        if (this.price != other.price) {
//            return false;
//        }
//        if ((this.comment == null) ? (other.comment != null) : !this.comment.equals(other.comment)) {
//            return false;
//        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 37 * hash + this.id;
        hash = 37 * hash + (this.user != null ? this.user.hashCode() : 0);
        hash = 37 * hash + (this.time != null ? this.time.hashCode() : 0);
        hash = 37 * hash + (this.security != null ? this.security.hashCode() : 0);
        hash = 37 * hash + (this.type != null ? this.type.hashCode() : 0);
        hash = 37 * hash + this.amount;
        hash = 37 * hash + (int) (Double.doubleToLongBits(this.price) ^ (Double.doubleToLongBits(this.price) >>> 32));
        hash = 37 * hash + (this.comment != null ? this.comment.hashCode() : 0);
        return hash;
    }

    public static Transaction fromStruct(HashMap h) {
        Transaction t = new Transaction((Integer) h.get(Fields.ID.toString()));
        try {
            t.setUser(UserFactory.getInstance().getUserById((Integer) h.get(Fields.USER.toString())));
        } catch (StockPlayException ex) {
            t.setUser(null);
            logger.error("Error while retrieving user for transaction " + t.getId(), ex);
        }

        try {
            t.setSecurity(FinanceFactory.getInstance().getSecurityById((String) h.get(Fields.ISIN.toString())));
        } catch (StockPlayException ex) {
            t.setSecurity(null);
            logger.error("Error while retrieven security for transaction " + t.getId(), ex);
        }

        t.setType(Type.valueOf((String) h.get(Fields.TYPE.toString())));
        t.setAmount((Integer) h.get(Fields.AMOUNT.toString()));
        t.setPrice((Double) h.get(Fields.PRICE.toString()));
        t.setComment((String) h.get(Fields.COMMENTS.toString()));

        return t;
    }

    public HashMap toStruct() {
        HashMap h = new HashMap();

        h.put(Fields.ID.toString(), getId());
        h.put(Fields.USER.toString(), getUser().getId());
        if(getSecurity() != null)
        h.put(Fields.ISIN.toString(), getSecurity().getISIN());
        h.put(Fields.TYPE.toString(), getType().toString());
        if(getTime() != null)
        h.put(Fields.TIME.toString(), getTime());
        h.put(Fields.AMOUNT.toString(), getAmount());
        h.put(Fields.PRICE.toString(), getPrice());
        h.put(Fields.COMMENTS.toString(), getComment());

        return h;
    }
}
