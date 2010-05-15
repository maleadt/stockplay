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
package com.kapti.backend.api.finance;

import com.kapti.backend.api.MethodClass;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.GenericQuoteDAO;

import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import com.kapti.data.Quote;
import com.kapti.data.Security;
import com.kapti.exceptions.FilterException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import org.apache.xmlrpc.XmlRpcException;

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
                    com.kapti.data.Security.Fields.ISIN,
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
        Collection<com.kapti.data.Security> tSecurities = tSecurityDAO.findByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Security tIndex : tSecurities) {
            oVector.add(tIndex.toStruct(
                    com.kapti.data.Security.Fields.ISIN,
                    com.kapti.data.Security.Fields.SYMBOL,
                    com.kapti.data.Security.Fields.NAME,
                    com.kapti.data.Security.Fields.EXCHANGE,
                    com.kapti.data.Security.Fields.VISIBLE,
                    com.kapti.data.Security.Fields.SUSPENDED));
        }

        return oVector;
    }

    public int Modify(String iFilter, HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Get the Indexs we need to modify
        Collection<com.kapti.data.Security> tSecurities = tSecurityDAO.findByFilter(filter);

        // Now apply the new properties
        for (com.kapti.data.Security tSecurity : tSecurities) {
            tSecurity.applyStruct(iDetails);
            tSecurityDAO.update(tSecurity);
        }

        // Deze waarde kan gebruikt worden bij de unit tests om te verzekeren
        // dat het correct aantal securities aangepast zijn.
        return tSecurities.size();
    }

    public int Create(HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        // Instantiate a new security
        Security tSecurity = Security.fromStruct(iDetails);
        
        tSecurity.applyStruct(iDetails);
        tSecurityDAO.create(tSecurity);

        return 1;
    }

    public int Remove(String iFilter) throws StockPlayException {
        GenericDAO<com.kapti.data.Security, String> tSecurityDAO = getDAO().getSecurityDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        Collection<com.kapti.data.Security> tSecurities = tSecurityDAO.findByFilter(filter);

        for(com.kapti.data.Security security : tSecurities)
            tSecurityDAO.delete(security);

        return tSecurities.size();
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
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Quote> tQuotes = tQuoteDAO.findByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Quote tQuote : tQuotes) {
            oVector.add(tQuote.toStruct(
                    Quote.Fields.ISIN,
                    Quote.Fields.TIME,
                    Quote.Fields.PRICE,
                    Quote.Fields.VOLUME,
                    Quote.Fields.BID,
                    Quote.Fields.ASK,
                    Quote.Fields.LOW,
                    Quote.Fields.HIGH,
                    Quote.Fields.OPEN));
        }
        return oVector;
    }

    /**
     * Geeft alle koersen die aan aan de opgegeven filter voldoen, gelimiteerd
     * tot een bepaalde range en "breedte".
     * @param iStart
     * @param iEnd
     * @param iSpan
     * @param iFilter
     * @return
     * @throws XmlRpcException
     * @throws StockPlayException
     * @throws FilterException
     * @throws ParserException
     */
    public List<Map<String, Object>> Quotes(Date iStart, Date iEnd, int iSpan, String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Quote> tQuotes = tQuoteDAO.findSpanByFilter(iStart, iEnd, iSpan, filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Quote tQuote : tQuotes) {
            oVector.add(tQuote.toStruct(
                    Quote.Fields.ISIN,
                    Quote.Fields.TIME,
                    Quote.Fields.PRICE,
                    Quote.Fields.VOLUME,
                    Quote.Fields.BID,
                    Quote.Fields.ASK,
                    Quote.Fields.LOW,
                    Quote.Fields.HIGH,
                    Quote.Fields.OPEN));
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
    public List<Map<String, Object>> LatestQuotes(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Fetch and convert all Indexs
        Collection<com.kapti.data.Quote> tQuotes = tQuoteDAO.findLatestByFilter(filter);
        Vector<Map<String, Object>> oVector = new Vector<Map<String, Object>>();
        for (com.kapti.data.Quote tQuote : tQuotes) {
            oVector.add(tQuote.toStruct(
                    Quote.Fields.ISIN,
                    Quote.Fields.TIME,
                    Quote.Fields.PRICE,
                    Quote.Fields.VOLUME,
                    Quote.Fields.BID,
                    Quote.Fields.ASK,
                    Quote.Fields.LOW,
                    Quote.Fields.HIGH,
                    Quote.Fields.OPEN));
        }
        return oVector;
    }

    public List<Timestamp> QuoteRange(String isin) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();
        return tQuoteDAO.getRange(isin);
    }

    public double getHighest(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        return tQuoteDAO.getHighest(filter);
    }

    public double getLowest(String iFilter) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        return tQuoteDAO.getLowest(filter);
    }

    public int Update(HashMap<String, Object> iDetails) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        // Instantiate a new quote
        Quote tQuote = Quote.fromStruct(iDetails);

        tQuote.applyStruct(iDetails);
        tQuoteDAO.create(tQuote);

        return 1;
    }

    public int UpdateBulk(Vector<HashMap<String, Object>> iQuotes) throws StockPlayException {
        // Get DAO reference
        GenericQuoteDAO tQuoteDAO = getDAO().getQuoteDAO();

        List<Quote> tQuotes = new ArrayList<Quote>();
        for (HashMap<String, Object> iDetails : iQuotes) {
            HashMap<String, Object> iDetails2 = new HashMap<String, Object>(iDetails);
            
            // Instantiate a new quote
            Quote tQuote = Quote.fromStruct(iDetails2);

            tQuote.applyStruct(iDetails2);
            tQuotes.add(tQuote);
        }
        tQuoteDAO.createBulk(tQuotes);

        return 1;
    }
}
