package com.smcinema.classes;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final String dbUrl = "jdbc:derby://localhost:1527/DB_SMCinema";
    private static final String dbUsername = "tristanRosales";
    private static final String dbPassword = "javalover22";
    private static final String driverName = "org.apache.derby.jdbc.ClientDriver";
    private static Connection con;
    
    public static Connection getConnection(){
        try {
            Class.forName(driverName);
            try {
                con = DriverManager.getConnection(dbUrl, dbUsername,dbPassword);
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return con;
    }
    
}
