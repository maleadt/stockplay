/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.user;

import com.kapti.mobileclient.finance.FinanceFactory;
import com.kapti.mobileclient.finance.Security;
import com.kapti.mobileclient.exceptions.StockPlayException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class Order {

    public static final String IDFIELD = "ID";
    public static final String USERFIELD = "USER";
    public static final String ISINFIELD = "ISIN";
    public static final String AMOUNTFIELD = "AMOUNT";
    public static final String PRICEFIELD = "PRICE";
    public static final String TYPEFIELD = "TYPE";
    public static final String STATUSFIELD = "STATUS";
    public static final String CREATIONTIMEFIELD = "CREATIONTIME";
    public static final String EXPIRATIONTIMEFIELD = "EXPIRATIONTIME";
    public static final String EXECUTIONTIMEFIELD = "EXECUTIONTIME";
    public static final String PARAMETERSFIELD = "PARAMETERS";

    public static class OrderStatus {

        public static final String ACCEPTED = "ACCEPTED";
        public static final String EXECUTED = "EXECUTED";
        public static final String CANCELLED = "CANCELLED";
        public static final String FAILED = "FAILED";
    }
    protected int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    void setId(int id ){
        this.id= id;
    }

    protected int userid = -1;
    protected User user;

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public User getUser() {
        if (user == null && userid != -1) {
            try {
                user = UserFactory.getInstance().getUserById(userid);
            } catch (StockPlayException ex) {
                ex.printStackTrace();
            }
        }
        return user;

    }

    /**
     * Set the value of user
     *
     * @param user new value of user
     */
    public void setUser(User user) {
        this.user = user;
    }
    protected String isin;
    protected Security security;

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
//        if (security == null && isin != null) {
//            try {
//                security = FinanceFactory.getInstance().getSecurityById(isin);
//            } catch (StockPlayException ex) {
//                ex.printStackTrace();
//            }
//        }
        return security;
    }

    public String getIsin() {
        return isin;
    }

    

    /**
     * Set the value of security
     *
     * @param security new value of security
     */
    public void setSecurity(Security security) {
        this.security = security;
    }

    public static class Type {

        public static final String BUY = "BUY";
        public static final String SELL = "SELL";
        public static final String IMMEDIATE_BUY = "IMMEDIATE_BUY";
        public static final String IMMEDIATE_SELL = "IMMEDIATE_SELL";
        public static final String MANUAL = "MANUAL";
    }
    protected String type;

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public String getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(String type) {
        this.type = type;
    }
    protected int amount = 0;

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
        this.amount = amount;
    }
    protected Double price = null;

    /**
     * Get the value of price
     *
     * @return the value of price
     */
    public Double getPrice() {
        return price;
    }

    /**
     * Set the value of price
     *
     * @param price new value of price
     */
    public void setPrice(double price) {
        this.price = new Double(price);
    }
    protected String status;

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    protected Date creationTime;

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
        this.creationTime = creationTime;
    }
    protected Date executionTime;

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
        this.executionTime = executionTime;
    }
    protected Date expirationTime;

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
        this.expirationTime = expirationTime;
    }

    public static Order fromStruct(Hashtable h) {
        Order t = new Order();
        for (Enumeration k = h.keys(); k.hasMoreElements();) {
            String key = (String) k.nextElement();

            if (key.equals(IDFIELD)) {
                t.id = ((Integer) h.get(IDFIELD)).intValue();
            } else if (key.equals(AMOUNTFIELD)) {
                t.amount = ((Integer) h.get(AMOUNTFIELD)).intValue();
            } else if (key.equals(ISINFIELD)) {
                t.isin = (String) h.get(ISINFIELD);
                try {
                    t.security = FinanceFactory.getInstance().getSecurityById(t.isin);
                } catch (StockPlayException ex) {
                    ex.printStackTrace();
                }

            } else if (key.equals(PRICEFIELD)) {
                t.price = Double.valueOf((String) h.get(PRICEFIELD));
            } else if (key.equals(TYPEFIELD)) {
                t.type = (String) h.get(TYPEFIELD);
            } else if (key.equals(USERFIELD)) {
                t.userid = ((Integer)h.get(USERFIELD)).intValue();
            } else if (key.equals(CREATIONTIMEFIELD)) {
                t.creationTime = (Date) h.get(CREATIONTIMEFIELD);
            } else if (key.equals(EXECUTIONTIMEFIELD)) {
                t.executionTime = (Date) h.get(EXECUTIONTIMEFIELD);
            } else if (key.equals(EXPIRATIONTIMEFIELD)) {
                t.expirationTime = (Date) h.get(EXPIRATIONTIMEFIELD);
            } else if (key.equals(STATUSFIELD)) {
                t.status = (String) h.get(STATUSFIELD);
            }
        }

        return t;
    }

    public Hashtable toStruct() {

        Hashtable h = new Hashtable();

//        h.put(IDFIELD, new Integer(getId()));
        h.put(USERFIELD, new Integer(getUser().getId()));
        h.put(ISINFIELD, getSecurity().getISIN());

//        if (getCreationTime() != null) {
//            h.put(CREATIONTIMEFIELD, getCreationTime());
//        }
//
//        if (getExecutionTime() != null) {
//            h.put(EXECUTIONTIMEFIELD, getExecutionTime());
//        }
//
//        if (getExpirationTime() != null) {
//            h.put(EXPIRATIONTIMEFIELD, getExpirationTime());
//        }

        h.put(AMOUNTFIELD, new Integer(getAmount()));
        h.put(PRICEFIELD,getPrice());
        h.put(TYPEFIELD, getType());
       if(getStatus() != null)
           h.put(STATUSFIELD, getStatus());

        return h;

    }
}
