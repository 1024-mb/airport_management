package com.example.airport_management;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class flightDetailController {
    @FXML private ImageView planelayout;
    @FXML private ImageView airlinelogo;
    @FXML private Label journey;
    @FXML private Label flightCode;
    @FXML private Label departure;
    @FXML private Label status;
    @FXML private Label duration;
    @FXML private Label manufacturer;
    @FXML private Label model;
    @FXML private Label passengers;
    @FXML private Label flight_attendants;


    void add_details(Integer JourneyID) throws Exception {
        Connection connectDB = DatabaseConnection.connect();

        String connectionQuery =
                "SELECT AIRLINE.AirlineID, JOURNEY.FlightNumber, FLIGHT.Destination, FLIGHT.Duration, FLIGHT.Origin, JOURNEY.DepartureDateTime, " +
                        "JOURNEY.DelayedDateTime, PLANE.PlaneID, PLANE.Manufacturer, PLANE.Model, PLANE.PlaneLayout, JOURNEY.DepartureGate, PLANE.FlightAttendants, " +
                        "PLANE.Passengers, " +
                        "TIMESTAMPDIFF(HOUR, NOW(), JOURNEY.DepartureDateTime) AS HOURDIFF " +
                        "FROM JOURNEY " +
                        "INNER JOIN FLIGHT ON JOURNEY.FlightNumber = FLIGHT.FlightNumber " +
                        "INNER JOIN AIRLINE ON FLIGHT.AirlineID = AIRLINE.AirlineID " +
                        "INNER JOIN PLANE ON PLANE.PlaneID = JOURNEY.PlaneID " +
                        "WHERE JOURNEY.JourneyID = "  + JourneyID + ";";
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            queryOutput.next();

            String logo = com.example.airport_management.airlineLogo.get_logo(queryOutput.getInt("AirlineID"));

            planelayout.setImage(new Image(queryOutput.getString("PlaneLayout"), 792, 357, false, true));
            airlinelogo.setImage(new Image(main.class.getResource(logo).openStream(), 47, 47, true, true));
            journey.setText(queryOutput.getString("Origin") + "  →  " + queryOutput.getString("Destination"));
            flightCode.setText("Flight " + queryOutput.getString("FlightNumber"));

            if(queryOutput.getString("DelayedDateTime") != null) {
                departure.setText(queryOutput.getString("DelayedDateTime").substring(11, 16));
                departure.setStyle("-fx-text-fill: #9E2A2B;");
            }
            else {
                departure.setText(queryOutput.getString("DepartureDateTime").substring(11, 16));
                departure.setStyle("-fx-text-fill: #81B29A;");
            }

            manufacturer.setText("Manufacturer: " + queryOutput.getString("Manufacturer"));
            model.setText("Model: " + queryOutput.getString("Model"));
            passengers.setText("Passengers: " + queryOutput.getString("Passengers"));
            flight_attendants.setText("Flight Attendant: " + queryOutput.getString("FlightAttendants"));

            flightStatus flightStatus = new flightStatus();
            String stats = flightStatus.getStatus(departure.getText());

            if(stats.equals("Final Call")) {
                status.setText("Final Call");
                status.getStyleClass().add("-fx-text-fill: #9E2A2B;");
            }
            else if(stats.equals("Boarding")) {
                status.setText("Boarding");
                status.getStyleClass().add("-fx-text-fill: #D98E04;");
            }
            else if(stats.equals("Gates Open")) {
                status.setText("Gates Open");
                status.getStyleClass().add("-fx-text-fill: #81B29A;");
            }
            else if(stats.equals("Departed")) {
                status.setText("Departed");
                status.getStyleClass().add("-fx-text-fill: #9E2A2B;");
            }

            if(Integer.parseInt(queryOutput.getString("Duration")) >=1 ) {duration.setText("Duration: "+queryOutput.getString("Duration") + " hr");}
            else {duration.setText("Duration: "+(Integer.parseInt(queryOutput.getString("Duration")) * 60) + " min");}

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    void back(ActionEvent event) throws Exception {
        starterController starterController = new starterController();
        starterController.flights(event);
    }

}
