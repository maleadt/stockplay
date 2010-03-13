package com.kapti.data.persistence.oracle;

import com.kapti.exceptions.*;
import com.kapti.data.*;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import com.kapti.filter.exception.FilterException;
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

                    return new Exchange(symbol, rs.getString(1), rs.getString(2));
                } else {
                    throw new NonexistentEntityException("There is no security with symbol '" + symbol + "'");
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
                while (rs.next())
                    list.add(new Exchange(rs.getString(1), rs.getString(2), rs.getString(3)));
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

    public Collection<Exchange> findByExample(Exchange example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_EXCHANGES_FILTER);

                stmt.setString(1, '%' + example.getSymbol() + '%');
                stmt.setString(2, '%' + example.getName() + '%');
                stmt.setString(3, '%' + example.getLocation() + '%');

                rs = stmt.executeQuery();
                ArrayList<Exchange> list = new ArrayList<Exchange>();
                while (rs.next())
                    list.add(new Exchange(rs.getString(1), rs.getString(2), rs.getString(3)));
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

                    list.add(new Exchange(rs.getString(1), rs.getString(2), rs.getString(3)));
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

    public boolean create(Exchange entity) throws StockPlayException {
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
