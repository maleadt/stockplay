/*
 * Order.java
 * StockPlay - Order object.
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
 * \brief   Order object.
 *
 */

public class Order {

    private static Logger logger = Logger.getLogger(Order.class);

    public static enum Fields {

        ID, USER, ISIN, AMOUNT, PRICE, TYPE, // Instruction.Fields
        STATUS, CREATIONTIME, EXPIRATIONTIME, EXECUTIONTIME, PARAMETERS, SECONDAIRYLIMIT
    }

    public enum OrderStatus {

        ACCEPTED,
        EXECUTED,
        CANCELLED,
        FAILED
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

    Order(int id) {
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
        IMMEDIATE_BUY,
        STOP_LOSS_BUY,
        STOP_LOSS_SELL,
        IMMEDIATE_SELL,
        TRAILING_STOP_BUY,
        TRAILING_STOP_SELL,
        BRACKET_LIMIT_BUY,
        BRACKET_LIMIT_SELL,
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

    protected double secondairyLimit;
    public static final String PROP_SECONDAIRY_LIMIT = "secondairylimit";

    /**
     * Get the value of price
     *
     * @return the value of price
     */
    public double getSecondairyLimit() {
        return secondairyLimit;
    }

    /**
     * Set the value of price
     *
     * @param price new value of price
     */
    public void setSecondairyLimit(double secondairyLimit) {
        double oldSecondairyLimit = this.secondairyLimit;
        this.secondairyLimit = secondairyLimit;
        propertyChangeSupport.firePropertyChange(PROP_SECONDAIRY_LIMIT, oldSecondairyLimit, secondairyLimit);
    }

    protected OrderStatus status;
    public static final String PROP_STATUS = "status";

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public OrderStatus getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(OrderStatus status) {
        OrderStatus oldStatus = this.status;
        this.status = status;
        propertyChangeSupport.firePropertyChange(PROP_STATUS, oldStatus, status);
    }
    protected Date creationTime;
    public static final String PROP_CREATIONTIME = "creationTime";

    /**
     * Get the value of creationTime
     *
     * @return the value of creationTime
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * Set the value of creationTime
     *
     * @param creationTime new value of creationTime
     */
    public void setCreationTime(Date creationTime) {
        Date oldCreationTime = this.creationTime;
        this.creationTime = creationTime;
        propertyChangeSupport.firePropertyChange(PROP_CREATIONTIME, oldCreationTime, creationTime);
    }
    protected Date executionTime;
    public static final String PROP_EXECUTIONTIME = "executionTime";

    /**
     * Get the value of executionTime
     *
     * @return the value of executionTime
     */
    public Date getExecutionTime() {
        return executionTime;
    }

    /**
     * Set the value of executionTime
     *
     * @param executionTime new value of executionTime
     */
    public void setExecutionTime(Date executionTime) {
        Date oldExecutionTime = this.executionTime;
        this.executionTime = executionTime;
        propertyChangeSupport.firePropertyChange(PROP_EXECUTIONTIME, oldExecutionTime, executionTime);
    }
    protected Date expirationTime;
    public static final String PROP_EXPIRATIONTIME = "expirationTime";

    /**
     * Get the value of expirationTime
     *
     * @return the value of expirationTime
     */
    public Date getExpirationTime() {
        return expirationTime;
    }

    /**
     * Set the value of expirationTime
     *
     * @param expirationTime new value of expirationTime
     */
    public void setExpirationTime(Date expirationTime) {
        Date oldExpirationTime = this.expirationTime;
        this.expirationTime = expirationTime;
        propertyChangeSupport.firePropertyChange(PROP_EXPIRATIONTIME, oldExpirationTime, expirationTime);
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

    public static Order fromStruct(HashMap h) {
        Order t = new Order((Integer) h.get(Fields.ID.toString()));
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

        //for (Object test : h.keySet().toArray())
        //    System.out.println(test.toString());

        //System.out.println(h.get("SECONDAIRYLIMIT"));

        t.setType(Type.valueOf((String) h.get(Fields.TYPE.toString())));
        t.setAmount((Integer) h.get(Fields.AMOUNT.toString()));
        t.setPrice((Double) h.get(Fields.PRICE.toString()));
        t.setSecondairyLimit((Double) h.get(Fields.SECONDAIRYLIMIT.toString()));
        t.setCreationTime((Date) h.get(Fields.CREATIONTIME.toString()));
        t.setExecutionTime((Date) h.get(Fields.EXECUTIONTIME.toString()));
        t.setExpirationTime((Date) h.get(Fields.EXPIRATIONTIME.toString()));
        t.setStatus(OrderStatus.valueOf((String) h.get(Fields.STATUS.toString())));
        return t;
    }

    public HashMap toStruct() {

        HashMap h = new HashMap();

        h.put(Fields.ID.toString(), getId());
        h.put(Fields.USER.toString(), getUser().getId());
        h.put(Fields.ISIN.toString(), getSecurity().getISIN());
        h.put(Fields.TYPE.toString(), getType().toString());
        if (getCreationTime() != null) {
            h.put(Fields.CREATIONTIME.toString(), getCreationTime());
        }
        if (getExecutionTime() != null) {
            h.put(Fields.EXECUTIONTIME.toString(), getExecutionTime());
        }
        if (getExpirationTime() != null) {
            h.put(Fields.EXPIRATIONTIME.toString(), getExpirationTime());
        }
        h.put(Fields.AMOUNT.toString(), getAmount());
        h.put(Fields.PRICE.toString(), getPrice());
        h.put(Fields.TYPE.toString(), getType().name());
        if (getStatus() != null) {
            h.put(Fields.STATUS.toString(), getStatus().toString());
        }

        return h;


    }
}
