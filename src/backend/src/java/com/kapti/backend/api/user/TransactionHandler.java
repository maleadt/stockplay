/*
 * TransactionHandler.java
 * StockPlay - Handler van de User.Transaction subklasse.
 *
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
import com.kapti.data.Transaction;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
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
    public Vector<Hashtable<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<Transaction, Integer> tTransactionDAO = getDAO().getTransactionDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<Transaction> tTransactions = tTransactionDAO.findByFilter(filter);
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
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
}