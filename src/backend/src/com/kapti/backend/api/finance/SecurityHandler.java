/*
 * SecurityHandler.java
 * StockPlay - Handler van de Finance.Security subklasse.
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
package com.kapti.backend.api.finance;

import com.kapti.backend.api.MethodClass;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.QuoteDAO;

import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.ParserException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
import com.kapti.data.Quote;

/**
 * \brief   Handler van de Finance.Security subklasse.
 *
 * Deze klasse is de handler van de Finance.Security subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class SecurityHandler extends MethodClass {
    //
    // Methodes
    //

    public List<Map<String, Object>> List() throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Security> tIndexs = tSecurityDAO.findAll();
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Security tIndex : tIndexs) {
            oVector.add(tIndex.toStruct(
                    com.kapti.data.Security.Fields.SYMBOL,
                    com.kapti.data.Security.Fields.NAME,
                    com.kapti.data.Security.Fields.EXCHANGE,
                    com.kapti.data.Security.Fields.VISIBLE,
                    com.kapti.data.Security.Fields.SUSPENDED));
        }

        return oVector;
    }

    public List<Map<String, Object>> List(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Security> tIndexs = tSecurityDAO.findByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Security tIndex : tIndexs) {
            oVector.add(tIndex.toStruct(
                    com.kapti.data.Security.Fields.SYMBOL,
                    com.kapti.data.Security.Fields.NAME,
                    com.kapti.data.Security.Fields.EXCHANGE,
                    com.kapti.data.Security.Fields.VISIBLE,
                    com.kapti.data.Security.Fields.SUSPENDED));
        }

        return oVector;
    }

    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        // Get the Indexs we need to modify
        Collection<com.kapti.data.Security> tIndexs = tSecurityDAO.findAll();

        // Now apply the new properties
        // TODO: controleren of de struct geen ID field bevat, deze kan _enkel_
        //       gebruikt worden om een initiële Exchange aa nte maken (Create)
        for (com.kapti.data.Security tIndex : tIndexs) {
            tIndex.fromStruct(iDetails);
            tSecurityDAO.update(tIndex);
        }

        return 1;
    }

    public int Create(Hashtable<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        com.kapti.data.Security tSecurity = new com.kapti.data.Security();
        tSecurity.fromStruct(iDetails);
        tSecurityDAO.create(tSecurity);

        return 1;
    }

    public List<Map<String, Object>> Details(String iFilter) throws StockPlayException {
        // Get DAO reference
        QuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Quote> tQuotes = tQuoteDAO.findLatestByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Quote tQuote : tQuotes) {
            oVector.add(tQuote.toStruct(
                    Quote.Fields.TIME,
                    Quote.Fields.PRICE,
                    Quote.Fields.VOLUME,
                    Quote.Fields.BID,
                    Quote.Fields.ASK,
                    Quote.Fields.LOW,
                    Quote.Fields.HIGH,
                    Quote.Fields.OPEN
                    ));
        }

        return oVector;
    }

    /**
     * Geeft alle koersen die aan aan de opgegeven filter voldoen
     * @param iFilter
     * @return
     * @throws XmlRpcException
     * @throws StockPlayException
     * @throws FilterException
     * @throws ParserException
     */
        public List<Map<String, Object>> Quotes(String iFilter) throws StockPlayException {
        // Get DAO reference
        QuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Quote> tQuotes = tQuoteDAO.findByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Quote tQuote : tQuotes) {
            oVector.add(tQuote.toStruct(
                    Quote.Fields.TIME,
                    Quote.Fields.PRICE,
                    Quote.Fields.VOLUME,
                    Quote.Fields.BID,
                    Quote.Fields.ASK,
                    Quote.Fields.LOW,
                    Quote.Fields.HIGH,
                    Quote.Fields.OPEN
                    ));
        }

        return oVector;
    }
}