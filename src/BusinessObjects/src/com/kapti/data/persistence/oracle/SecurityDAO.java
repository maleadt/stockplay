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
import com.kapti.filter.exception.FilterException;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class SecurityDAO implements com.kapti.data.persistence.SecurityDAO {

    private static final String SELECT_SECURITY = "SELECT name, exchange, visible, suspended FROM securities WHERE symbol = ?";
    private static final String SELECT_SECURITIES_FILTER = "SELECT symbol, name, exchange, visible, suspended FROM securities WHERE symbol LIKE ? AND name LIKE ? AND exchange LIKE ? AND visible LIKE ? AND suspended LIKE ?";
    private static final String SELECT_SECURITIES = "SELECT symbol, name, exchange, visible, suspended FROM securities";
    private static final String INSERT_SECURITY = "INSERT INTO securities(symbol, name, exchange, visible, suspended) VALUES(?, ?, ?, ?, ?)";
    private static final String UPDATE_SECURITY = "UPDATE securities SET name = ?, visible = ?, suspended = ? WHERE symbol = ? AND exchange = ?";
    private static final String DELETE_SECURITY = "DELETE FROM securities WHERE symbol = ? AND exchange = ?";
    private static SecurityDAO instance = new SecurityDAO();

    public static SecurityDAO getInstance() {
        return instance;
    }

    public Security findById(String symbol) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SECURITY);

                stmt.setString(1, symbol);

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new Security(symbol, rs.getString(1), rs.getString(2), rs.getBoolean(3), rs.getBoolean(4));
                } else {
                    return null; //throw new NonexistentEntityException("There is no security with symbol '" + symbol + "'");
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
        if (iFilter.empty()) {
            return findAll();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SECURITIES + " WHERE " + (String) iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<Security> list = new ArrayList<Security>();
                while (rs.next()) {

                    list.add(new Security(rs.getString(1), rs.getString(2), rs.getString(3)));
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

    public Collection<Security> findByExample(Security example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SECURITIES_FILTER);

                stmt.setString(1, '%' + example.getSymbol() + '%');
                stmt.setString(2, '%' + example.getName() + '%');
                if (example.getExchange() != null) {
                    stmt.setString(3, '%' + example.getExchange() + '%');
                } else {
                    stmt.setString(3, "%%");
                }
                stmt.setBoolean(4, example.isVisible());
                stmt.setBoolean(5, example.isSuspended());

                rs = stmt.executeQuery();
                ArrayList<Security> list = new ArrayList<Security>();
                while (rs.next()) {

                    list.add(new Security(rs.getString(1), rs.getString(2), rs.getString(3)));

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
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SECURITIES);

                rs = stmt.executeQuery();
                ArrayList<Security> list = new ArrayList<Security>();
                while (rs.next()) {

                    list.add(new Security(rs.getString(1), rs.getString(2), rs.getString(3), rs.getBoolean(4), rs.getBoolean(5)));
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

    /**
     * Maakt de opgegeven security aan in de database
     * @param security Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public boolean create(Security security) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_SECURITY);

                stmt.setString(1, security.getSymbol());
                stmt.setString(2, security.getName());
                if (security.getExchange() != null) {
                    stmt.setString(3, security.getExchange());
                }
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

                stmt.setString(4, security.getSymbol());
                stmt.setString(5, security.getExchange());

                stmt.setString(1, security.getName());
                stmt.setBoolean(2, security.isVisible());
                stmt.setBoolean(3, security.isSuspended());
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
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_SECURITY);

                stmt.setString(1, security.getSymbol());
                stmt.setString(2, security.getExchange());

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
}