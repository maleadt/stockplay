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

import com.kapti.exceptions.*;
import com.kapti.data.*;
import com.kapti.data.persistence.GenericQuoteDAO;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QuoteDAO implements GenericQuoteDAO {
    //
    // Member data
    //

    private static final String SELECT_QUOTE = "SELECT price, volume, bid, ask, low, high, open FROM quotes WHERE isin = ? AND timestamp = ?";
    private static final String SELECT_QUOTES = "SELECT isin, timestamp, price, volume, bid, ask, low, high, open FROM quotes";
    private static final String INSERT_QUOTE = "INSERT INTO quotes(isin, timestamp, price, volume, bid, ask, low, high, open) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_QUOTE = "UPDATE quotes SET price = ?, volume ?, bid = ?, ask = ?, low = ?, high = ?, open ? WHERE isin = ? AND timestamp = ?";
    private static final String DELETE_QUOTE = "DELETE FROM quotes WHERE isin = ? AND timestamp = ?";
    private static final String SELECT_LATEST_QUOTES = "with x as (select isin, max(timestamp)  latesttime from quotes group by isin) select isin, timestamp, price, volume, bid, ask, low, high, open from quotes q where timestamp = (select latesttime from x where q.isin=x.isin)";
    private static final String SELECT_LOWEST = "SELECT MIN(PRICE) lowest FROM quotes";
    private static final String SELECT_HIGHEST = "SELECT MAX(PRICE) highest FROM quotes";
    private static final String QUOTE_RANGE = "SELECT MIN(TIMESTAMP) min, MAX(TIMESTAMP) max FROM quotes WHERE isin = ?";
    private static final String SELECT_LATEST_QUOTE_FILTER =    "SELECT  ISIN, TIMESTAMP, PRICE, VOLUME, BID, ASK, LOW, HIGH, OPEN"
                                                                + " FROM ( SELECT  QUOTES.*, MAX(TIMESTAMP) OVER (PARTITION BY ISIN) AS MAX_TIMESTAMP FROM QUOTES ) WHERE TIMESTAMP = MAX_TIMESTAMP AND ( $filter )";
    private static final String SELECT_SPAN_QUOTE =         "select isin, median(timestamp) as timestamp, AVG(price) AS price, MAX(volume) AS volume, AVG(bid) AS bid, AVG(ask) AS ask, MIN(low) AS low, MAX(high) AS high, open"
                                                           + " from ( SELECT q.*, time_diff(?, TIMESTAMP) as diff, time_diff(?, min(TIMESTAMP) over(order by timestamp asc)) as diff_start, time_diff(?, max(TIMESTAMP) over(order by timestamp desc)) as diff_end from quotes q where (TIMESTAMP BETWEEN ? AND ?) ) s"
                                                           + " GROUP BY isin, trunc(? * (diff - diff_start) / (diff_end - diff_start)), open ORDER BY timestamp";
    private static final String SELECT_SPAN_QUOTE_FILTER = "select isin, median(timestamp) as timestamp, AVG(price) AS price, MAX(volume) AS volume, AVG(bid) AS bid, AVG(ask) AS ask, MIN(low) AS low, MAX(high) AS high, open"
                                                           + " from ( SELECT q.*, time_diff(?, TIMESTAMP) as diff, time_diff(?, min(TIMESTAMP) over(order by timestamp asc)) as diff_start, time_diff(?, max(TIMESTAMP) over(order by timestamp desc)) as diff_end from quotes q where (TIMESTAMP BETWEEN ? AND ?) AND ($filter) ) s"
                                                           + " GROUP BY isin, trunc(? * (diff - diff_start) / (diff_end - diff_start)), open ORDER BY timestamp";
    
    //  CREATE OR REPLACE FUNCTION time_diff (
    //DATE_1 IN DATE, DATE_2 IN DATE) RETURN NUMBER IS
    //
    //NDATE_1   NUMBER;
    //NDATE_2   NUMBER;
    //NSECOND_1 NUMBER(5,0);
    //NSECOND_2 NUMBER(5,0);
    //
    //BEGIN
    //  -- Get Julian date number from first date (DATE_1)
    //  NDATE_1 := TO_NUMBER(TO_CHAR(DATE_1, 'J'));
    //
    //  -- Get Julian date number from second date (DATE_2)
    //  NDATE_2 := TO_NUMBER(TO_CHAR(DATE_2, 'J'));
    //
    //  -- Get seconds since midnight from first date (DATE_1)
    //  NSECOND_1 := TO_NUMBER(TO_CHAR(DATE_1, 'SSSSS'));
    //
    //  -- Get seconds since midnight from second date (DATE_2)
    //  NSECOND_2 := TO_NUMBER(TO_CHAR(DATE_2, 'SSSSS'));
    //
    //  RETURN (((NDATE_2 - NDATE_1) * 86400)+(NSECOND_2 - NSECOND_1));
    //END time_diff;
    ///


    //
    // Construction
    //
    
    private static GenericQuoteDAO instance = new QuoteDAO();

    private QuoteDAO() {
    }

    public static GenericQuoteDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //

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
                    tQuote.setPrice(rs.getDouble("price"));
                    tQuote.setVolume(rs.getInt("volume"));
                    tQuote.setBid(rs.getDouble("bid"));
                    tQuote.setAsk(rs.getDouble("ask"));
                    tQuote.setLow(rs.getDouble("low"));
                    tQuote.setHigh(rs.getDouble("high"));
                    tQuote.setOpen(rs.getDouble("open"));
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }

    public Collection<Quote> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_QUOTES);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<Quote> list = new ArrayList<Quote>();
                while (rs.next()) {
                    Quote tQuote = new Quote(rs.getString("isin"), rs.getTimestamp("timestamp"));
                    tQuote.setPrice(rs.getDouble("price"));
                    tQuote.setVolume(rs.getInt("volume"));
                    tQuote.setBid(rs.getDouble("bid"));
                    tQuote.setAsk(rs.getDouble("ask"));
                    tQuote.setLow(rs.getDouble("low"));
                    tQuote.setHigh(rs.getDouble("high"));
                    tQuote.setOpen(rs.getDouble("open"));
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }

    }

    public Collection<Quote> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven security aan in de database
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public int create(Quote entity) throws StockPlayException {
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }

    public List<Timestamp> getRange(String isin) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(QUOTE_RANGE);

                stmt.setString(1, isin);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    List<Timestamp> tRange = new ArrayList<Timestamp>();
                    tRange.add(rs.getTimestamp("min"));
                    tRange.add(rs.getTimestamp("max"));
                    return tRange;
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }

    public double getLowest(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_LOWEST);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("lowest");
                } else {
                    return 0;
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


    public double getHighest(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_HIGHEST);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                if (rs.next()) {
                    return rs.getDouble("highest");
                } else {
                    return 0;
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

    public Collection<Quote> findLatestByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                if(!iFilter.empty())
                    stmt = conn.prepareStatement(SELECT_LATEST_QUOTE_FILTER.replace("$filter", (String) iFilter.compile("sql")));
                else
                    stmt = conn.prepareStatement(SELECT_LATEST_QUOTES);
                rs = stmt.executeQuery();

                ArrayList<Quote> result = new ArrayList<Quote>();
                while (rs.next()) {
                    Quote tQuote = new Quote(rs.getString("isin"), rs.getTimestamp("timestamp"));
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }

    public Collection<Quote> findSpanByFilter(java.util.Date iStart, java.util.Date iStop, int iSpan, Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                if(!iFilter.empty())
                    stmt = conn.prepareStatement(SELECT_SPAN_QUOTE_FILTER.replace("$filter", (String) iFilter.compile("sql")));
                else
                    stmt = conn.prepareStatement(SELECT_SPAN_QUOTE);
                    
                stmt.setTimestamp(1, new Timestamp(iStart.getTime()));
                stmt.setTimestamp(2, new Timestamp(iStart.getTime()));
                stmt.setTimestamp(3, new Timestamp(iStart.getTime()));
                stmt.setTimestamp(4, new Timestamp(iStart.getTime()));
                stmt.setTimestamp(5, new Timestamp(iStop.getTime()));
                stmt.setInt(6, iSpan-1);

                rs = stmt.executeQuery();

                ArrayList<Quote> result = new ArrayList<Quote>();
                while (rs.next()) {
                    Quote tQuote = new Quote(rs.getString("isin"), rs.getTimestamp("timestamp"));
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }
}
