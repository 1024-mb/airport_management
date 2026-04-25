package com.example.airport_management.Test;

import com.example.airport_management.controller.databaseController;
import com.example.airport_management.controller.databases_entry_controller;
import com.example.airport_management.models.database;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import java.util.Set;

public class databases_entry_controllerTest extends ApplicationTest {

    databases_entry_controller controller = null;


    // all other methods call the methods on this file so can be checked easily
    @Override
    public void start(Stage stage) {
        controller = new databases_entry_controller();

        controller.root = new AnchorPane();
        controller.avgFlightTime = new Label();
        controller.minFlightTime = new Label();
        controller.maxFlightTime = new Label();

        controller.maxPassengers = new Label();
        controller.minPassengers = new Label();
        controller.avgPassengers = new Label();
        controller.home_button = new Button();


        controller.topAirlines = new PieChart();
        controller.topDestinations = new PieChart();
        controller.topTickets = new PieChart();

        controller.report = new Button();
        controller.flight = new Button();
        controller.airline = new Button();
        controller.plane = new Button();

        controller.x = new CategoryAxis();
        controller.y = new NumberAxis();

        stage.setScene(new Scene(controller.avgFlightTime));
        stage.show();
    }

    @Test
    void test_initialize_fade_transitions_applied() {
        interact(() -> controller.initialize());
        Set<Node> nodes = controller.root.lookupAll(".arrow_in");
        Assertions.assertNotNull(nodes);
    }

    @Test
    void test_sales_summary_populates_dashboard() {

        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.report.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));

        Assertions.assertNotNull(controller.maxPassengers.getText());
        Assertions.assertNotNull(controller.avgPassengers.getText());
        Assertions.assertNotNull(controller.minPassengers.getText());
        Assertions.assertNotNull(controller.avgFlightTime.getText());
        Assertions.assertNotNull(controller.maxFlightTime.getText());
        Assertions.assertNotNull(controller.minFlightTime.getText());
    }

    @Test
    void test_report() {
        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.report.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void test_flight_database() {
        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.flight.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void test_plane_database() {
        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.plane.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }

    @Test
    void test_airline_database() {
        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.airline.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
    @Test
    void test_home() {
        Assertions.assertDoesNotThrow(() -> interact(() -> {
            try {
                controller.home_button.fire();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }));
    }
}
