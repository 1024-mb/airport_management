package com.example.airport_management.controller;

import com.example.airport_management.models.DatabaseConnection;
import com.example.airport_management.main;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;


public class databases_entry_controller {
    @FXML public AnchorPane root;
    @FXML public VBox airlines;
    @FXML public VBox tickets;
    @FXML public VBox destinations;

    @FXML public LineChart sales_graph;
    @FXML public CategoryAxis x;
    @FXML public NumberAxis y;

    @FXML public Label avgFlightTime;
    @FXML public Label minFlightTime;
    @FXML public Label maxFlightTime;

    @FXML public Label maxPassengers;
    @FXML public Label avgPassengers;
    @FXML public Label minPassengers;
    @FXML public Button home_button;

    @FXML public PieChart topDestinations;
    @FXML public PieChart topAirlines;
    @FXML public PieChart topTickets;

    @FXML public Button airline;
    @FXML public Button report;
    @FXML public Button flight;
    @FXML public Button plane;

    public void initialize() {
        Set<Node> nodes = root.lookupAll(".arrow_in");

        for (Node node : nodes) {
            FadeTransition fade_shopping = new FadeTransition(Duration.seconds(2), node);
            fade_shopping.setFromValue(1.0);
            fade_shopping.setToValue(0.0);
            fade_shopping.setCycleCount(Animation.INDEFINITE);
            fade_shopping.setAutoReverse(true);
            fade_shopping.play();
        }
    }

    public void home(ActionEvent event) throws IOException {com.example.airport_management.home.home(event);}

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases_entry.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void sales_summary(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/dashboard.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databases_entry_controller controller = loader.getController();

        String connectQuery_destination = "SELECT COUNT(JOURNEY.JourneyID) AS COUNT_JOURNEY, FLIGHT.Destination FROM JOURNEY" +
                                          " INNER JOIN FLIGHT" +
                                          " ON FLIGHT.FlightNumber = JOURNEY.FlightNumber" +
                                          " GROUP BY FLIGHT.Destination" +
                                          " ORDER BY COUNT(JOURNEY.JourneyID) DESC";

        String connectQuery_airline = "SELECT AIRLINE.AirlineName, COUNT(JOURNEY.JourneyID) AS COUNT_JOURNEY FROM JOURNEY" +
                                      " INNER JOIN FLIGHT" +
                                      " ON FLIGHT.FlightNumber = JOURNEY.FlightNumber" +
                                      " INNER JOIN AIRLINE" +
                                      " ON AIRLINE.AirlineID = FLIGHT.AirlineID" +
                                      " GROUP BY AIRLINE.AirlineName" +
                                      " ORDER BY COUNT(JOURNEY.JourneyID) DESC";

        String connectQuery_ticket = "SELECT COUNT(TICKET.TicketID) AS TICKET_COUNT, TICKETTYPE.Type FROM TICKET" +
                                     " INNER JOIN TICKETTYPE" +
                                     " ON TICKETTYPE.TicketTypeID = TICKET.TicketTypeID" +
                                     " GROUP BY TICKETTYPE.Type"+
                                     " ORDER BY COUNT(TICKET.TicketID) DESC";

        String total_payment_day = "SELECT SUM(" +
                                   "\nCASE" +
                                   "\n  WHEN TICKET.TicketTypeID = 1 THEN 100" +
                                   "\n  WHEN TICKET.TicketTypeID = 2 THEN 110" +
                                   "\n  WHEN TICKET.TicketTypeID = 3 THEN 120" +
                                   "\n  WHEN TICKET.TicketTypeID = 4 THEN 140" +
                                   "\nEND" +
                                   ")\nAS TOTAL, DATE(JOURNEY.DepartureDateTime) AS SALES_DAY" +
                                   "\nFROM TICKET" +
                                   "\nINNER JOIN JOURNEY ON JOURNEY.JourneyID = TICKET.JourneyID" +
                                   "\nWHERE JOURNEY.DepartureDateTime >= NOW() - INTERVAL 7 DAY"+
                                   "\nGROUP BY SALES_DAY" +
                                   "\nORDER BY SALES_DAY ASC;";

        String flight_time = "SELECT SUM(FLIGHT.Duration) AS TOTAL, ROUND(AVG(FLIGHT.Duration), 1) AS AVERAGE, MAX(FLIGHT.Duration) AS MAXIMUM, MIN(FLIGHT.Duration) AS MINIMUM" +
                             " FROM FLIGHT" +
                             " INNER JOIN JOURNEY" +
                             " ON JOURNEY.FlightNumber = FLIGHT.FlightNumber" +
                             " WHERE JOURNEY.DepartureDateTime >= NOW() - INTERVAL 7 DAY;";

        String passengers_avg = "SELECT ROUND(AVG(ticket_count)) AS AVERAGE, MAX(ticket_count) AS MAXIMUM, MIN(ticket_count) AS MINIMUM"+
                                " FROM ("+
                                "SELECT COUNT(*) AS ticket_count" +
                                " FROM TICKET" +
                                " INNER JOIN JOURNEY ON JOURNEY.JourneyID = TICKET.JourneyID" +
                                " WHERE JOURNEY.DepartureDateTime >= NOW() - INTERVAL 7 DAY" +
                                " GROUP BY JOURNEY.JourneyID" +
                                ") t;";

        Connection connectDB = DatabaseConnection.connect();


        try {
            Statement statement = connectDB.createStatement();

            ResultSet labelOutput_passengers = statement.executeQuery(passengers_avg);
            labelOutput_passengers.next();
            controller.maxPassengers.setText("Max Passengers: " + labelOutput_passengers.getString("MAXIMUM"));
            controller.avgPassengers.setText("Avg Passengers: " + labelOutput_passengers.getString("AVERAGE"));
            controller.minPassengers.setText("Min Passengers: " + labelOutput_passengers.getString("MINIMUM"));


            ResultSet queryOutput_flight_time = statement.executeQuery(flight_time);
            queryOutput_flight_time.next();
            controller.avgFlightTime.setText(queryOutput_flight_time.getString("AVERAGE") + "h");
            controller.maxFlightTime.setText(queryOutput_flight_time.getString("MAXIMUM") + "h");
            controller.minFlightTime.setText(queryOutput_flight_time.getString("MINIMUM") + "h");

            ObservableList<PieChart.Data> pieData_destinations = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> pieData_airline = FXCollections.observableArrayList();
            ObservableList<PieChart.Data> pieData_tickets = FXCollections.observableArrayList();

            ResultSet queryOutput_destination = statement.executeQuery(connectQuery_destination);

            while(queryOutput_destination.next()) {
                Integer count = queryOutput_destination.getInt("COUNT_JOURNEY");
                String destination = queryOutput_destination.getString("Destination");

                pieData_destinations.add(new PieChart.Data(destination, count));
            }
            controller.topDestinations.setData(pieData_destinations);


            ResultSet queryOutput_airline = statement.executeQuery(connectQuery_airline);

            while(queryOutput_airline.next()) {
                Integer count = queryOutput_airline.getInt("COUNT_JOURNEY");
                String airline = queryOutput_airline.getString("AirlineName");

                pieData_airline.add(new PieChart.Data(airline, count));
            }

            ResultSet queryOutput_tickets = statement.executeQuery(connectQuery_ticket);

            while(queryOutput_tickets.next()) {
                Integer count = queryOutput_tickets.getInt("TICKET_COUNT");
                String ticketType = queryOutput_tickets.getString("Type");

                pieData_tickets.add(new PieChart.Data(ticketType, count));
            }
            controller.topTickets.setData(pieData_tickets);

            controller.topAirlines.setData(pieData_airline);

            controller.topDestinations.setClockwise(true);
            controller.topDestinations.setLabelsVisible(true);
            controller.topDestinations.setLegendVisible(false);

            controller.topAirlines.setClockwise(true);
            controller.topAirlines.setLabelsVisible(true);
            controller.topAirlines.setLegendVisible(false);

            controller.topTickets.setClockwise(true);
            controller.topTickets.setLabelsVisible(true);
            controller.topTickets.setLegendVisible(false);

            ResultSet money = statement.executeQuery(total_payment_day);

            XYChart.Series series = new XYChart.Series();
            series.setName("Revenue");

            controller.x.setLabel("Date");
            controller.y.setLabel("Total Sales /RM");
            controller.x.setAutoRanging(true);
            controller.x.setGapStartAndEnd(true);

            ObservableList<String> categories = FXCollections.observableArrayList();

            while(money.next()) {
                String date = money.getString("SALES_DAY");
                categories.add(date);


                int total = Integer.parseInt(money.getString("TOTAL"));


                series.getData().add(new XYChart.Data<>(date, total));
            }
            controller.x.setCategories(categories);
            controller.sales_graph.getData().addAll(series);



        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void flight_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController();
        controller.displayData("FLIGHT");
    }

    public void journey_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController();
        controller.displayData("JOURNEY");
    }

    public void airline_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController();
        controller.displayData("AIRLINE");
    }

    public void plane_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController();
        controller.displayData("PLANE");
    }

}
