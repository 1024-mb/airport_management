package com.example.airport_management;

//cd /Users/msajjad/IdeaProjects/airport_management/src/main/java/com/example/airport_management/

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class databaseController {
    public static void getFlights() throws Exception {
        Connection connectDB = DatabaseConnection.connect();

        String connectionQuery = "SELECT Username from UserAccount;";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            System.out.println(queryOutput);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
