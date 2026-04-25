package com.example.airport_management.Test;

import com.example.airport_management.controller.flightDetailController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.testfx.framework.junit5.ApplicationTest;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

public class flightDetailControllerTest extends ApplicationTest {

    private flightDetailController controller;

    private MockedStatic<com.example.airport_management.models.DatabaseConnection> dbMock;
    private MockedStatic<com.example.airport_management.utilities.airlineLogo> logoMock;

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;

    @Override
    public void start(Stage stage) {
        controller = new flightDetailController();

        controller.planelayout = new ImageView();
        controller.airlinelogo = new ImageView();
        controller.back = new Button();

        controller.journey = new Label();
        controller.flightCode = new Label();
        controller.departure = new Label();
        controller.status = new Label();
        controller.duration = new Label();
        controller.manufacturer = new Label();
        controller.model = new Label();
        controller.passengers = new Label();
        controller.flight_attendants = new Label();

        stage.setScene(new Scene(controller.journey));
        stage.show();
    }

    @BeforeEach
    void setUp() throws Exception {

        // simulates the database connection as well as other slow-to-stimulate things
        conn = mock(Connection.class);
        stmt = mock(Statement.class);
        rs = mock(ResultSet.class);

        dbMock = Mockito.mockStatic(com.example.airport_management.models.DatabaseConnection.class);
        dbMock.when(com.example.airport_management.models.DatabaseConnection::connect)
                .thenReturn(conn);

        logoMock = Mockito.mockStatic(com.example.airport_management.utilities.airlineLogo.class);

        when(conn.createStatement()).thenReturn(stmt);
        when(stmt.executeQuery(anyString())).thenReturn(rs);

        // Fake DB row
        when(rs.next()).thenReturn(true);

        when(rs.getInt("AirlineID")).thenReturn(1);
        when(rs.getString("PlaneLayout")).thenReturn("file:test.png");
        when(rs.getString("Origin")).thenReturn("London");
        when(rs.getString("Destination")).thenReturn("Kuala Lumpur");
        when(rs.getString("FlightNumber")).thenReturn("BA249");
        when(rs.getString("DepartureDateTime")).thenReturn("2026-04-14 10:30:00");
        when(rs.getString("DelayedDateTime")).thenReturn(null);
        when(rs.getString("Manufacturer")).thenReturn("Boeing");
        when(rs.getString("Model")).thenReturn("777");
        when(rs.getString("Passengers")).thenReturn("300");
        when(rs.getString("FlightAttendants")).thenReturn("12");
        when(rs.getString("Duration")).thenReturn("13");


        logoMock.when(() -> com.example.airport_management.utilities.airlineLogo.get_logo(anyInt()))
                .thenReturn("/logo.png");
    }

    @AfterEach
    void tearDown() {
        dbMock.close();
        logoMock.close();
    }

    @Test
    void testAddDetails_populatesLabelsCorrectly() throws Exception {
        interact(() -> {
            try {
                controller.add_details(2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Assertions.assertEquals("Kuala Lumpur International  →  Singapore Changi", controller.journey.getText());
        Assertions.assertEquals("Flight AK701", controller.flightCode.getText());
        Assertions.assertEquals("Manufacturer: Boeing", controller.manufacturer.getText());
        Assertions.assertEquals("Model: 737-MAX", controller.model.getText());
        Assertions.assertEquals("Passengers: 200", controller.passengers.getText());
        Assertions.assertEquals("Flight Attendant: 10", controller.flight_attendants.getText());
        Assertions.assertEquals("Duration: 1 hr", controller.duration.getText());
    }

    @Test
    void testDeparture_whenNoDelay_usesDepartureTime() throws Exception {

        interact(() -> {
            try {
                controller.add_details(2);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // checks the time matches ---> delayed time is used instead of the departure time, if there is a delay
        Assertions.assertEquals("05:55", controller.departure.getText());
    }

    @Test
    void test_back_does_not_throw() {
        assertDoesNotThrow(() -> controller.back.fire());
    }
}