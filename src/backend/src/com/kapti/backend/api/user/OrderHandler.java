/*
 * OrderHandler.java
 * StockPlay - Handler van de User.Order subklasse.
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
import com.kapti.data.OrderStatus;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.ParserException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.parsing.Parser;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;
/**
 * \brief   Handler van de User.Order subklasse.
 *
 * Deze klasse is de handler van de User.Order subklasse. Ze staat in
 * voor de verwerking van aanroepen van functies die zich in deze klasse
 * bevinden, lokaal de correcte aanvragen uit te voeren, en het resultaat
 * op conforme wijze terug te sturen.
 */
public class OrderHandler extends MethodClass {
    public Vector<Hashtable<String, Object>> List(String iFilter) throws XmlRpcException, StockPlayException, FilterException, ParserException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Order, Integer> orDAO = getDAO().getOrderDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);
        
        // Fetch and convert all orders
        Collection<com.kapti.data.Order> tOrders = orDAO.findByFilter(filter);
        Vector<Hashtable<String, Object>> oVector = new Vector<Hashtable<String, Object>>();
        for (com.kapti.data.Order tOrder : tOrders)
            oVector.add(tOrder.toStruct(
                    com.kapti.data.Order.Fields.ID,
                    com.kapti.data.Order.Fields.TYPE,
                    com.kapti.data.Order.Fields.SECURITY,
                    com.kapti.data.Order.Fields.AMOUNT));

        return oVector;    
    }

    public int Create(Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Order, Integer> orDAO = getDAO().getOrderDAO();

        com.kapti.data.Order tOrder = new com.kapti.data.Order();
        tOrder.fromStruct(iDetails);
        orDAO.create(tOrder);

        return 1;
    }
    
    public int Cancel(String iFilter) throws XmlRpcException, FilterException, StockPlayException, ParserException {
        // Get DAO reference
        GenericDAO<com.kapti.data.Order, Integer> orDAO = getDAO().getOrderDAO();

        Parser parser = Parser.getInstance();
        Filter filter = parser.parse(iFilter);

        // Get the exchanges we need to modify
        Collection<com.kapti.data.Order> tOrders = orDAO.findByFilter(filter);

        // Now apply the cancelation
        for (com.kapti.data.Order tOrder : tOrders) {
            tOrder.setStatus(OrderStatus.CANCELLED);
            orDAO.update(tOrder);
        }

        return 1;
    }    
}