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

public class TransactionDAO implements GenericDAO<Transaction, Integer> {
    //
    // Member data
    //

    private static final String SELECT_TRANSACTION_LASTID = "select transactionid_seq.currval from dual";
    private static final String SELECT_TRANSACTION = "SELECT userid, timest, isin, type, amount, price, comments FROM transactions WHERE id = ?";
    private static final String SELECT_TRANSACTIONS = "SELECT id, userid, timest, isin, type, amount, price, comments FROM transactions";
    private static final String INSERT_TRANSACTION = "INSERT INTO transactions(id, userid, timest, isin, type, amount, price, comments) VALUES(transactionid_seq.nextval, ?, ?, ?, ?, ?, ?,?)";
    private static final String UPDATE_TRANSACTION = "UPDATE transactions SET userid = ?, timest = ?, isin = ?, type = ?, amount = ?, price = ?, comments = ? WHERE id = ?";
    private static final String DELETE_TRANSACTION = "DELETE FROM transactions WHERE id = ?";


    //
    // Construction
    //
    
    private static TransactionDAO instance = new TransactionDAO();

    private TransactionDAO() {
    }

    public static TransactionDAO getInstance() {
        return instance;
    }


    //
    // Methods
    //

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
                    Transaction t = new Transaction(id, rs.getInt(1), rs.getString(3));
                    t.setTime(new Date(rs.getTimestamp(2).getTime()));
                    t.setType(InstructionType.valueOf(rs.getString(4).toUpperCase()));
                    t.setAmount(rs.getInt(5));
                    t.setPrice(rs.getDouble(6));
                    t.setComments(rs.getString(7));
                    return t;

                } else {
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "There is no transaction with id '" + id + "'");
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

    public Collection<Transaction> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_TRANSACTIONS);
                if (!iFilter.empty())
                    tQuery.append(" WHERE " + (String)iFilter.compile("sql"));
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<Transaction> list = new ArrayList<Transaction>();
                while (rs.next()) {
                    Transaction t = new Transaction(rs.getInt(1), rs.getInt(2), rs.getString(4));
                    t.setTime(new Date(rs.getTimestamp(3).getTime()));
                    t.setType(InstructionType.valueOf(rs.getString(5).toUpperCase()));
                    t.setAmount(rs.getInt(6));
                    t.setPrice(rs.getDouble(7));
                    t.setComments(rs.getString(8));

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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }

    }

    public Collection<Transaction> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven index aan in de database. De id van het object wordt genegeerd, en er wordt door de database mbv. een sequence een uniek nummer gecreërd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public int create(Transaction entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtID = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                conn.setAutoCommit(false);
                stmt = conn.prepareStatement(INSERT_TRANSACTION);

                stmt.setInt(1, entity.getUser());
                System.out.println("User: " + entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
                System.out.println("Timestamp: " + entity.getTime());
                stmt.setString(3, entity.getIsin());
                System.out.println("Isin: " + entity.getIsin());
                stmt.setString(4, entity.getType().toString());
                System.out.println("Type: " + entity.getType().toString());
                stmt.setInt(5, entity.getAmount());
                System.out.println("Amount: " + entity.getAmount());
                stmt.setDouble(6, entity.getPrice());
                System.out.println("Price: " + entity.getPrice());
                stmt.setString(7, entity.getComments());
                System.out.println("Comments: " + entity.getComments());

               if(stmt.executeUpdate() == 1){

                    stmtID = conn.prepareStatement(SELECT_TRANSACTION_LASTID);

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
                if (rs != null) {
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
            System.out.println(ex.getSQLState());
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
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

                stmt.setInt(8, entity.getId());
                stmt.setInt(1, entity.getUser());
                stmt.setTimestamp(2, new Timestamp(entity.getTime().getTime()));
                stmt.setString(3, entity.getIsin());
                stmt.setString(4, entity.getType().toString());
                stmt.setInt(5, entity.getAmount());
                stmt.setDouble(6, entity.getPrice());
                stmt.setString(7, entity.getComments());
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
            throw new SubsystemException(SubsystemException.Type.DATABASE_FAILURE, ex.getCause());
        }
    }
}
