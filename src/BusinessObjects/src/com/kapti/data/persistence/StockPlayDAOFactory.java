package com.kapti.data.persistence;

import com.kapti.exceptions.FatalException;
import com.kapti.exceptions.StockPlayException;
import java.util.ResourceBundle;

public class StockPlayDAOFactory {

    public static StockPlayDAO getDAO() throws StockPlayException {
        try {
            StockPlayDAO spDAO = null;
            ResourceBundle rb = ResourceBundle.getBundle("com.kapti.data.persistence.StockPlayDAOFactory");
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
