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
import com.kapti.data.Transaction;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.TimeZone;
import java.util.Vector;

/**
 * \brief   Handler van de User.Transaction subklasse.
 *
 * Deze klasse is de handler van de User.Transaction subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class TransactionHandler extends MethodClass {
    public Vector<HashMap<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<Transaction, Integer> tTransactionDAO = getDAO().getTransactionDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<Transaction> tTransactions = tTransactionDAO.findByFilter(filter);
        Vector<HashMap<String, Object>> oVector = new Vector<HashMap<String, Object>>();
        for (Transaction tTransaction : tTransactions) {
            oVector.add(tTransaction.toStruct(
                    com.kapti.data.Transaction.Fields.AMOUNT,
                    com.kapti.data.Transaction.Fields.ID,
                    com.kapti.data.Transaction.Fields.PRICE,
                    com.kapti.data.Transaction.Fields.ISIN,
                    com.kapti.data.Transaction.Fields.TIME,
                    com.kapti.data.Transaction.Fields.TYPE,
                    com.kapti.data.Transaction.Fields.USER));
        }

        return oVector;        
    }
    
        public int Create(HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Transaction, Integer> tTransactionDAO = getDAO().getTransactionDAO();

        // Instantiate a new user
        Transaction tTransaction = Transaction.fromStruct(iDetails);
        tTransaction.applyStruct(iDetails);
        tTransaction.setTime(DateHelper.convertCalendar(Calendar.getInstance(), TimeZone.getTimeZone("GMT")).getTime());

        return tTransactionDAO.create(tTransaction);

    }

       public int Modify(String iFilter, HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Transaction, Integer> tTransactionDAO = getDAO().getTransactionDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Now apply the new properties
        // TODO: controleren of de struct geen ID field bevat, deze kan _enkel_
        //       gebruikt worden om een initiële Exchange aa nte maken (Create)
        for (com.kapti.data.Transaction tTransaction : tTransactionDAO.findByFilter(filter)) {
            tTransaction.applyStruct(iDetails);
            tTransactionDAO.update(tTransaction);
        }

        return 1;
    }

//    public int Remove(String iFilter) throws StockPlayException {
//        // Get DAO reference
//        GenericDAO<com.kapti.data.Transaction, Integer> tTransactionDAO = getDAO().getTransactionDAO();
//
//        Parser parser = Parser.getInstance();
//        Filter filter = parser.parse(iFilter);
//
//      for (com.kapti.data.Transaction tTransaction : tTransactionDAO.findByFilter(filter)) {
//            tTransactionDAO.delete(tTransaction);
//        }
//
//        return 1;
//    }
}