/*
 * QuoteDAO.java
 * StockPlay - Abastracte Data access object laag voor de prijsnoteringen
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

public class QuoteDAO implements com.kapti.data.persistence.QuoteDAO {

    private static final String SELECT_QUOTE = "SELECT price, volume, bid, ask, low, high, open FROM quotes WHERE symbol = ? AND timestamp = ?";
    private static final String SELECT_QUOTE_FILTER = "SELECT symbol, timestamp, price, volume, bid, ask, low, high, open FROM quotes "
            + "WHERE symbol LIKE ? AND price LIKE ? AND volume LIKE ? AND bid LIKE ? AND ask LIKE ? AND low LIKE ? AND high LIKE ? AND open LIKE ?";
    private static final String SELECT_QUOTES = "SELECT symbol, timestamp, price, volume, bid, ask, low, high, open FROM quotes";
    private static final String INSERT_QUOTE = "INSERT INTO quotes(symbol, timestamp, price, volume, bid, ask, low, high, open) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUOTE = "UPDATE quotes SET price = ?, volume ?, bid = ?, ask = ?, low = ?, high = ?, open ? WHERE symbol = ? AND timestamp = ?";
    private static final String DELETE_QUOTE = "DELETE FROM quotes WHERE symbol = ? AND timestamp = ?";

    private static final String SELECT_LATEST_QUOTE = "SELECT price, volume, bid, ask, low, high, open, timestamp FROM quotes WHERE symbol = ? AND timestamp = max(timestamp) over(partition by symbol)";


    private static QuoteDAO instance = new QuoteDAO();

    public static QuoteDAO getInstance() {
        return instance;
    }

    public Quote findById(Quote.QuotePK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_QUOTE);

                stmt.setString(1, pk.getSecurity());
                stmt.setDate(2, new Date(pk.getTime().getTime()));

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new Quote(pk.getSecurity(), pk.getTime(), rs.getDouble(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7));
                } else {
                    return null;//throw new NonexistentEntityException("There is no quote with symbol '" + pk.getSecurity() + "' and timestamp '" + pk.getTime() + "'");
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

    public Collection<Quote> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        if (iFilter.empty())
            return findAll();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_QUOTES + " WHERE " + (String)iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<Quote> list = new ArrayList<Quote>();
                while (rs.next()) {

                    list.add(new Quote(rs.getString(1), rs.getDate(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8),rs.getDouble(9)));
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

    public Collection<Quote> findByExample(Quote example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_QUOTE_FILTER);

                stmt.setString(1, '%' + example.getSecurity() + '%');
                //stmt.setString(2, '%' + example.getTime().getTime() + '%');
                if (example.getPrice() != 0.0) {
                    stmt.setString(2, "%" + example.getPrice() + "%");
                } else {
                    stmt.setString(2, "%");
                }

                if (example.getVolume() != 0.0) {
                    stmt.setString(3, "%" + example.getVolume() + "%");
                } else {
                    stmt.setString(3, "%");
                }

                if (example.getBid() != 0.0) {
                    stmt.setString(4, "%" + example.getBid() + "%");
                } else {
                    stmt.setString(4, "%");
                }

                if (example.getAsk() != 0.0) {
                    stmt.setString(5, "%" + example.getAsk() + "%");
                } else {
                    stmt.setString(5, "%");
                }

                if (example.getLow() != 0.0) {
                    stmt.setString(6, "%" + example.getLow() + "%");
                } else {
                    stmt.setString(6, "%");
                }

                if (example.getHigh() != 0.0) {
                    stmt.setString(7, "%" + example.getHigh() + "%");
                } else {
                    stmt.setString(7, "%");
                }

                                if (example.getOpen() != 0.0) {
                    stmt.setString(8, "%" + example.getHigh() + "%");
                } else {
                    stmt.setString(8, "%");
                }

                rs = stmt.executeQuery();
                ArrayList<Quote> list = new ArrayList<Quote>();
                while (rs.next()) {

                    list.add(new Quote(rs.getString(1), rs.getDate(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8),rs.getDouble(9)));

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

    public Collection<Quote> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_QUOTES);

                rs = stmt.executeQuery();
                ArrayList<Quote> list = new ArrayList<Quote>();
                while (rs.next()) {

                    list.add(new Quote(rs.getString(1), rs.getDate(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8),rs.getDouble(9)));
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
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public boolean create(Quote entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_QUOTE);

                stmt.setString(1, entity.getSecurity());
                stmt.setDate(2, new Date(entity.getTime().getTime()));
                stmt.setDouble(3, entity.getPrice());
                stmt.setInt(4, entity.getVolume());
                stmt.setDouble(5, entity.getBid());
                stmt.setDouble(6, entity.getAsk());
                stmt.setDouble(7, entity.getLow());
                stmt.setDouble(8, entity.getHigh());
                stmt.setDouble(9, entity.getOpen());

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
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return
     * @throws StockPlayException
     */
    public boolean update(Quote entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_QUOTE);


                stmt.setDouble(1, entity.getPrice());
                stmt.setInt(2, entity.getVolume());
                stmt.setDouble(3, entity.getBid());
                stmt.setDouble(4, entity.getAsk());
                stmt.setDouble(5, entity.getLow());
                stmt.setDouble(6, entity.getHigh());
                stmt.setString(8, entity.getSecurity());
                stmt.setDate(9, new Date(entity.getTime().getTime()));
                stmt.setDouble(7, entity.getOpen());
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

    public boolean delete(Quote entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_QUOTE);

                stmt.setString(1, entity.getSecurity());
                stmt.setDate(2, new Date(entity.getTime().getTime()));

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

        public Quote findLatest(String symbol) throws StockPlayException {

                Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_LATEST_QUOTE);

                stmt.setString(1, symbol);


                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new Quote(symbol, rs.getTimestamp(8), rs.getDouble(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7));
                } else {
                    throw new NonexistentEntityException("There is no quote with symbol '" + symbol + "'");
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
}
