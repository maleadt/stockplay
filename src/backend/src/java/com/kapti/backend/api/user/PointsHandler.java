/*
 * Copyright (c) 2010 StockPlay development team
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
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
 * \brief   Handler van de User.Points subklasse.
 *
 * Deze klasse is de handler van de User.Points subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
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
