/*
 * Index.java
 * StockPlay - Abstracte klasse van de Finance.Index interface.
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
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import com.kapti.filter.exception.FilterException;
import java.util.Hashtable;
import java.util.Vector;
import org.apache.xmlrpc.XmlRpcException;

/**
 * \brief Abstracte klasse van de Finance.Index interface.
 *
 * Deze klasse voorziet in functiesignaturen zoals voorgeschreven in de
 * protocoldefinitie van de Finance.Index subklasse.
 */
public abstract class Index extends MethodClass {
    //
    // Methodes
    //

    public abstract Vector<Hashtable<String, Object>> List(Filter iFilter) throws XmlRpcException, StockPlayException, FilterException;
    public abstract int Modify(Filter iFilter, Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException, FilterException;
    public abstract int Create(Hashtable<String, Object> iDetails) throws XmlRpcException, StockPlayException;
}