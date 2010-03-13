/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence.oracle;

import com.kapti.exceptions.*;
import com.kapti.data.*;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import com.kapti.filter.exception.FilterException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 *
 * @author Thijs
 */
public class IndexSecurityDAO implements GenericDAO<IndexSecurity, IndexSecurity> {

    private static final String SELECT_USERSECURITY = "SELECT null FROM indexsecurities WHERE indexid = ? AND symbol = ?";
    private static final String SELECT_USERSECURITIES_FILTER = "SELECT indexid, symbol FROM indexsecurities WHERE indexid LIKE ? AND sybmol LIKE ?";
    private static final String SELECT_USERSECURITIES = "SELECT indexid, symbol FROM indexsecurities";
    private static final String INSERT_USERSECURITY = "INSERT INTO indexsecurties(indexid, symbol) VALUES(?, ?, ?)";
    private static final String UPDATE_USERSECURITY = "UPDATE indexsecurities SET null WHERE indexid = ? AND symbol = ?";
    private static final String DELETE_USERSECURITY = "DELETE FROM indexsecurities WHERE indexid = ? AND symbol = ?";
    private static IndexSecurityDAO instance = new IndexSecurityDAO();

    private IndexSecurityDAO() {
    }

    public static IndexSecurityDAO getInstance() {
        return instance;
    }

    public IndexSecurity findById(IndexSecurity pk) throws StockPlayException {


        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITY);

                stmt.setInt(1, pk.getIndex());
                stmt.setString(2, pk.getSymbol());

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new IndexSecurity(pk.getIndex(), pk.getSymbol());
                } else {
                    throw new NonexistentEntityException("There is no indexsecurity with indexid '" + pk.getIndex() + "' and symbol " + pk.getSymbol() + "'");
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

    public Collection<IndexSecurity> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        if (iFilter.empty())
            return findAll();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITIES + " WHERE " + (String)iFilter.compile());

                rs = stmt.executeQuery();
                ArrayList<IndexSecurity> list = new ArrayList<IndexSecurity>();
                while (rs.next()) {
                    list.add(new IndexSecurity(rs.getInt(1), rs.getString(2)));
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
    public Collection<IndexSecurity> findByExample(IndexSecurity example) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITIES_FILTER);

                if (example.getIndex() != 0) {
                    stmt.setString(1, "%" + example.getIndex() + "%");
                } else {
                    stmt.setString(1, "%%");
                }

                stmt.setString(2, '%' + example.getSymbol() + '%');


                rs = stmt.executeQuery();
                ArrayList<IndexSecurity> list = new ArrayList<IndexSecurity>();
                while (rs.next()) {
                    list.add(new IndexSecurity(rs.getInt(1), rs.getString(2)));
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
    public Collection<IndexSecurity> findAll() throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_USERSECURITIES);

                rs = stmt.executeQuery();
                ArrayList<IndexSecurity> list = new ArrayList<IndexSecurity>();
                while (rs.next()) {
                    list.add(new IndexSecurity(rs.getInt(1), rs.getString(2)));
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
    public boolean create(IndexSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_USERSECURITY);

                stmt.setInt(1, entity.getIndex());
                stmt.setString(2, entity.getSymbol());


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
    public boolean update(IndexSecurity entity) throws StockPlayException {

        return false; // er is niets om te updaten!!!

        /*
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_USERSECURITY);

                stmt.setInt(2, entity.getPk().getUser());
                stmt.setString(3, entity.getPk().getSymbol());


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
        }*/
    }

    /**
     * Verwijdert de user met de opgegeven id
     * @param entity Enkel het veld "id" is van belang, de rest mag gewoon leeg zijn.
     * @return True als het verwijderen gelukt is
     * @throws StockPlayException
     */
    public boolean delete(IndexSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_USERSECURITY);

                stmt.setInt(1, entity.getIndex());
                stmt.setString(2, entity.getSymbol());


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
