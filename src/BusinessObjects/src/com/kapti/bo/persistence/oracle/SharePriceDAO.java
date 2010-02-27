/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.bo.persistence.oracle;

import com.kapti.bo.exceptions.*;
import com.kapti.bo.*;
import com.kapti.bo.persistence.GenericDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Thijs
 */
public class SharePriceDAO implements GenericDAO<SharePrice, SharePrice.SharePricePK> {

    private static final String SELECT_SHAREPRICE = "SELECT price, volume, buy, sell, low, high FROM shareprices WHERE symbol = ? AND timestamp = ?";
    private static final String SELECT_SHAREPRICE_FILTER = "SELECT symbol, timestamp, price, volume, buy, sell, low, high  FROM shareprices "
            + "WHERE symbol LIKE ? AND price LIKE ? AND volume LIKE ? AND buy LIKE ? AND sell LIKE ? AND low LIKE ? AND high LIKE ?";
    private static final String SELECT_SHAREPRICES = "SELECT symbol, name, exchange FROM shareprices";
    private static final String INSERT_SHAREPRICE = "INSERT INTO shareprices(symbol, timestamp, price, volume, buy, sell, low, high) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SHAREPRICE = "UPDATE shareprices SET price = ?, volume ?, buy = ?, sell = ?, low = ?, high = ? WHERE symbol = ? AND timestamp = ?";
    private static final String DELETE_SHAREPRICE = "DELETE FROM shareprices WHERE symbol = ? AND timestamp = ?";
    private static SharePriceDAO instance = new SharePriceDAO();
    private static SecurityDAO secDAO = SecurityDAO.getInstance();

    public static SharePriceDAO getInstance() {
        return instance;
    }

    public SharePrice findById(SharePrice.SharePricePK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SHAREPRICE);

                stmt.setString(1, pk.getSecurity().getSymbol());
                stmt.setDate(2, new Date(pk.getTime().getTime()));

                rs = stmt.executeQuery();
                if (rs.next()) {
                    //TODO: exchange ophalen eens geimplementeerd
                    return new SharePrice(pk.getSecurity(), pk.getTime(), rs.getDouble(1), rs.getInt(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5), rs.getDouble(6));
                } else {
                    throw new NonexistentEntityException("There is no shareprice with symbol '" + pk.getSecurity().getSymbol() + "' and timestamp '" + pk.getTime() + "'");
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

    public Collection<SharePrice> findByExample(SharePrice example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SHAREPRICE_FILTER);

                stmt.setString(1, '%' + example.getSecurity().getSymbol() + '%');
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

                if (example.getBuy() != 0.0) {
                    stmt.setString(4, "%" + example.getBuy() + "%");
                } else {
                    stmt.setString(4, "%");
                }

                if (example.getSell() != 0.0) {
                    stmt.setString(5, "%" + example.getSell() + "%");
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

                rs = stmt.executeQuery();
                ArrayList<SharePrice> list = new ArrayList<SharePrice>();
                while (rs.next()) {
                    //TODO: exchange ophalen eens geimplementeerd
                    list.add(new SharePrice(secDAO.findById(rs.getString(1)), rs.getDate(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8)));

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

    public Collection<SharePrice> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_SHAREPRICES);

                rs = stmt.executeQuery();
                ArrayList<SharePrice> list = new ArrayList<SharePrice>();
                while (rs.next()) {
                    //TODO: exchange ophalen eens geimplementeerd
                    list.add(new SharePrice(secDAO.findById(rs.getString(1)), rs.getDate(2), rs.getDouble(3), rs.getInt(4), rs.getDouble(5), rs.getDouble(6), rs.getDouble(7), rs.getDouble(8)));
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
    public boolean create(SharePrice entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_SHAREPRICE);

                stmt.setString(1, entity.getSecurity().getSymbol());
                stmt.setDate(2, new Date(entity.getTime().getTime()));
                stmt.setDouble(3, entity.getPrice());
                stmt.setInt(4, entity.getVolume());
                stmt.setDouble(5, entity.getBuy());
                stmt.setDouble(6, entity.getSell());
                stmt.setDouble(7, entity.getLow());
                stmt.setDouble(8, entity.getHigh());

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
    public boolean update(SharePrice entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_SHAREPRICE);


                stmt.setDouble(1, entity.getPrice());
                stmt.setInt(2, entity.getVolume());
                stmt.setDouble(3, entity.getBuy());
                stmt.setDouble(4, entity.getSell());
                stmt.setDouble(5, entity.getLow());
                stmt.setDouble(6, entity.getHigh());
                stmt.setString(7, entity.getSecurity().getSymbol());
                stmt.setDate(8, new Date(entity.getTime().getTime()));

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

    public boolean delete(SharePrice entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_SHAREPRICE);

                stmt.setString(1, entity.getSecurity().getSymbol());
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
}
