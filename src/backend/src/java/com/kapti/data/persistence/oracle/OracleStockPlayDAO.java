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

package com.kapti.data.persistence.oracle;

import com.kapti.cache.Manager;
import com.kapti.cache.Proxy;
import com.kapti.data.Exchange;
import com.kapti.data.Index;
import com.kapti.data.IndexSecurity;
import com.kapti.data.IndexSecurity.IndexSecurityPK;
import com.kapti.data.Order;
import com.kapti.data.PointsTransaction;
import com.kapti.data.PointsTransaction.PointsTransactionPK;
import com.kapti.data.Role;
import com.kapti.data.Security;
import com.kapti.data.Transaction;
import com.kapti.data.User;
import com.kapti.data.UserSecurity;
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.data.persistence.GenericQuoteDAO;
import com.kapti.data.persistence.StockPlayDAO;
import com.kapti.exceptions.ServiceException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.exceptions.SubsystemException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OracleStockPlayDAO implements StockPlayDAO {
    //
    // Member data
    //

    // Granted via 'grant select on sys.v_$instance to stockplay'
    private static final String SELECT_UPTIME = "SELECT (sysdate - startup_time)*24*60*60 FROM sys.v_$instance";
    private static final String SELECT_RATE = "SELECT MAX(VALUE) FROM sys.v_$sysmetric WHERE METRIC_NAME = 'User Transaction Per Sec' GROUP BY METRIC_NAME";

    // Caches
    
    private final static boolean mCache = true;
    GenericDAO<Exchange, String> mExchangeDAO;
    GenericDAO<Security, String> mSecurityDAO;
    GenericQuoteDAO mQuoteDAO;
    GenericDAO<User, Integer> mUserDAO;
    GenericDAO<Index, String> mIndexDAO;
    GenericDAO<Order, Integer> mOrderDAO;
    GenericDAO<Transaction, Integer> mTransactionDAO;
    GenericDAO<IndexSecurity, IndexSecurityPK> mIndexSecurityDAO;
    GenericDAO<UserSecurity, UserSecurityPK> mUserSecurityDAO;
    GenericDAO<Role, Integer> mRoleDAO;

    public OracleStockPlayDAO() {
        // TODO: deze code hoort ergens anders!

        mExchangeDAO = ExchangeDAO.getInstance();
        mSecurityDAO = SecurityDAO.getInstance();
        mQuoteDAO = QuoteDAO.getInstance();
        mUserDAO = UserDAO.getInstance();
        mIndexDAO = IndexDAO.getInstance();
        mOrderDAO = OrderDAO.getInstance();
        mTransactionDAO = TransactionDAO.getInstance();
        mIndexSecurityDAO = IndexSecurityDAO.getInstance();
        mUserSecurityDAO = UserSecurityDAO.getInstance();
        mRoleDAO = RoleDAO.getInstance();

        if (mCache) {
            mExchangeDAO = (GenericDAO<Exchange, String>) Proxy.newProxyInstance(mExchangeDAO, GenericDAO.class, Manager.getCache("exchanges"));
            mSecurityDAO = (GenericDAO<Security, String>) Proxy.newProxyInstance(mSecurityDAO, GenericDAO.class, Manager.getCache("securities"));
            mQuoteDAO = (GenericQuoteDAO) Proxy.newProxyInstance(mQuoteDAO, GenericQuoteDAO.class, Manager.getCache("quotes"));
            mUserDAO = (GenericDAO<User, Integer>) Proxy.newProxyInstance(mUserDAO, GenericDAO.class, Manager.getCache("users"));
            mIndexDAO = (GenericDAO<Index, String>) Proxy.newProxyInstance(mIndexDAO, GenericDAO.class, Manager.getCache("indexes"));
            mOrderDAO = (GenericDAO<Order, Integer>) Proxy.newProxyInstance(mOrderDAO, GenericDAO.class, Manager.getCache("orders"));
            mTransactionDAO = (GenericDAO<Transaction, Integer>) Proxy.newProxyInstance(mTransactionDAO, GenericDAO.class, Manager.getCache("transactions"));
            mIndexSecurityDAO = (GenericDAO<IndexSecurity, IndexSecurityPK>) Proxy.newProxyInstance(mIndexSecurityDAO, GenericDAO.class, Manager.getCache("indexsecurities"));
            mUserSecurityDAO = (GenericDAO<UserSecurity, UserSecurityPK>) Proxy.newProxyInstance(mUserSecurityDAO, GenericDAO.class, Manager.getCache("usersecurities"));
            mRoleDAO = (GenericDAO<Role, Integer>)Proxy.newProxyInstance(mRoleDAO, GenericDAO.class, Manager.getCache("roles"));
        }
    }

    public GenericDAO<Exchange, String> getExchangeDAO() {
        return mExchangeDAO;
    }

    public GenericDAO<Security, String> getSecurityDAO() {
        return mSecurityDAO;
    }

    public GenericQuoteDAO getQuoteDAO() {
        return mQuoteDAO;
    }

    public GenericDAO<User, Integer> getUserDAO() {
        return mUserDAO;
    }

    public GenericDAO<Index, String> getIndexDAO() {
        return mIndexDAO;
    }

    public GenericDAO<Order, Integer> getOrderDAO() {
        return mOrderDAO;
    }

    public GenericDAO<Transaction, Integer> getTransactionDAO() {
       return mTransactionDAO;
    }

    public GenericDAO<IndexSecurity, IndexSecurityPK> getIndexSecurityDAO() {
        return mIndexSecurityDAO;
    }

    public GenericDAO<UserSecurity, UserSecurityPK> getUserSecurityDAO() {
        return mUserSecurityDAO;
    }

    public GenericDAO<Role, Integer> getRolesDAO() {
        return mRoleDAO;
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }

    public GenericDAO<PointsTransaction, PointsTransactionPK> getPointsTransactionDAO() {
        return PointsTransactionDAO.getInstance();
    }


}
