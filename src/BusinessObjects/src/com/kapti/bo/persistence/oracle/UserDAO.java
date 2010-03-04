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
public class UserDAO implements GenericDAO<User, Integer> {

    private static final String SELECT_USER = "SELECT nickname, password, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash FROM users WHERE id = ?";
    private static final String SELECT_USERS_FILTER = "SELECT id, nickname, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash "
            + "FROM users WHERE id LIKE ? AND nickname LIKE ? AND lastname LIKE ? AND firstname LIKE ? AND is_admin LIKE ? AND regtime LIKE ? AND rrn LIKE ? AND points LIKE ? AND startamount LIKE ? AND cash LIKE ?";
    private static final String SELECT_USERS = "SELECT id, nickname, password, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash FROM users";
    private static final String INSERT_USER = "INSERT INTO users(id, nickname, password, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash) "
            + "VALUES(userid_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET nickname = ?, password = ?, lastname = ?, firstname = ?, is_admin = ?, regtime = ?, rrn = ?, points = ?, startamount = ?, cash = ?  WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";
    private static UserDAO instance = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }

    public User findById(Integer id) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USER);

                stmt.setInt(1, id);

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new User(id, rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getBoolean(5), rs.getDate(6), rs.getInt(7), rs.getInt(8), rs.getDouble(9), rs.getDouble(10));
                } else {
                    throw new NonexistentEntityException("There is no user with id '" + id + "'");
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

    /**
     * Zoekt alle users waarvan de velden lijken op (zoals in: LIKE in SQL) de ingegeven gegevens uit het voorbeeld.
     * vb. Als in het example User-object de nickname "A" is ingevuld, worden alle users waarin hoofdletter A voorkomt teruggegeven
     * @param example
     * @return Collection met User-objecten.
     * @throws StockPlayException Deze exceptie wordt opgeworpen als er een probleem is met de databaseconnectie, of met de query.
     */
    public Collection<User> findByExample(User example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERS_FILTER);

                if (example.getId() != 0) {
                    stmt.setString(1, "%" + example.getId() + "%");
                } else {
                    stmt.setString(1, "%%");
                }

                stmt.setString(2, '%' + example.getNickname() + '%');
                stmt.setString(3, '%' + example.getLastname() + '%');
                stmt.setString(4, '%' + example.getFirstname() + '%');
                stmt.setBoolean(5, example.isAdmin());
                //stmt.setDate(6, new Date(example.getRegdate().getTime()));
                stmt.setString(6, "%%"); //regtime

                if (example.getRijksregisternummer() != 0) {
                    stmt.setString(7, "%" + example.getRijksregisternummer() + "%");
                } else {
                    stmt.setString(7, "%%");
                }

                if (example.getPoints() != 0) {
                    stmt.setString(8, "%" + example.getPoints() + "%");
                } else {
                    stmt.setString(8, "%%");
                }

                if (example.getStartamount() != 0.0) {
                    stmt.setString(9, "%" + example.getStartamount() + "%");
                } else {
                    stmt.setString(9, "%%");
                }

                if (example.getCash() != 0.0) {
                    stmt.setString(10, "%" + example.getCash() + "%");
                } else {
                    stmt.setString(10, "%%");
                }

                rs = stmt.executeQuery();
                ArrayList<User> list = new ArrayList<User>();
                while (rs.next()) {
                    list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getBoolean(6), rs.getDate(7), rs.getInt(8), rs.getInt(9), rs.getDouble(10), rs.getDouble(11)));
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
     * Geeft alle gebruikers in het systeem terug.
     * @return
     * @throws StockPlayException
     */
    public Collection<User> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERS);

                rs = stmt.executeQuery();
                ArrayList<User> list = new ArrayList<User>();
                while (rs.next()) {
                    list.add(new User(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getBoolean(6), rs.getDate(7), rs.getInt(8), rs.getInt(9), rs.getDouble(10), rs.getDouble(11)));
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
     * Maakt de opgegeven user aan in de database. De Id die werd ingegeven wordt genegeerd, en er wordt automatisch een gegenereerd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het invoegen gelukt is
     * @throws StockPlayException
     */
    public boolean create(User entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_USER);

                //stmt.setInt(1, entity.getId()); dit wordt automatisch gegenereerd door de database
                stmt.setString(1, entity.getNickname());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getLastname());
                stmt.setString(4, entity.getFirstname());
                stmt.setBoolean(5, entity.isAdmin());
                stmt.setDate(6, new Date(entity.getRegdate().getTime()));
                stmt.setInt(7, entity.getRijksregisternummer());
                stmt.setInt(8, entity.getPoints());
                stmt.setDouble(9, entity.getStartamount());
                stmt.setDouble(10, entity.getCash());

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
     * Update de gegevens van de gebruiker met de opgegeven primary key
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het updaten gelukt is
     * @throws StockPlayException
     */
    public boolean update(User entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_USER);

                stmt.setInt(11, entity.getId());
                stmt.setString(1, entity.getNickname());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getLastname());
                stmt.setString(4, entity.getFirstname());
                stmt.setBoolean(5, entity.isAdmin());
                stmt.setDate(6, new Date(entity.getRegdate().getTime()));
                stmt.setInt(7, entity.getRijksregisternummer());
                stmt.setInt(8, entity.getPoints());
                stmt.setDouble(9, entity.getStartamount());
                stmt.setDouble(10, entity.getCash());

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
     * Verwijdert de user met de opgegeven id
     * @param entity Enkel het veld "id" is van belang, de rest mag gewoon leeg zijn.
     * @return True als het verwijderen gelukt is
     * @throws StockPlayException
     */
    public boolean delete(User entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_USER);

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
