package com.example.airport_management.Test;

import com.example.airport_management.controller.databaseController;
import com.example.airport_management.models.database;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class validationTest extends ApplicationTest {
    databaseController controller = null;

    @Override
    public void start(Stage stage) {
        controller = new databaseController();

        controller.root = new VBox();

        controller.flightTableView = new TableView<database.Flight>();
        controller.Flight_AirlineID = new TextField();
        controller.Flight_Origin = new TextField();
        controller.Flight_Destination = new TextField();
        controller.Flight_Duration = new TextField();
        controller.Flight_FlightNumber = new TextField();

        controller.journeyTableView = new TableView<database.Journey>();
        controller.Journey_DepartureDateTime = new TextField();
        controller.Journey_DelayedDateTime = new TextField();
        controller.Journey_DepartureGate = new TextField();
        controller.Journey_PlaneID = new TextField();
        controller.Journey_FlightNumber = new TextField();

        controller.planeTableView = new TableView<database.Plane>();
        controller.Plane_PlaneLayout = new TextField();
        controller.Plane_Manufacturer = new TextField();
        controller.Plane_Passengers = new TextField();
        controller.Plane_Model = new TextField();
        controller.Plane_FlightAttendants = new TextField();
        controller.submitButton = new Button();

        controller.airlineTableView = new TableView<database.Airline>();
        controller.Airline_AirlineName = new TextField();

        controller.yesButtonConfirm = new Button();
        controller.noButtonConfirm = new Button();

        controller.OKButton = new Button();

        controller.error_message = new Label();
        controller.error_msg = new Label();

        controller.database_name = new Label();

        stage.setScene(new Scene(controller.Flight_AirlineID));
        stage.show();
    }


    @Test
    void test_invalid_duration() {
        databaseController.validation.live_validation_flight(controller);

        controller.Flight_Duration.setText("25");

        assertEquals("Invalid Duration", controller.error_msg.getText());
        assertTrue(controller.submitButton.isDisabled());
    }

    @Test
    void test_valid_duration() {
        databaseController.validation.live_validation_flight(controller);

        controller.Flight_Duration.setText("10");

        assertEquals("", controller.error_msg.getText());
        assertFalse(controller.submitButton.isDisabled());
    }

    @Test
    void test_invalid_flight_number() {
        databaseController.validation.live_validation_flight(controller);

        controller.Flight_FlightNumber.setText("abc123");

        assertEquals("Invalid Flight Number", controller.error_msg.getText());
        assertTrue(controller.submitButton.isDisabled());
    }

    @Test
    void test_valid_flight_number() {
        databaseController.validation.live_validation_flight(controller);

        controller.Flight_FlightNumber.setText("BA123");

        assertEquals("", controller.error_msg.getText());
    }

    @Test
    void test_invalid_airline_id() {
        databaseController.validation.live_validation_flight(controller);

        controller.Flight_AirlineID.setText("abc");

        assertEquals("Invalid AirlineID", controller.error_msg.getText());
        assertTrue(controller.submitButton.isDisabled());
    }


    @Test
    void test_invalid_gate() {
        databaseController.validation.live_validation_journey(controller);

        controller.Journey_DepartureGate.setText("invalid");

        assertEquals("Invalid Departure Gate", controller.error_msg.getText());
        assertTrue(controller.submitButton.isDisabled());
    }

    @Test
    void test_valid_gate() {
        databaseController.validation.live_validation_journey(controller);

        controller.Journey_DepartureGate.setText("A12");

        assertEquals("", controller.error_msg.getText());
    }

    @Test
    void test_invalid_datetime() {
        databaseController.validation.live_validation_journey(controller);

        controller.Journey_DepartureDateTime.setText("wrong");

        assertEquals("Invalid DateTime", controller.error_msg.getText());
    }

    @Test
    void test_valid_datetime() {
        databaseController.validation.live_validation_journey(controller);

        controller.Journey_DepartureDateTime.setText("2026-04-14 12:30:00");

        assertEquals("", controller.error_msg.getText());
    }

    @Test
    void test_invalid_airline_name() {
        databaseController.validation.live_validation_airline(controller);

        controller.Airline_AirlineName.setText("Airline123");

        assertEquals("Invalid AirlineID", controller.error_msg.getText());
    }

    @Test
    void test_valid_airline_name() {
        databaseController.validation.live_validation_airline(controller);

        controller.Airline_AirlineName.setText("British Airways");

        assertEquals("", controller.error_msg.getText());
    }


    @Test
    void test_invalid_passengers() {
        databaseController.validation.live_validation_plane(controller);

        controller.Plane_Passengers.setText("1000");

        assertEquals("Invalid Number of Passengers.", controller.error_msg.getText());
    }

    @Test
    void test_invalid_attendants() {
        databaseController.validation.live_validation_plane(controller);

        controller.Plane_FlightAttendants.setText("-1");

        assertEquals("Invalid Number of Flight Attendants.", controller.error_msg.getText());
    }


    @Test
    void test_validate_data_airline_valid() {
        controller.Airline_AirlineName.setText("Valid Name");

        boolean result = databaseController.validation.validate_data_airline(controller, null);

        assertTrue(result);
    }


}
