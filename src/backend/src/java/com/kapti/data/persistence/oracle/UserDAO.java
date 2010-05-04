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
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserDAO implements GenericDAO<User, Integer> {
    //
    // Member data
    //

    private static final String SELECT_USER_LASTID = "select userid_seq.currval from dual";
    private static final String SELECT_USER = "SELECT nickname, password, email, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash FROM users WHERE id = ?";
    private static final String SELECT_USERS = "SELECT id, nickname, password, email, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash FROM users";
    private static final String INSERT_USER = "INSERT INTO users(id, nickname, password, email, lastname, firstname, is_admin, regtime, rrn, points, startamount, cash) "
            + "VALUES(userid_seq.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE users SET nickname = ?, password = ?, email = ?, lastname = ?, firstname = ?, is_admin = ?, regtime = ?, rrn = ?, points = ?, startamount = ?, cash = ?  WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM users WHERE id = ?";


    //
    // Construction
    //
    
    private static UserDAO instance = new UserDAO();

    private UserDAO() {
    }

    public static UserDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //

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
                    User tUser = new User(id);
                    tUser.setNickname(rs.getString(1));
                    tUser.setEncryptedPassword(rs.getString(2));
                    tUser.setEmail(rs.getString(3));
                    tUser.setLastname(rs.getString(4));
                    tUser.setFirstname(rs.getString(5));
                    tUser.setRole(rs.getInt(6));
                    tUser.setRegdate(rs.getDate(7));
                    tUser.setRijksregisternummer(rs.getLong(8));
                    tUser.setPoints(rs.getInt(9));
                    tUser.setStartamount(rs.getDouble(10));
                    tUser.setCash(rs.getDouble(11));
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
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_USERS);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<User> list = new ArrayList<User>();
                while (rs.next()) {
                    User tUser = new User(rs.getInt(1));
                    tUser.setNickname(rs.getString(2));
                    tUser.setEncryptedPassword(rs.getString(3));
                    tUser.setEmail(rs.getString(4));
                    tUser.setLastname(rs.getString(5));
                    tUser.setFirstname(rs.getString(6));
                    tUser.setRole(rs.getInt(7));
                    tUser.setRegdate(rs.getDate(8));
                    tUser.setRijksregisternummer(rs.getLong(9));
                    tUser.setPoints(rs.getInt(10));
                    tUser.setStartamount(rs.getDouble(11));
                    tUser.setCash(rs.getDouble(12));
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
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven user aan in de database. De Id die werd ingegeven wordt genegeerd, en er wordt automatisch een gegenereerd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het invoegen gelukt is
     * @throws StockPlayException
     */
    public int create(User entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtID = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.prepareStatement(INSERT_USER);

                //stmt.setInt(1, entity.getId()); dit wordt automatisch gegenereerd door de database
                stmt.setString(1, entity.getNickname());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getEmail());
                stmt.setString(4, entity.getLastname());
                stmt.setString(5, entity.getFirstname());
                stmt.setInt(6, entity.getRole().getId());
                stmt.setTimestamp(7, new Timestamp(entity.getRegdate().getTime()));
                stmt.setLong(8, entity.getRijksregisternummer());
                stmt.setInt(9, entity.getPoints());
                stmt.setDouble(10, entity.getStartamount());
                stmt.setDouble(11, entity.getCash());



               if(stmt.executeUpdate() == 1){

                    stmtID = conn.prepareStatement(SELECT_USER_LASTID);

                    rs = stmtID.executeQuery();
                    if(rs.next()){
                        conn.commit();
                        conn.setAutoCommit(true);
                        return rs.getInt(1);

                    }else{
                        return -1;
                    }
                }else{
                    return -1;
                }


            } finally {
                if(rs!= null){
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if(stmtID != null){
                    stmtID.close();
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

                stmt.setInt(12, entity.getId());
                stmt.setString(1, entity.getNickname());
                stmt.setString(2, entity.getPassword());
                stmt.setString(3, entity.getEmail());
                stmt.setString(4, entity.getLastname());
                stmt.setString(5, entity.getFirstname());
                stmt.setInt(6, entity.getRole().getId());
                stmt.setTimestamp(7, new Timestamp(entity.getRegdate().getTime()));
                stmt.setLong(8, entity.getRijksregisternummer());
                stmt.setInt(9, entity.getPoints());
                stmt.setDouble(10, entity.getStartamount());
                stmt.setDouble(11, entity.getCash());

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
