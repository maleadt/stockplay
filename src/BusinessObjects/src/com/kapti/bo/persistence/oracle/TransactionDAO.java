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
public class TransactionDAO implements GenericDAO<Transaction, Integer> {

    private static final String SELECT_TRANSACTION = "SELECT userid, timestamp, symbol, type, amount, price FROM transactions WHERE id = ?";
    private static final String SELECT_TRANSACTIONS_FILTER = "SELECT id, userid, timestamp, symbol, type, amount, price FROM transactions "
            + "WHERE id LIKE ? AND userid LIKE ? AND timestamp LIKE ? AND symbol LIKE ? AND type LIKE ? AND amount LIKE ? AND price LIKE ?";
    private static final String SELECT_TRANSACTIONS = "SELECT id, userid, timestamp, symbol, type, amount, price FROM transactions";
    private static final String INSERT_TRANSACTION = "INSERT INTO transactions(id, userid, timestamp, symbol, type, amount, price) VALUES(transactionid_seq.nextval, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TRANSACTION = "UPDATE transactions SET userid = ?, timestamp = ?, symbol = ?, type = ?, amount = ?, price = ? WHERE id = ?";
    private static final String DELETE_TRANSACTION = "DELETE FROM transactions WHERE id = ?";
    private static TransactionDAO instance = new TransactionDAO();

    private TransactionDAO() {
    }

    public static TransactionDAO getInstance() {
        return instance;
    }

    public Transaction findById(Integer id) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_TRANSACTION);

                stmt.setInt(1, id);

                rs = stmt.executeQuery();
                if (rs.next()) {

                    Transaction t = new Transaction(id);
                    t.setUser(rs.getInt(1));
                    t.setTime(new Date(rs.getTimestamp(2).getTime()));
                    t.setSecurity(rs.getString(3));
                    t.setType(InstructionType.valueOf(rs.getString(4).toUpperCase()));
                    t.setAmount(rs.getInt(5));
                    t.setPrice(rs.getDouble(6));
                    return t;

                } else {
                    throw new NonexistentEntityException("There is no transaction with id '" + id + "'");
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

    public Collection<Transaction> findByExample(Transaction example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_TRANSACTIONS_FILTER);

                stmt.setString(1, "%" + example.getId() + "%");
                stmt.setString(2, "%" + example.getUser() + "%");
                stmt.setString(3, "%" + example.getTime() + "%");
                stmt.setString(4, "%" + example.getSecurity() + "%");
                stmt.setString(5, "%" + example.getType() + "%");
                stmt.setString(6, "%" + example.getAmount() + "%");
                stmt.setString(7, "%" + example.getPrice() + "%");

                rs = stmt.executeQuery();
                ArrayList<Transaction> list = new ArrayList<Transaction>();
                while (rs.next()) {

                    Transaction t = new Transaction(rs.getInt(1));
                    t.setUser(rs.getInt(2));
                    t.setTime(new Date(rs.getTimestamp(3).getTime()));
                    t.setSecurity(rs.getString(4));
                    t.setType(InstructionType.valueOf(rs.getString(5).toUpperCase()));
                    t.setAmount(rs.getInt(6));
                    t.setPrice(rs.getDouble(7));

                    list.add(t);

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

    public Collection<Transaction> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_TRANSACTIONS);

                rs = stmt.executeQuery();
                ArrayList<Transaction> list = new ArrayList<Transaction>();
                while (rs.next()) {
                    Transaction t = new Transaction(rs.getInt(1));
                    t.setUser(rs.getInt(2));
                    t.setTime(new Date(rs.getTimestamp(3).getTime()));
                    t.setSecurity(rs.getString(4));
                    t.setType(InstructionType.valueOf(rs.getString(5).toUpperCase()));
                    t.setAmount(rs.getInt(6));
                    t.setPrice(rs.getDouble(7));

                    list.add(t);
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
     * Maakt de opgegeven index aan in de database. De id van het object wordt genegeerd, en er wordt door de database mbv. een sequence een uniek nummer gecreërd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public boolean create(Transaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_TRANSACTION);

                stmt.setInt(1, entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
                stmt.setString(3, entity.getSecurity());
                stmt.setString(4, entity.getType().toString());
                stmt.setInt(5, entity.getAmount());
                stmt.setDouble(6, entity.getPrice());

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
     * Past de Transaction met de opgegeven id aan in de database.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return
     * @throws StockPlayException
     */
    public boolean update(Transaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_TRANSACTION);

                stmt.setInt(7, entity.getId());
                stmt.setInt(1, entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
                stmt.setString(3, entity.getSecurity());
                stmt.setString(4, entity.getType().toString());
                stmt.setInt(5, entity.getAmount());
                stmt.setDouble(6, entity.getPrice());

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
     * Verwijdert de index met de id van het object uit de database.
     * @param entity Enkel de Id van het object is van belang
     * @return True als het verwijderen van de index gelukt is.
     * @throws StockPlayException
     */
    public boolean delete(Transaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_TRANSACTION);

                stmt.setInt(1, entity.getId());

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
