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

public class RoleDAO implements GenericDAO<Role, Integer> {
    //
    // Member data
    //

    private static final String SELECT_ROLE = "SELECT name, user_remove, security_create, security_modify, security_remove, security_update, transaction_admin, points_admin, backend_admin, database_admin, scraper_admin FROM roles WHERE id = ?";
    private static final String SELECT_ROLES = "SELECT id, name, user_remove, security_create, security_modify, security_remove, security_update, transaction_admin, points_admin, backend_admin, database_admin, scraper_admin FROM roles";
    private static final String INSERT_ROLE = "INSERT INTO roles(id, name, user_remove, security_create, security_modify, security_remove, security_update, transaction_admin, points_admin, backend_admin, database_admin, scraper_admin)"
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_ROLE = "UPDATE roles SET id= ?, name= ?, user_remove= ?, security_create= ?, security_modify= ?, security_remove= ?, security_update= ?, transaction_admin = ?, points_admin = ?, backend_admin= ?, database_admin= ?, scraper_admin = ? WHERE id = ?";
    private static final String DELETE_ROLE = "DELETE FROM roles WHERE id = ?";
    //
    // Construction
    //
    private static RoleDAO instance = new RoleDAO();

    private RoleDAO() {
    }

    public static RoleDAO getInstance() {
        return instance;
    }

    //
    // Methods
    //
    public Role findById(Integer id) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(SELECT_ROLE);

                stmt.setInt(1, id);

                rs = stmt.executeQuery();
                if (rs.next()) {
                    Role tRole = new Role(id);
                    tRole.setName(rs.getString("name"));
                    tRole.setUserRemove(rs.getBoolean("user_remove"));
                    tRole.setSecurityCreate(rs.getBoolean("security_create"));
                    tRole.setSecurityModify(rs.getBoolean("security_modify"));
                    tRole.setSecurityRemove(rs.getBoolean("security_remove"));
                    tRole.setSecurityUpdate(rs.getBoolean("security_update"));
                    tRole.setTransactionAdmin(rs.getBoolean("transaction_admin"));
                    tRole.setPointsAdmin(rs.getBoolean("points_admin"));
                    tRole.setBackendAdmin(rs.getBoolean("backend_admin"));
                    tRole.setDatabaseAdmin(rs.getBoolean("database_admin"));
                    tRole.setScraperAdmin(rs.getBoolean("scraper_admin"));


                    return tRole;
                } else {
                    throw new InvocationException(InvocationException.Type.NON_EXISTING_ENTITY, "There is no role with id '" + id + "'");
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

    public Collection<Role> findByFilter(Filter iFilter) throws StockPlayException, FilterException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();

                StringBuilder tQuery = new StringBuilder(SELECT_ROLES);
                if (!iFilter.empty()) {
                    tQuery.append(" WHERE " + (String) iFilter.compile("sql"));
                }
                stmt = conn.prepareStatement(tQuery.toString());

                rs = stmt.executeQuery();
                ArrayList<Role> list = new ArrayList<Role>();
                while (rs.next()) {
                    Role tRole = new Role(rs.getInt("id"));
                    tRole.setName(rs.getString("name"));
                    tRole.setUserRemove(rs.getBoolean("user_remove"));
                    tRole.setSecurityCreate(rs.getBoolean("security_create"));
                    tRole.setSecurityModify(rs.getBoolean("security_modify"));
                    tRole.setSecurityRemove(rs.getBoolean("security_remove"));
                    tRole.setSecurityUpdate(rs.getBoolean("security_update"));
                    tRole.setTransactionAdmin(rs.getBoolean("transaction_admin"));
                    tRole.setPointsAdmin(rs.getBoolean("points_admin"));
                    tRole.setBackendAdmin(rs.getBoolean("backend_admin"));
                    tRole.setDatabaseAdmin(rs.getBoolean("database_admin"));
                    tRole.setScraperAdmin(rs.getBoolean("scraper_admin"));

                    list.add(tRole);
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

    public Collection<Role> findAll() throws StockPlayException {
        return findByFilter(new Filter());
    }

    /**
     * Maakt de opgegeven index aan in de database. De id van het object wordt genegeerd, en er wordt door de database mbv. een sequence een uniek nummer gecreërd.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return 
     * @throws StockPlayException
     */
    public int create(Role entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement stmtID = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(INSERT_ROLE);

                stmt.setString(2, entity.getName());
                stmt.setBoolean(3, entity.isUserRemove());
                stmt.setBoolean(4, entity.isSecurityCreate());
                stmt.setBoolean(5, entity.isSecurityModify());
                stmt.setBoolean(6, entity.isSecurityRemove());
                stmt.setBoolean(7, entity.isSecurityUpdate());
                stmt.setBoolean(8, entity.isTransactionAdmin());
                stmt.setBoolean(9, entity.isPointsAdmin());
                stmt.setBoolean(10, entity.isBackendAdmin());
                stmt.setBoolean(11, entity.isDatabaseAdmin());
                stmt.setBoolean(12, entity.isScraperAdmin());
                stmt.setInt(1, entity.getId());

                return stmt.executeUpdate();
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (stmt != null) {
                    stmt.close();
                }
                if (stmtID != null) {
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
     * Past de Index met de opgegeven id aan in de database.
     * @param entity Het object dat moet worden aangemaakt in de database
     * @return
     * @throws StockPlayException
     */
    public boolean update(Role entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(UPDATE_ROLE);


                stmt.setString(1, entity.getName());
                stmt.setBoolean(2, entity.isUserRemove());
                stmt.setBoolean(3, entity.isSecurityCreate());
                stmt.setBoolean(4, entity.isSecurityModify());
                stmt.setBoolean(5, entity.isSecurityRemove());
                stmt.setBoolean(6, entity.isSecurityUpdate());
                stmt.setBoolean(7, entity.isTransactionAdmin());
                stmt.setBoolean(8, entity.isPointsAdmin());
                stmt.setBoolean(9, entity.isBackendAdmin());
                stmt.setBoolean(10, entity.isDatabaseAdmin());
                stmt.setBoolean(11, entity.isScraperAdmin());
                stmt.setInt(12, entity.getId());

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
    public boolean delete(Role entity) throws StockPlayException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            try {
                conn = OracleConnection.getConnection();
                stmt = conn.prepareStatement(DELETE_ROLE);

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
