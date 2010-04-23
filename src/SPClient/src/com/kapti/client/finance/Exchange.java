/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.client.finance;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;

/**
 *
 * @author Thijs
 */
public class Exchange {

    public static enum Fields {

        SYMBOL, NAME, LOCATION
    }
    /**
     * Deze variabele geeft aan of de waarden van het object werden gewijzigd
     */
    private boolean dirty = false;

    Exchange(String symbol, String name, String location) {
        this.symbol = symbol;
        this.name = name;
        this.location = location;
    }

    boolean isDirty() {
        return dirty;
    }

    void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
    protected String symbol;

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public String getSymbol() {
        return symbol;
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
    protected String location;
    public static final String PROP_LOCATION = "location";

    /**
     * Get the value of location
     *
     * @return the value of location
     */
    public String getLocation() {
        return location;
    }

    /**
     * Set the value of location
     *
     * @param location new value of location
     */
    public void setLocation(String location) {
        String oldLocation = this.location;
        this.location = location;
        propertyChangeSupport.firePropertyChange(PROP_LOCATION, oldLocation, location);
        dirty = true;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Exchange other = (Exchange) obj;
        if ((this.symbol == null) ? (other.symbol != null) : !this.symbol.equals(other.symbol)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.location == null) ? (other.location != null) : !this.location.equals(other.location)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 83 * hash + (this.symbol != null ? this.symbol.hashCode() : 0);
        hash = 83 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 83 * hash + (this.location != null ? this.location.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return name;
    }

    public static Exchange fromStruct(HashMap h) {
        return new Exchange((String) h.get(Exchange.Fields.SYMBOL.toString()),
                (String) h.get(Exchange.Fields.NAME.toString()),
                (String) h.get(Exchange.Fields.LOCATION.toString()));
    }


    public HashMap toStruct(){
        HashMap h = new HashMap();
        h.put(Fields.SYMBOL.toString(), getSymbol());
        h.put(Fields.NAME.toString(), getName());
        h.put(Fields.LOCATION.toString(), getLocation());
        return h;

    }
}
