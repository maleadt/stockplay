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
import com.kapti.data.IndexSecurity.IndexSecurityPK;
import com.kapti.data.Order;
import com.kapti.data.PointsTransaction;
import com.kapti.data.PointsTransaction.PointsTransactionPK;
import com.kapti.data.Transaction;
import com.kapti.data.User;
import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.StockPlayDAO;
import com.kapti.exceptions.DBException;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleStockPlayDAO implements StockPlayDAO {
    // Granted via 'grant select on sys.v_$instance to stockplay'
    private static final String SELECT_UPTIME = "SELECT (sysdate - startup_time)*24*60*60 FROM sys.v_$instance";
    private static final String SELECT_RATE = "SELECT MAX(VALUE) FROM sys.v_$sysmetric WHERE METRIC_NAME = 'User Transaction Per Sec' GROUP BY METRIC_NAME";

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

    public GenericDAO<Index, String> getIndexDAO() {
        return IndexDAO.getInstance();
    }

    public GenericDAO<Order, Integer> getOrderDAO() {
        return OrderDAO.getInstance();
    }

    public GenericDAO<Transaction, Integer> getTransactionDAO() {
       return TransactionDAO.getInstance();
    }

    public GenericDAO<IndexSecurity, IndexSecurityPK> getIndexSecurityDAO() {
        return IndexSecurityDAO.getInstance();
    }

    public GenericDAO<UserSecurity, UserSecurityPK> getUserSecurityDAO() {
        return UserSecurityDAO.getInstance();
    }

    public long getUptime() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_UPTIME);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    long tUptime = rs.getLong(1);
                    return tUptime;
                } else {
                    throw new ServiceException(ServiceException.Type.SERVICE_UNAVAILABLE);
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public double getRate() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_RATE);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    double tRate = rs.getDouble(1);
                    return tRate;
                } else {
                    throw new ServiceException(ServiceException.Type.SERVICE_UNAVAILABLE);
                }
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public GenericDAO<PointsTransaction, PointsTransactionPK> getPointsTransactionDAO() {
        return PointsTransactionDAO.getInstance();
    }

}
