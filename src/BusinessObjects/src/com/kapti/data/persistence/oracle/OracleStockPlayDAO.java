/*
 * OracleStockPlayDAO.java
 * StockPlay - Oracle implementatie van de Stockplay Data Objects laag
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

package com.kapti.data.persistence.oracle;

import com.kapti.data.Exchange;
import com.kapti.data.Index;
import com.kapti.data.IndexSecurity;
import com.kapti.data.Order;
import com.kapti.data.Transaction;
import com.kapti.data.User;
import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.StockPlayDAO;

public class OracleStockPlayDAO implements StockPlayDAO{


    public GenericDAO<Exchange, String> getExchangeDAO() {
        return ExchangeDAO.getInstance();
    }

    public SecurityDAO getSecurityDAO() {
        return SecurityDAO.getInstance();
    }

    public QuoteDAO getQuoteDAO() {
        return QuoteDAO.getInstance();
    }

    public GenericDAO<User, Integer> getUserDAO() {
        return UserDAO.getInstance();
    }

    public GenericDAO<Index, Integer> getIndexDAO() {
        return IndexDAO.getInstance();
    }

    public GenericDAO<Order, Integer> getOrderDAO() {
        return OrderDAO.getInstance();
    }

    public GenericDAO<Transaction, Integer> getTransactionDAO() {
       return TransactionDAO.getInstance();
    }

    public GenericDAO<IndexSecurity, IndexSecurity> getIndexSecurityDAO() {
        return IndexSecurityDAO.getInstance();
    }

    public GenericDAO<UserSecurity, UserSecurityPK> getUserSecurityDAO() {
        return UserSecurityDAO.getInstance();
    }

}
