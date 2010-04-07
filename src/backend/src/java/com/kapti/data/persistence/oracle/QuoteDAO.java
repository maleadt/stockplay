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
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuoteDAO implements com.kapti.data.persistence.QuoteDAO {

    private static final String SELECT_QUOTE = "SELECT price, volume, bid, ask, low, high, open FROM quotes WHERE isin = ? AND timestamp = ?";
    private static final String SELECT_QUOTE_FILTER = "SELECT isin, timestamp, price, volume, bid, ask, low, high, open FROM quotes "
            + "WHERE isin LIKE ? AND price LIKE ? AND volume LIKE ? AND bid LIKE ? AND ask LIKE ? AND low LIKE ? AND high LIKE ? AND open LIKE ?";
    private static final String SELECT_QUOTES = "SELECT isin, timestamp, price, volume, bid, ask, low, high, open FROM quotes";
    private static final String INSERT_QUOTE = "INSERT INTO quotes(isin, timestamp, price, volume, bid, ask, low, high, open) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUOTE = "UPDATE quotes SET price = ?, volume ?, bid = ?, ask = ?, low = ?, high = ?, open ? WHERE isin = ? AND timestamp = ?";
    private static final String DELETE_QUOTE = "DELETE FROM quotes WHERE isin = ? AND timestamp = ?";
    private static final String SELECT_LATEST_QUOTE = "SELECT price, volume, bid, ask, low, high, open, timestamp FROM quotes WHERE isin = ? AND timestamp =(select max(timestamp) from quotes WHERE isin= ? )";
    private static final String SELECT_LATEST_QUOTE_FILTER = "SELECT isin, max(price) price, max(volume) volume, max(bid) bid, max(ask) ask, max(low) low, max(high) high, max(open) open, max(timestamp) timestamp "
            + "FROM quotes q WHERE FILTER and timestamp = (select max(timestamp) from quotes where isin = q.isin ) GROUP BY isin";
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

                stmt.setString(1, pk.getIsin());
                stmt.setTimestamp(2, new Timestamp(pk.getTime().getTime()));

                rs = stmt.executeQuery();
                if (rs.next()) {
                    Quote tQuote = new Quote(pk.getIsin(), pk.getTime());
                    tQuote.setPrice(rs.getDouble(1));
                    tQuote.setVolume(rs.getInt(2));
                    tQuote.setBid(rs.getDouble(3));
                    tQuote.setAsk(rs.getDouble(4));
                    tQuote.setLow(rs.getDouble(5));
                    tQuote.setHigh(rs.getDouble(6));
                    tQuote.setOpen(rs.getDouble(7));
                    return tQuote;
                } else {
                    return null;//throw new NonexistentEntityException("There is no quote with isin '" + pk.getSecurity() + "' and timestamp '" + pk.getTime() + "'");
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
        if (iFilter.empty()) {
            return findAll();
        }
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_QUOTES + " WHERE " + (String) iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<Quote> list = new ArrayList<Quote>();
                while (rs.next()) {
                    Quote tQuote = new Quote(rs.getString(1), rs.getDate(2));
                    tQuote.setPrice(rs.getDouble(3));
                    tQuote.setVolume(rs.getInt(4));
                    tQuote.setBid(rs.getDouble(5));
                    tQuote.setAsk(rs.getDouble(6));
                    tQuote.setLow(rs.getDouble(7));
                    tQuote.setHigh(rs.getDouble(8));
                    tQuote.setOpen(rs.getDouble(9));
                    list.add(tQuote);
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

                stmt.setString(1, '%' + example.getIsin() + '%');
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
                    Quote tQuote = new Quote(rs.getString(1), rs.getDate(2));
                    tQuote.setPrice(rs.getDouble(3));
                    tQuote.setVolume(rs.getInt(4));
                    tQuote.setBid(rs.getDouble(5));
                    tQuote.setAsk(rs.getDouble(6));
                    tQuote.setLow(rs.getDouble(7));
                    tQuote.setHigh(rs.getDouble(8));
                    tQuote.setOpen(rs.getDouble(9));
                    list.add(tQuote);
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
                    Quote tQuote = new Quote(rs.getString(1), rs.getDate(2));
                    tQuote.setPrice(rs.getDouble(3));
                    tQuote.setVolume(rs.getInt(4));
                    tQuote.setBid(rs.getDouble(5));
                    tQuote.setAsk(rs.getDouble(6));
                    tQuote.setLow(rs.getDouble(7));
                    tQuote.setHigh(rs.getDouble(8));
                    tQuote.setOpen(rs.getDouble(9));
                    list.add(tQuote);
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
                
                stmt.setString(1, entity.getIsin());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
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
    public boolean createBulk(List<Quote> iEntities) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_QUOTE);
                conn.setAutoCommit(false);

                for (Quote entity : iEntities) {
                    stmt.setString(1, entity.getIsin());
                    stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
                    stmt.setDouble(3, entity.getPrice());
                    stmt.setInt(4, entity.getVolume());
                    stmt.setDouble(5, entity.getBid());
                    stmt.setDouble(6, entity.getAsk());
                    stmt.setDouble(7, entity.getLow());
                    stmt.setDouble(8, entity.getHigh());
                    stmt.setDouble(9, entity.getOpen());
                    stmt.addBatch();
                }

                int [] numUpdates = stmt.executeBatch();
                conn.commit();
                for (int i = 0; i < numUpdates.length; i++) {
                    if (numUpdates[i] != 1)
                        return false;
                }
                return true;


            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (conn != null) {
                    conn.setAutoCommit(true);
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
                stmt.setString(8, entity.getIsin());
                stmt.setTimestamp(9, new Timestamp(entity.getTime().getTime()));
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

                stmt.setString(1, entity.getIsin());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));

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

    public Quote findLatest(String isin) throws StockPlayException {

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_LATEST_QUOTE);

                stmt.setString(1, isin);


                rs = stmt.executeQuery();
                if (rs.next()) {
                    Quote tQuote = new Quote(isin, rs.getDate(8));
                    tQuote.setPrice(rs.getDouble(1));
                    tQuote.setVolume(rs.getInt(2));
                    tQuote.setBid(rs.getDouble(3));
                    tQuote.setAsk(rs.getDouble(4));
                    tQuote.setLow(rs.getDouble(5));
                    tQuote.setHigh(rs.getDouble(6));
                    tQuote.setOpen(rs.getDouble(7));
                    return tQuote;
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

    public Collection<Quote> findLatestByFilter(Filter iFilter) throws StockPlayException, FilterException {


        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_LATEST_QUOTE_FILTER.replace("FILTER", (String) iFilter.compile()));


                rs = stmt.executeQuery();

                ArrayList<Quote> result = new ArrayList<Quote>();
                while (rs.next()) {
                    Quote tQuote = new Quote(rs.getString("isin"), rs.getDate("timestamp"));
                    tQuote.setPrice(rs.getDouble("price"));
                    tQuote.setVolume(rs.getInt("volume"));
                    tQuote.setBid(rs.getDouble("bid"));
                    tQuote.setAsk(rs.getDouble("ask"));
                    tQuote.setLow(rs.getDouble("low"));
                    tQuote.setHigh(rs.getDouble("high"));
                    tQuote.setOpen(rs.getDouble("open"));
                    result.add(tQuote);
                }
                return result;
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
