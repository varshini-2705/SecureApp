package com.app.util;

import javax.servlet.ServletContext;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    
    // SQL statement for table creation
    private static final String CREATE_TABLE_SQL = 
        "CREATE TABLE IF NOT EXISTS users (" +
        " user_id SERIAL PRIMARY KEY, username VARCHAR(50) UNIQUE NOT NULL, " +
        " password_hash VARCHAR(60) NOT NULL, email VARCHAR(100) UNIQUE, " +
        " role VARCHAR(20) NOT NULL DEFAULT 'user', department VARCHAR(50) NOT NULL DEFAULT 'General', " +
        " clearance_level INT DEFAULT 1, last_login_time TIMESTAMP, persistent_token VARCHAR(64));";

    
    public static Connection getConnection(ServletContext context) throws SQLException {
        try {
            String dbUrl = context.getInitParameter("db.url");
            String dbUser = context.getInitParameter("db.user");
            String dbPassword = context.getInitParameter("db.password");

            if (dbUrl == null || dbUser == null || dbPassword == null) {
                throw new SQLException("Database context parameters are not set in web.xml.");
            }

            Class.forName("org.postgresql.Driver");
            return DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver error.", e);
        }
    }

   
    public static void initializeDatabase(ServletContext context) {
        try {
          
            ensureDatabaseExists(context);
           
            try (Connection conn = getConnection(context);
                 Statement stmt = conn.createStatement()) {
                stmt.execute(CREATE_TABLE_SQL);
                System.out.println("Database initialization successful: 'users' table created.");
            }

        } catch (SQLException e) {
            System.err.println("A error occurred during database initialization.");
            e.printStackTrace();
        }
    }

    
    private static void ensureDatabaseExists(ServletContext context) throws SQLException {
        String dbUrl = context.getInitParameter("db.url");
        String dbUser = context.getInitParameter("db.user");
        String dbPassword = context.getInitParameter("db.password");

        
        String dbName = dbUrl.substring(dbUrl.lastIndexOf("/") + 1);
        String serverUrl = dbUrl.substring(0, dbUrl.lastIndexOf("/") + 1) + "postgres";

        try (Connection conn = DriverManager.getConnection(serverUrl, dbUser, dbPassword);
             Statement stmt = conn.createStatement()) {
            
       
            boolean dbExists = false;
            try (ResultSet rs = stmt.executeQuery("SELECT 1 FROM pg_database WHERE datname = '" + dbName + "'")) {
                if (rs.next()) {
                    dbExists = true;
                }
            }

            // If the database does not exist, create it
            if (!dbExists) {
                System.out.println("Database '" + dbName + "' not found. Creating it now...");
                stmt.executeUpdate("CREATE DATABASE " + dbName);
                System.out.println("Database '" + dbName + "' created successfully.");
            } else {
                System.out.println("Database '" + dbName + "' already exists.");
            }
        } catch (SQLException e) {
            System.err.println("Could not check or create the database. Please ensure the user '" + dbUser + "' has CREATEDB permissions in PostgreSQL.");
            throw e; 
        }
    }
}
