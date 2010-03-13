/*
 * ConcreteDummy.java
 * StockPlay - Dummy implementatie van de Finance.Security subklasse.
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
package com.kapti.backend.api.finance.security;

import com.kapti.backend.api.finance.Security;
import com.kapti.data.Quote;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.QuoteDAO;
import com.kapti.data.persistence.SecurityDAO;
import com.kapti.exceptions.StockPlayException;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief   Dummy implementatie van de Finance.Security interface.
 *
 * Deze klasse is een dummy implementatie van de Finance.Security interface. Een
 * dergelijke implementatie geeft valide data terug, zonder daarvoor de database
 * te raadplegen. Deze implementatie kan zo gebruikt worden om een client-systeem
 * te testen.
 */
public class ConcreteDatabase extends Security {
    //
    // Methodes
    //

    @Override
    public Vector<Hashtable<String, Object>> Details(String iFilter) throws XmlRpcException, StockPlayException {
       GenericDAO<com.kapti.data.Security, String> secDAO = getDAO().getSecurityDAO();
       QuoteDAO quoDAO = getDAO().getQuoteDAO();
       Vector<Hashtable<String,Object>> result = new Vector<Hashtable<String,Object>>();
       Collection<com.kapti.data.Security> securities = secDAO.findByExample(new com.kapti.data.Security(iFilter));

       for(com.kapti.data.Security sec : securities){
            Hashtable<String, Object> ex = new Hashtable<String, Object>();
            ex.put("id", sec.getSymbol());
            ex.put("exchange", sec.getExchange());
            ex.put("quote", quoDAO.findLatest(sec.getSymbol()).getPrice());
            ex.put("flags", null);

            result.add(ex);
       }

       return result;
    }

    @Override
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, StockPlayException {
       GenericDAO<com.kapti.data.Security, String> secDAO = getDAO().getSecurityDAO();
       QuoteDAO quoDAO = getDAO().getQuoteDAO();
       Vector<Hashtable<String,Object>> result = new Vector<Hashtable<String,Object>>();
       Collection<com.kapti.data.Security> securities = secDAO.findAll();

       for(com.kapti.data.Security sec : securities){
            Hashtable<String, Object> ex = new Hashtable<String, Object>();
            ex.put("id", sec.getSymbol());
            ex.put("exchange", sec.getExchange());
            ex.put("quote", quoDAO.findLatest(sec.getSymbol()).getPrice());
            ex.put("flags", null);

            result.add(ex);
       }

       return result;
    }

    @Override
    public int Modify(String iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException {
        com.kapti.data.Security sec = new com.kapti.data.Security((String)iDetails.get("id"));

        sec.setExchange((String)iDetails.get("exchange"));
        sec.setName((String)iDetails.get("name"));


        SecurityDAO secDAO = getDAO().getSecurityDAO();


        if(secDAO.findById(sec.getSymbol()) != null)
            return secDAO.update(sec) ? 1: 0;
        else
            return secDAO.create(sec) ? 1: 0;


    }

    @Override
    public int Update(Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException {



        Quote q = new Quote((String) iDetails.get("security"), (Date) iDetails.get("time"));

        q.setPrice((Double) iDetails.get("price"));
        q.setVolume((Integer) iDetails.get("volume"));
        q.setAsk((Double) iDetails.get("ask"));
        q.setBid((Double) iDetails.get("bid"));
        q.setLow((Double) iDetails.get("low"));
        q.setHigh((Double) iDetails.get("high"));
        q.setOpen((Double) iDetails.get("open"));


        QuoteDAO quoteDAO = getDAO().getQuoteDAO();

        if(quoteDAO.create(q))
            return 1;
        else
            return 0;

    }

}

