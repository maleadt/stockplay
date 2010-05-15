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
public class Transaction {

    private static final String IDFIELD = "ID";
    private static final String USERFIELD = "USER";
    private static final String ISINFIELD = "ISIN";
    private static final String AMOUNTFIELD = "AMOUNT";
    private static final String PRICEFIELD = "PRICE";
    private static final String TYPEFIELD = "TYPE";
    private static final String TIMEFIELD = "TIME";
    private static final String COMMENTSFIELD = "COMMENTS";
    protected int id;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    protected int userid = -1;
    protected User user;

    /**
     * Get the value of user
     *
     * @return the value of user
     */
    public User getUser() {
        if(user == null && userid != -1){
            try {
                user = UserFactory.getInstance().getUserById(userid);
            } catch (StockPlayException ex) {
                ex.printStackTrace();
            }
        }
        return user;
    }
    protected Date time;

    /**
     * Get the value of time
     *
     * @return the value of time
     */
    public Date getTime() {
        return time;
    }
    protected String isin;
    protected Security security = null;

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
        if (security == null && isin != null) {
            try {
                security = FinanceFactory.getInstance().getSecurityById(isin);
            } catch (StockPlayException ex) {
                ex.printStackTrace();
            }
        }
        return security;
    }

    public static class Type {

        public final static String BUY = "BUY";
        public final static String SELL = "SELL";
        public final static String MANUAL = "MANUAL";
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
    protected int amount;

    /**
     * Get the value of amount
     *
     * @return the value of amount
     */
    public int getAmount() {
        return amount;
    }
    protected double price;

    /**
     * Get the value of price
     *
     * @return the value of price
     */
    public double getPrice() {
        return price;
    }
    protected String comment;

    /**
     * Get the value of comment
     *
     * @return the value of comment
     */
    public String getComment() {
        return comment;
    }

    public static Transaction fromStruct(Hashtable h) {
        Transaction t = new Transaction();


        for (Enumeration k = h.keys(); k.hasMoreElements();) {
            String key = (String) k.nextElement();

            if (key.equals(IDFIELD)) {
                t.id = ((Integer) h.get(IDFIELD)).intValue();
            } else if (key.equals(AMOUNTFIELD)) {
                t.amount = ((Integer) h.get(AMOUNTFIELD)).intValue();
            } else if (key.equals(COMMENTSFIELD)) {
                t.comment = (String) h.get(COMMENTSFIELD);
            } else if (key.equals(ISINFIELD)) {
                t.isin = (String) h.get(ISINFIELD);
            } else if (key.equals(PRICEFIELD)) {
                t.price = Double.parseDouble((String) h.get(PRICEFIELD));
            } else if (key.equals(TIMEFIELD)) {
                t.time = (Date) h.get(TIMEFIELD);
            } else if (key.equals(TYPEFIELD)) {
                t.type = (String) h.get(TYPEFIELD);
            } else if (key.equals(USERFIELD)) {
                t.userid = ((Integer) h.get(USERFIELD)).intValue();
            } 

        }

        return t;
    }
}
