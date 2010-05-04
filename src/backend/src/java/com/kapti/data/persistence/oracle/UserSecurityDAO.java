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
import com.kapti.data.UserSecurity.UserSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class UserSecurityDAO implements GenericDAO<UserSecurity, UserSecurityPK> {
    //
    // Member data
    //

    private static final String SELECT_USERSECURITY = "SELECT amount FROM user_securities WHERE userid = ? AND isin = ?";
    private static final String SELECT_USERSECURITIES = "SELECT userid, isin, amount FROM user_securities";
    private static final String INSERT_USERSECURITY = "INSERT INTO user_securties(userid, isin, amount) VALUES(?, ?, ?)";
    private static final String UPDATE_USERSECURITY = "UPDATE user_securities SET amount = ? WHERE userid = ? AND isin = ?";
    private static final String DELETE_USERSECURITY = "DELETE FROM user_securities WHERE userid = ? AND isin = ?";


    //
    // Construction
    //
    
    private static UserSecurityDAO instance = new UserSecurityDAO();

    private UserSecurityDAO() {
    }

    public static UserSecurityDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //

    public UserSecurity findById(UserSecurityPK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITY);

                stmt.setInt(1, pk.getUser());
                stmt.setString(2, pk.getIsin());

                rs = stmt.executeQuery();
                if (rs.next()) {
                    UserSecurity tSecurity = new UserSecurity(pk.getUser(), pk.getIsin());
                    tSecurity.setAmount(rs.getInt(1));
                    return tSecurity;
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

    public Collection<UserSecurity> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_USERSECURITIES);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<UserSecurity> list = new ArrayList<UserSecurity>();
                while (rs.next()) {
                    UserSecurity tSecurity = new UserSecurity(rs.getInt(1), rs.getString(2));
                    tSecurity.setAmount(rs.getInt(3));
                    list.add(tSecurity);
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
    public Collection<UserSecurity> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven user aan in de database. De Id die werd ingegeven wordt genegeerd, en er wordt automatisch een gegenereerd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het invoegen gelukt is
     * @throws StockPlayException
     */
    public int create(UserSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_USERSECURITY);

                stmt.setInt(1, entity.getPk().getUser());
                stmt.setString(2, entity.getPk().getIsin());
                stmt.setInt(3, entity.getAmount());

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
     * Update de gegevens van de gebruiker met de opgegeven primary key
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het updaten gelukt is
     * @throws StockPlayException
     */
    public boolean update(UserSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_USERSECURITY);

                stmt.setInt(2, entity.getPk().getUser());
                stmt.setString(3, entity.getPk().getIsin());
                stmt.setInt(1, entity.getAmount());

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
    public boolean delete(UserSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_USERSECURITY);

                stmt.setInt(1, entity.getPk().getUser());
                stmt.setString(2, entity.getPk().getIsin());


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
