package com.example.connect_db_demo;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class ConnectionClass {
    protected static String db = "demo_db";
    protected static String ip = "10.0.2.2";
    protected static String port = "3306";
    protected static String username = "root";
    protected static String password = "12345";

    public Connection connectDB() {
        Connection con = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String connectionString = "jdbc:mysql://" + ip + ":" + port + "/" + db;
            con = DriverManager.getConnection(connectionString, username, password);

        } catch (Exception e) {
            Log.e("Error Connection", Objects.requireNonNull(e.getMessage()));
        }
        return con;
    }
}
