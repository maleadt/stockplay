/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.administration.bo.finance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 *
 * @author Thijs
 */
public class Security {

    public Security(String symbol, Exchange exchange, String name, boolean visible, boolean suspended) {
        this.symbol = symbol;
        this.exchange = exchange;
        this.name = name;
        this.visible = visible;
        this.suspended = suspended;
    }

    public Security(String symbol, Exchange exchange) {
        this.symbol = symbol;
        this.exchange = exchange;
    }
    /**
     * Deze variabele geeft aan of de waarden van het object werden gewijzigd
     */
    private boolean dirty = false;

    boolean isDirty() {
        return dirty;
    }

    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }


//        protected String ISIN;
//
//    /**
//     * Get the value of ISIN
//     *
//     * @return the value of ISIN
//     */
//    public String getISIN() {
//        return ISIN;
//    }
    protected String symbol;

    /**
     * Get the value of symbol
     *
     * @return the value of symbol
     */
    public String getSymbol() {
        return symbol;
    }
    protected Exchange exchange;

    /**
     * Get the value of exchange
     *
     * @return the value of exchange
     */
    public Exchange getExchange() {
        return exchange;
    }
    protected String name;
    public static final String PROP_NAME = "name";

    /**
     * Get the value of name
     *
     * @return the value of name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the value of name
     *
     * @param name new value of name
     */
    public void setName(String name) {
        String oldName = this.name;
        this.name = name;
        propertyChangeSupport.firePropertyChange(PROP_NAME, oldName, name);
        dirty = true;
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
    protected boolean visible;
    public static final String PROP_VISIBLE = "visible";

    /**
     * Get the value of visible
     *
     * @return the value of visible
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Set the value of visible
     *
     * @param visible new value of visible
     */
    public void setVisible(boolean visible) {
        boolean oldVisible = this.visible;
        this.visible = visible;
        propertyChangeSupport.firePropertyChange(PROP_VISIBLE, oldVisible, visible);
        dirty = true;
    }
    protected boolean suspended;
    public static final String PROP_SUSPENDED = "suspended";

    /**
     * Get the value of suspended
     *
     * @return the value of suspended
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * Set the value of suspended
     *
     * @param suspended new value of suspended
     */
    public void setSuspended(boolean suspended) {
        boolean oldSuspended = this.suspended;
        this.suspended = suspended;
        propertyChangeSupport.firePropertyChange(PROP_SUSPENDED, oldSuspended, suspended);
        dirty = true;
    }


    public enum SecurityType {
        Stock,
        Fund,
        Tracker
    }

    protected SecurityType type = SecurityType.Stock;
    public static final String PROP_TYPE = "type";

    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public SecurityType getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(SecurityType type) {
        SecurityType oldType = this.type;
        this.type = type;
        propertyChangeSupport.firePropertyChange(PROP_TYPE, oldType, type);
        dirty=true;
    }


}