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
import com.kapti.data.IndexSecurity.IndexSecurityPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.filter.Filter;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class IndexSecurityDAO implements GenericDAO<IndexSecurity, IndexSecurityPK> {
    //
    // Member data
    //

    private static final String SELECT_INDEXSECURITY = "SELECT null FROM index_securities WHERE index_isin = ? AND security_isin = ?";
    private static final String SELECT_INDEXSECURITIES = "SELECT index_isin, security_isin FROM index_securities";
    private static final String INSERT_INDEXSECURITY = "INSERT INTO index_securities(index_isin, security_isin) VALUES(?, ?)";
    private static final String DELETE_INDEXSECURITY = "DELETE FROM index_securities WHERE index_isin = ? AND security_isin = ?";


    //
    // Construction
    //
    
    private static IndexSecurityDAO instance = new IndexSecurityDAO();

    private IndexSecurityDAO() {
    }

    public static IndexSecurityDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //


    public IndexSecurity findById(IndexSecurityPK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_INDEXSECURITY);

                stmt.setString(1, pk.getIndexIsin());
                stmt.setString(2, pk.getSecurityIsin());

                rs = stmt.executeQuery();
                if (rs.next()) {

                    return new IndexSecurity(pk.getIndexIsin(), pk.getSecurityIsin());
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

    public Collection<IndexSecurity> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_INDEXSECURITIES);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<IndexSecurity> list = new ArrayList<IndexSecurity>();
                while (rs.next()) {
                    list.add(new IndexSecurity(rs.getString("index_isin"), rs.getString("security_isin")));
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
    /**
     * Geeft alle gebruikers in het systeem terug.
     * @return
     * @throws StockPlayException
     */
    public Collection<IndexSecurity> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven user aan in de database. De Id die werd ingegeven wordt genegeerd, en er wordt automatisch een gegenereerd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het invoegen gelukt is
     * @throws StockPlayException
     */
    public int create(IndexSecurity entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_INDEXSECURITY);

                stmt.setString(1, entity.getPk().getIndexIsin());
                stmt.setString(2, entity.getPk().getSecurityIsin());


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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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
                stmt = conn.prepareStatement(DELETE_INDEXSECURITY);

                stmt.setString(1, entity.getPk().getIndexIsin());
                stmt.setString(2, entity.getPk().getSecurityIsin());


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
}
