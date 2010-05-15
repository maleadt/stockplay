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

import com.kapti.data.PointsTransaction;
import com.kapti.data.PointsTransaction.PointsTransactionPK;
import com.kapti.data.persistence.GenericDAO;
import com.kapti.exceptions.FilterException;
import com.kapti.exceptions.StockPlayException;
import com.kapti.exceptions.SubsystemException;
import com.kapti.filter.Filter;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author Thijs
 */
public class PointsTransactionDAO implements GenericDAO<PointsTransaction, PointsTransaction.PointsTransactionPK> {
    //
    // Member data
    //

    private static final String SELECT_POINTSTRANSACTION = "SELECT delta, comments FROM pointstransactions WHERE userid = ? AND timest = ?";
    private static final String SELECT_POINTSTRANSACTIONS = "SELECT userid, timest, delta, comments FROM pointstransactions";
    private static final String INSERT_POINTSTRANSACTION = "INSERT INTO pointstransactions(userid, timest, delta, comments) "
            + "VALUES(?, ?, ?, ?)";
    private static final String UPDATE_POINTSTRANSACTION = "UPDATE pointstransactions SET delta = ?, comments = ? WHERE userid = ? AND timest = ?";
    private static final String DELETE_POINTSTRANSACTION = "DELETE FROM pointstransactions WHERE userid = ? AND timest = ?";


    //
    // Construction
    //
    
    private static PointsTransactionDAO instance = new PointsTransactionDAO();

    private PointsTransactionDAO() {
    }

    public static PointsTransactionDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //

    public PointsTransaction findById(PointsTransactionPK pk) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_POINTSTRANSACTION);

                stmt.setInt(1, pk.getUser());
                stmt.setTimestamp(2, new Timestamp(pk.getTimestamp().getTime()));


                rs = stmt.executeQuery();
                if (rs.next()) {
                    PointsTransaction tPointsTransaction = new PointsTransaction(pk.getUser(), pk.getTimestamp());
                    tPointsTransaction.setDelta(rs.getInt(1));
                    tPointsTransaction.setComments(rs.getString(2));
                    return tPointsTransaction;
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

    public Collection<PointsTransaction> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_POINTSTRANSACTIONS);
                if (!iFilter.empty()) {
                    tQuery.append(" WHERE " + (String) iFilter.compile("sql"));
                }
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<PointsTransaction> list = new ArrayList<PointsTransaction>();
                while (rs.next()) {
                    PointsTransaction tPointsTransaction = new PointsTransaction(rs.getInt(1), new Date(rs.getTimestamp(2).getTime()));
                    tPointsTransaction.setDelta(rs.getInt(3));
                    tPointsTransaction.setComments(rs.getString(4));
                    list.add(tPointsTransaction);
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
    public Collection<PointsTransaction> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven user aan in de database. De Id die werd ingegeven wordt genegeerd, en er wordt automatisch een gegenereerd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return True als het invoegen gelukt is
     * @throws StockPlayException
     */
    public int create(PointsTransaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_POINTSTRANSACTION);

                stmt.setInt(1, entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTimestamp().getTime()));
                stmt.setInt(3, entity.getDelta());
                stmt.setString(4, entity.getComments());

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
    public boolean update(PointsTransaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_POINTSTRANSACTION);

                stmt.setInt(3, entity.getUser());
                stmt.setTimestamp(4, new Timestamp(entity.getTimestamp().getTime()));
                stmt.setInt(1, entity.getDelta());
                stmt.setString(2, entity.getComments());

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

    /**
     * Verwijdert de user met de opgegeven id
     * @param entity Enkel het veld "id" is van belang, de rest mag gewoon leeg zijn.
     * @return True als het verwijderen gelukt is
     * @throws StockPlayException
     */
    public boolean delete(PointsTransaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_POINTSTRANSACTION);

                stmt.setInt(1, entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTimestamp().getTime()));


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
