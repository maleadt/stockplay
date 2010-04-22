/*
 * QuoteDAO.java
 * StockPlay - Abastracte Data access object laag voor de effecten noteringen
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
package com.kapti.data.persistence;

import com.kapti.data.Quote;
import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.filter.Filter;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public interface QuoteDAO extends GenericDAO<Quote, Quote.QuotePK> {
    boolean createBulk(List<Quote> iQuotes) throws StockPlayException;

    Timestamp getFirstTime(String symbol) throws StockPlayException;
    Timestamp getLatestTime(String symbol) throws StockPlayException;

    Collection<Quote> findLatestByFilter(Filter iFilter) throws StockPlayException, FilterException;
    Collection<Quote> findSpanByFilter(Date iStart, Date iStop, int iSeconds, Filter iFilter) throws StockPlayException, FilterException;
}
