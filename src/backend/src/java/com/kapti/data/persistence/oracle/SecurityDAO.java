/*
 * SecurityDAO.java
 * StockPlay - Abastracte Data access object laag voor de effecten
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

import com.kapti.exceptions.*;
import com.kapti.data.*;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SecurityDAO implements com.kapti.data.persistence.SecurityDAO {

    private static final String SELECT_SECURITY = "SELECT symbol, name, exchange, visible, suspended FROM securities WHERE isin = ?";
    private static final String SELECT_SECURITIES = "SELECT isin, symbol, name, exchange, visible, suspended FROM securities";
    private static final String INSERT_SECURITY = "INSERT INTO securities(isin, symbol, name, exchange, visible, suspended) VALUES(?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SECURITY = "UPDATE securities SET symbol = ?, exchange = ?, name = ?, visible = ?, suspended = ? WHERE isin = ?";
    private static final String DELETE_SECURITY = "DELETE FROM securities WHERE isin = ?";
    private static SecurityDAO instance = new SecurityDAO();

    public static SecurityDAO getInstance() {
        return instance;
    }

    public Security findById(String isin) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SECURITY);

                stmt.setString(1, isin);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    Security tSecurity = new Security(isin, rs.getString(1), rs.getString(3));
                    tSecurity.setName(rs.getString(2));
                    tSecurity.setVisible(rs.getBoolean(4));
                    tSecurity.setSuspended(rs.getBoolean(5));
                    return tSecurity;
                } else {
                    return null;
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

    public Collection<Security> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_SECURITIES);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile());
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<Security> list = new ArrayList<Security>();
                while (rs.next()) {
                    Security tSecurity = new Security(rs.getString(1), rs.getString(2), rs.getString(4));
                    tSecurity.setName(rs.getString(3));
                    tSecurity.setVisible(rs.getBoolean(5));
                    tSecurity.setSuspended(rs.getBoolean(6));
                    list.add(tSecurity);
                }
                return list;
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

    public Collection<Security> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven security aan in de database
     * @param security Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public int create(Security security) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_SECURITY);
                stmt.setString(1, security.getIsin());
                stmt.setString(2, security.getSymbol());
                stmt.setString(3, security.getName());
                if (security.getExchange() != null) {
                    stmt.setString(4, security.getExchange());
                }
                stmt.setBoolean(5, security.isVisible());
                stmt.setBoolean(6, security.isSuspended());
                return stmt.executeUpdate();
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

    /**
     * Maakt de opgegeven security aan in de database
     * @param security Het object dat moet worden aangemaakt in de database
     * @return
     * @throws StockPlayException
     */
    public boolean update(Security security) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_SECURITY);

                stmt.setString(6, security.getIsin());

                stmt.setString(1, security.getSymbol());
                stmt.setString(2, security.getExchange());
                stmt.setString(3, security.getName());
                stmt.setBoolean(4, security.isVisible());
                stmt.setBoolean(5, security.isSuspended());
                return stmt.executeUpdate() == 1;
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

    public boolean delete(Security security) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_SECURITY);

                stmt.setString(1, security.getIsin());

                return stmt.executeUpdate() == 1;


            } finally {
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
}