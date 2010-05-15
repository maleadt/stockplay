/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.mobileclient.user;

import com.kapti.mobileclient.finance.FinanceFactory;
import com.kapti.mobileclient.finance.Security;
import com.kapti.mobileclient.exceptions.StockPlayException;
import com.kapti.mobileclient.finance.Quote;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Thijs
 */
public class UserSecurity {

    public static final String USERFIELD = "USER";
    public static final String ISINFIELD = "ISIN";
    public static final String AMOUNTFIELD = "AMOUNT";
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
        if (security == null && isin != null) {
            try {
                security = FinanceFactory.getInstance().getSecurityById(isin);
            } catch (StockPlayException ex) {
                ex.printStackTrace();
            }
        }
        return security;
    }

    /**
     * Set the value of security
     *
     * @param security new value of security
     */
    public void setSecurity(Security security) {
        this.security = security;
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
    private Quote latestquote;

    public Quote getLatestquote() {
        return latestquote;
    }

    public void setLatestquote(Quote latestquote) {
        this.latestquote = latestquote;
    }

    public static UserSecurity fromStruct(Hashtable h) {
        UserSecurity t = new UserSecurity();
        for (Enumeration k = h.keys(); k.hasMoreElements();) {
            String key = (String) k.nextElement();


            if (key.equals(AMOUNTFIELD)) {
                t.amount = ((Integer) h.get(AMOUNTFIELD)).intValue();
            } else if (key.equals(ISINFIELD)) {
                t.isin = (String) h.get(ISINFIELD);
            } else if (key.equals(USERFIELD)) {
                t.userid = ((Integer) h.get(USERFIELD)).intValue();
            }
        }

        //we halen hier direct ook de change op
        try {
            Quote q = FinanceFactory.getInstance().getLatestQuoteFromSecurity(t.getSecurity());

            t.setLatestquote(q);
        } catch (Exception ex) {
           
        }

        return t;
    }

    public Hashtable toStruct() {

        Hashtable h = new Hashtable();

        h.put(USERFIELD, new Integer(getUser().getId()));
        h.put(ISINFIELD, getSecurity().getISIN());
        h.put(AMOUNTFIELD, new Integer(getAmount()));

        return h;

    }
}
