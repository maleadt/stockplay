/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.kapti.data.persistence;

import com.kapti.exceptions.FatalException;
import com.kapti.exceptions.StockPlayException;
import java.util.ResourceBundle;

/**
 *
 * @author Thijs
 */
public class StockPlayDAOFactory {

    /**
     * This method instantiates a particular subclass implementing
     * the DAO methods based on the information obtained from the
     * deployment descriptor
     */
    public static StockPlayDAO getDAO() throws StockPlayException {
        try {
            StockPlayDAO spDAO = null;
            ResourceBundle rb = ResourceBundle.getBundle("com.kapti.bo.persistence.StockPlayDAOFactory");
            spDAO = (StockPlayDAO) Class.forName(rb.getString("Class")).newInstance();
            return spDAO;
        } catch (InstantiationException ex) {
            throw new FatalException("Could not load properties-file with DAO-class", ex);
        } catch (IllegalAccessException ex) {
            throw new FatalException("Could not load properties-file with DAO-class", ex);
        } catch (ClassNotFoundException ex) {
            throw new FatalException("Could not find DAO-class", ex);
        }
    }

}
