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

package com.kapti.data.persistence;

import com.kapti.data.*;
import com.kapti.exceptions.StockPlayException;

public interface StockPlayDAO {

    public GenericDAO<Exchange, String> getExchangeDAO();
    public GenericDAO<Index, String> getIndexDAO();
    public GenericDAO<Order, Integer> getOrderDAO();
    public GenericQuoteDAO getQuoteDAO();
    public GenericDAO<Security, String> getSecurityDAO();
    public GenericDAO<Transaction, Integer> getTransactionDAO();
    public GenericDAO<User, Integer> getUserDAO();
    public GenericDAO<IndexSecurity, IndexSecurity.IndexSecurityPK> getIndexSecurityDAO();
    public GenericDAO<UserSecurity, UserSecurity.UserSecurityPK> getUserSecurityDAO();
    public GenericPointsTransactionDAO getPointsTransactionDAO();
    public GenericDAO<Role, Integer> getRolesDAO();

    public boolean testConnection();

    public long getUptime() throws StockPlayException;
    public double getRate() throws StockPlayException;
}