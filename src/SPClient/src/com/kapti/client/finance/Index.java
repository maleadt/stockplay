package com.kapti.client.finance;

import com.kapti.exceptions.StockPlayException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Dieter
 */
public class Index {

    private static Logger logger = Logger.getLogger(Security.class);

    public static enum Fields {
        ISIN, SYMBOL, NAME, EXCHANGE
    }

    public Index(String isin, String symbol, Exchange exchange, String name) {
        this.ISIN = isin;
        this.symbol = symbol;
        this.exchange = exchange;
        this.name = name;
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
    protected String ISIN;

    /**
     * Get the value of ISIN
     *
     * @return the value of ISIN
     */
    public String getISIN() {
        return ISIN;
    }
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

    public void setExchange(Exchange exchange) {
        this.exchange = exchange;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Security other = (Security) obj;
        if ((this.ISIN == null) ? (other.ISIN != null) : !this.ISIN.equals(other.ISIN)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + (this.ISIN != null ? this.ISIN.hashCode() : 0);
        return hash;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static Index fromStruct(HashMap h) {
        Exchange exch = null;
        try {
            exch = FinanceFactory.getInstance().getExchange((String)h.get(Fields.EXCHANGE.toString()));
        }catch(StockPlayException ex){
            logger.error(ex);
        }

        return new Index(
                (String) h.get(Security.Fields.ISIN.toString()),
                (String) h.get(Security.Fields.SYMBOL.toString()),
                 exch,
                (String) h.get(Security.Fields.NAME.toString()));
    }

    public HashMap toStruct(){

        HashMap h = new HashMap();
        h.put(Fields.ISIN.toString(), getISIN());
        h.put(Fields.EXCHANGE.toString(), getExchange().getSymbol());
        h.put(Fields.NAME.toString(), getName());
        h.put(Fields.SYMBOL.toString(), getSymbol());
        return h;


    }

}
