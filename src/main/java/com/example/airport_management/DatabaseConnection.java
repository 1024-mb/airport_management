package com.example.airport_management;
import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection databaseLink;

    public static Connection connect() throws Exception {
        String url = "jdbc:mysql://localhost:3306/airport_management"; // airport ==> databasename
        String user = "root";
        String password = System.getenv("mysqlpass");


        try {

            databaseLink = DriverManager.getConnection(url, user, password);

        } catch(Exception e) {
            e.printStackTrace();
        }

        return DriverManager.getConnection(url, user, password);
    }
}
