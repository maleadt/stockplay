/*
 * UserDAO.java
 * StockPlay - Abastracte Data access object laag voor de gebruikers
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
                    User tUser = new User(id, rs.getString(1), rs.getString(3), rs.getString(4), rs.getDate(6));
                    tUser.setPassword(rs.getString(2));
                    tUser.setAdmin(rs.getBoolean(5));
                    tUser.setRijksregisternummer(rs.getLong(7));
                    tUser.setPoints(rs.getInt(8));
                    tUser.setStartamount(rs.getDouble(9));
                    tUser.setCash(rs.getDouble(10));
                    return tUser;
                } else {
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "There is no user with id '" + id + "'");
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

    public Collection<User> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        if (iFilter.empty())
            return findAll();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERS + " WHERE " + (String)iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<User> list = new ArrayList<User>();
                while (rs.next()) {
                    User tUser = new User(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getDate(7));
                    tUser.setPassword(rs.getString(3));
                    tUser.setAdmin(rs.getBoolean(6));
                    tUser.setRijksregisternummer(rs.getLong(8));
                    tUser.setPoints(rs.getInt(9));
                    tUser.setStartamount(rs.getDouble(10));
                    tUser.setCash(rs.getDouble(11));
                    list.add(tUser);
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
                //stmt.setTimestamp(6, new Timestamp(example.getRegdate().getTime()));
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
                    User tUser = new User(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getDate(7));
                    tUser.setPassword(rs.getString(3));
                    tUser.setAdmin(rs.getBoolean(6));
                    tUser.setRijksregisternummer(rs.getLong(8));
                    tUser.setPoints(rs.getInt(9));
                    tUser.setStartamount(rs.getDouble(10));
                    tUser.setCash(rs.getDouble(11));
                    list.add(tUser);
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
                    User tUser = new User(rs.getInt(1), rs.getString(2), rs.getString(4), rs.getString(5), rs.getDate(7));
                    tUser.setPassword(rs.getString(3));
                    tUser.setAdmin(rs.getBoolean(6));
                    tUser.setRijksregisternummer(rs.getLong(8));
                    tUser.setPoints(rs.getInt(9));
                    tUser.setStartamount(rs.getDouble(10));
                    tUser.setCash(rs.getDouble(11));
                    list.add(tUser);
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
                stmt.setTimestamp(6, new Timestamp(entity.getRegdate().getTime()));
                stmt.setLong(7, entity.getRijksregisternummer());
                stmt.setInt(8, entity.getPoints());
                stmt.setDouble(9, entity.getStartamount());
                stmt.setDouble(10, entity.getCash());

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
                stmt.setTimestamp(6, new Timestamp(entity.getRegdate().getTime()));
                stmt.setLong(7, entity.getRijksregisternummer());
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
