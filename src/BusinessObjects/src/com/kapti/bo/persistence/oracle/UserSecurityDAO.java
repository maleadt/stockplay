/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.bo.persistence.oracle;

import com.kapti.bo.exceptions.*;
import com.kapti.bo.*;
import com.kapti.bo.UserSecurity.UserSecurityPK;
import com.kapti.bo.persistence.GenericDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Thijs
 */
public class UserSecurityDAO implements GenericDAO<UserSecurity, UserSecurityPK> {

    private static final String SELECT_USERSECURITY = "SELECT amount FROM usersecurities WHERE userid = ? AND symbol = ?";
    private static final String SELECT_USERSECURITIES_FILTER = "SELECT userid, symbol, amount "
            + "FROM usersecurities WHERE userid LIKE ? AND symbol LIKE ? AND amount LIKE ?";
    private static final String SELECT_USERSECURITIES = "SELECT userid, symbol, amount FROM usersecurities";
    private static final String INSERT_USERSECURITY = "INSERT INTO usersecurties(userid, symbol, amount) "
            + "VALUES(?, ?, ?)";
    private static final String UPDATE_USERSECURITY = "UPDATE usersecurities SET amount = ? WHERE userid = ? AND symbol = ?";
    private static final String DELETE_USERSECURITY = "DELETE FROM usersecurities WHERE userid = ? AND symbol = ?";
    private static UserSecurityDAO instance = new UserSecurityDAO();

    private UserSecurityDAO() {
    }

    public static UserSecurityDAO getInstance() {
        return instance;
    }

    public UserSecurity findById(UserSecurityPK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITY);

                stmt.setInt(1, pk.getUser());
                stmt.setString(2, pk.getSymbol());

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new UserSecurity(pk, rs.getInt(1));
                } else {
                    throw new NonexistentEntityException("There is no usersecurity with userid '" + pk.getUser() + "' and symbol " + pk.getSymbol() + "'");
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
    public Collection<UserSecurity> findByExample(UserSecurity example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITIES_FILTER);

                if (example.getPk().getUser() != 0) {
                    stmt.setString(1, "%" + example.getPk().getUser() + "%");
                } else {
                    stmt.setString(1, "%%");
                }

                stmt.setString(2, '%' + example.getPk().getSymbol() + '%');

                if (example.getAmount() != 0) {
                    stmt.setString(3, "%" + example.getAmount() + "%");
                } else {
                    stmt.setString(3, "%%");
                }

                rs = stmt.executeQuery();
                ArrayList<UserSecurity> list = new ArrayList<UserSecurity>();
                while (rs.next()) {
                    list.add(new UserSecurity(rs.getInt(1), rs.getString(2), rs.getInt(3)));
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
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITIES);

                rs = stmt.executeQuery();
                ArrayList<UserSecurity> list = new ArrayList<UserSecurity>();
                while (rs.next()) {
                    list.add(new UserSecurity(rs.getInt(1), rs.getString(2), rs.getInt(3)));
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
    public boolean create(UserSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_USERSECURITY);

                stmt.setInt(1, entity.getPk().getUser());
                stmt.setString(2, entity.getPk().getSymbol());
                stmt.setInt(3, entity.getAmount());

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
    public boolean update(UserSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_USERSECURITY);

                stmt.setInt(2, entity.getPk().getUser());
                stmt.setString(3, entity.getPk().getSymbol());
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
                stmt.setString(2, entity.getPk().getSymbol());


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
