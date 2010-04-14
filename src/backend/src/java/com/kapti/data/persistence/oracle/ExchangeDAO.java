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

package com.kapti.data.persistence.oracle;

import com.kapti.exceptions.*;
import com.kapti.data.*;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class ExchangeDAO implements GenericDAO<Exchange, String> {
    private static final String SELECT_EXCHANGE = "SELECT name, location FROM exchanges WHERE symbol = ?";
    private static final String SELECT_EXCHANGES_FILTER = "SELECT symbol, name, location FROM exchanges WHERE symbol LIKE ? AND name LIKE ? AND location LIKE ?";
    private static final String SELECT_EXCHANGES = "SELECT symbol, name, location FROM exchanges";
    private static final String INSERT_EXCHANGE = "INSERT INTO exchanges(symbol, name, location) VALUES(?, ?, ?)";
    private static final String UPDATE_EXCHANGE = "UPDATE exchanges SET name = ?, location = ? WHERE symbol = ?";
    private static final String DELETE_EXCHANGE = "DELETE FROM exchanges WHERE symbol = ?";

    private static ExchangeDAO instance = new ExchangeDAO();

    private  ExchangeDAO(){}

    public static ExchangeDAO getInstance() {
        return instance;
    }
    

    public Exchange findById(String symbol) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_EXCHANGE);

                stmt.setString(1, symbol);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    Exchange tExchange = new Exchange(symbol);
                    tExchange.setName(rs.getString(1));
                    tExchange.setLocation(rs.getString(2));
                    return tExchange;
                } else {
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "There is no security with symbol '" + symbol + "'");
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

    public Collection<Exchange> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        if (iFilter.empty())
            return findAll();

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_EXCHANGES + " WHERE " + (String)iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<Exchange> list = new ArrayList<Exchange>();
                while (rs.next()) {
                    Exchange tExchange = new Exchange(rs.getString(1));
                    tExchange.setName(rs.getString(2));
                    tExchange.setLocation(rs.getString(3));
                    list.add(tExchange);
                }
                return list;
            } finally {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public Collection<Exchange> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_EXCHANGES);

                rs = stmt.executeQuery();
                ArrayList<Exchange> list = new ArrayList<Exchange>();
                while (rs.next()) {
                    Exchange tExchange = new Exchange(rs.getString(1));
                    tExchange.setName(rs.getString(2));
                    tExchange.setLocation(rs.getString(3));
                    list.add(tExchange);
                }
                return list;
            } finally {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
                if (conn != null)
                    conn.close();
            }
        } catch (SQLException ex) {
            throw new DBException(ex);
        }
    }

    public int create(Exchange entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_EXCHANGE);

                stmt.setString(1, entity.getSymbol());
                stmt.setString(2, entity.getName());
                stmt.setString(3, entity.getLocation());

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
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return
     * @throws StockPlayException
     */
    public boolean update(Exchange entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_EXCHANGE);

                stmt.setString(3, entity.getSymbol());
                stmt.setString(1, entity.getName());
                stmt.setString(2, entity.getLocation());

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

    public boolean delete(Exchange entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_EXCHANGE);

                stmt.setString(1, entity.getSymbol());

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
