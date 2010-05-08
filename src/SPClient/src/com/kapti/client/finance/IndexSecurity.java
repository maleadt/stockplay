/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.client.finance;

import com.kapti.exceptions.StockPlayException;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author Thijs
 */
class IndexSecurity {

    private static Logger logger = Logger.getLogger(IndexSecurity.class);

    public static enum Fields {

        INDEX_ISIN, SECURITY_ISIN
    }

    public IndexSecurity(Index index, Security security) {
        this.index = index;
        this.security = security;
    }

    protected Index index;

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    public Index getIndex() {
        return index;
    }
    protected Security security;

    /**
     * Get the value of security
     *
     * @return the value of security
     */
    public Security getSecurity() {
        return security;
    }

    public static IndexSecurity fromStruct(HashMap h) {
        Security sec = null;
        Index ind = null;
        try {
            FinanceFactory ff = FinanceFactory.getInstance();
            sec = ff.getSecurityById((String)h.get(Fields.SECURITY_ISIN.toString()));
            ind = ff.getIndexById((String) h.get(Fields.INDEX_ISIN.toString()));
       } catch (StockPlayException ex) {
            logger.error(ex);
        }

        return new IndexSecurity(ind, sec);
    }

    public HashMap toStruct() {

        HashMap h = new HashMap();
        h.put(Fields.INDEX_ISIN.toString(), getIndex().getISIN());
        h.put(Fields.SECURITY_ISIN.toString(), getSecurity().getISIN());
        return h;


    }
}
