/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.backend.api.user;

import com.kapti.backend.api.MethodClass;
import com.kapti.backend.helpers.DateHelper;
import com.kapti.data.PointsTransaction;
import com.kapti.data.PointsTransaction.Fields;
import com.kapti.data.PointsTransaction.PointsTransactionPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Calendar;
import java.util.Collection;
import java.util.Hashtable;
import java.util.TimeZone;
import java.util.Vector;

/**
 *
 * @author Thijs
 */
public class PointsHandler extends MethodClass {

    public Vector<Hashtable<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<PointsTransaction, PointsTransactionPK> tPointsTransactionDAO = getDAO().getPointsTransactionDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<PointsTransaction> tTransactions = tPointsTransactionDAO.findByFilter(filter);
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (PointsTransaction tTransaction : tTransactions) {
            oVector.add(tTransaction.toStruct(
                    Fields.USER,
                    Fields.TIMESTAMP,
                    Fields.DELTA,
                    Fields.COMMENTS));
        }

        return oVector;
    }

    public int CreateTransaction(Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<PointsTransaction, PointsTransactionPK> tPointsTransactionDAO = getDAO().getPointsTransactionDAO();

        // Instantiate a new transaction
        iDetails.put(Fields.TIMESTAMP.toString(), DateHelper.convertCalendar(Calendar.getInstance(), TimeZone.getTimeZone("GMT")).getTime());
        PointsTransaction tTransaction = PointsTransaction.fromStruct(iDetails);
        tTransaction.applyStruct(iDetails);

        return tPointsTransactionDAO.create(tTransaction);

    }

    public int DeleteTransaction(Hashtable<String, Object> iDetails) throws StockPlayException{
        // Get DAO reference
        GenericDAO<PointsTransaction, PointsTransactionPK> tPointsTransactionDAO = getDAO().getPointsTransactionDAO();

        PointsTransaction tTransaction = PointsTransaction.fromStruct(iDetails);

        return tPointsTransactionDAO.delete(tTransaction) ? 1:0;
    }
}
