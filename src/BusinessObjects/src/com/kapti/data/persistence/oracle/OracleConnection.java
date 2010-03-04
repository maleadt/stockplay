/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kapti.data.persistence.oracle;

import org.apache.commons.dbcp.BasicDataSource;

import com.kapti.exceptions.*;
import java.sql.*;

/**
 *
 * @author Thijs
 */
public class OracleConnection {

    public static BasicDataSource ds = null;

    public static Connection getConnection() throws DBException {
        try {
            if (ds == null) {

                ds = new BasicDataSource();
                ds.setDriverClassName("oracle.jdbc.driver.OracleDriver");

                ds.setUrl("jdbc:oracle:thin:@//oraclepc:1521/xe");
                ds.setUsername("stockplay");
                ds.setPassword("chocolademouse");
            }

            return ds.getConnection();
        } catch (SQLException ex) {
            throw new DBException("Error while creating connection-object", ex);
        }
    }
}
