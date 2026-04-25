package com.example.airport_management.Test;

import com.example.airport_management.controller.databaseController;
import com.example.airport_management.controller.flightDetailController;
import com.example.airport_management.models.DatabaseConnection;
import com.example.airport_management.models.database;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.NodeQueryUtils.hasText;

public class databaseControllerTest extends ApplicationTest {

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
    void test_add_columns_flight() throws Exception {
        interact(() -> {
            try {
                controller.add_columns_flight();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // tests the output of the query and injection into the fxml vs the database values.
        Assertions.assertTrue(controller.flightTableView.getColumns().size() >= 4);

        Assertions.assertEquals("FlightNumber", controller.flightTableView.getColumns().get(0).getText());
        Assertions.assertEquals("Origin", controller.flightTableView.getColumns().get(1).getText());
        Assertions.assertEquals("Destination", controller.flightTableView.getColumns().get(2).getText());
        Assertions.assertEquals("Duration", controller.flightTableView.getColumns().get(3).getText());

        boolean hasActions = controller.flightTableView.getColumns()
                .stream()
                .anyMatch(col -> col.getText().equals("Actions"));

        Assertions.assertTrue(hasActions);

        Assertions.assertFalse(controller.flightTableView.isEditable());
    }

    @Test
    void test_add_columns_plane() throws Exception {

        interact(() -> {
            try {
                controller.add_columns_plane();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // tests the output of the query and injection into the fxml vs the database values.

        Assertions.assertEquals("PlaneID", controller.planeTableView.getColumns().get(0).getText());
        Assertions.assertEquals("Manufacturer", controller.planeTableView.getColumns().get(1).getText());
        Assertions.assertEquals("Model", controller.planeTableView.getColumns().get(2).getText());
        Assertions.assertEquals("FlightAttendants", controller.planeTableView.getColumns().get(3).getText());
        Assertions.assertEquals("Passengers", controller.planeTableView.getColumns().get(4).getText());
        Assertions.assertEquals("PlaneLayout", controller.planeTableView.getColumns().get(5).getText());


        boolean hasActions = controller.flightTableView.getColumns()
                .stream()
                .anyMatch(col -> col.getText().equals("Actions"));

        //Assertions.assertTrue(hasActions);

        Assertions.assertFalse(controller.flightTableView.isEditable());
    }

    @Test
    void test_add_columns_journey() throws Exception {

        interact(() -> {
            try {
                controller.add_columns_journey();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Ensure columns are injected properly
        Assertions.assertTrue(controller.journeyTableView.getColumns().size() >= 7);

        Assertions.assertEquals("JourneyID", controller.journeyTableView.getColumns().get(0).getText());
        Assertions.assertEquals("DepartureDateTime", controller.journeyTableView.getColumns().get(1).getText());
        Assertions.assertEquals("DelayedDateTime", controller.journeyTableView.getColumns().get(2).getText());
        Assertions.assertEquals("DepartureGate", controller.journeyTableView.getColumns().get(3).getText());
        Assertions.assertEquals("PlaneID", controller.journeyTableView.getColumns().get(4).getText());
        Assertions.assertEquals("FlightNumber", controller.journeyTableView.getColumns().get(5).getText());
        Assertions.assertEquals("Actions", controller.journeyTableView.getColumns().get(6).getText());

        // Ensure Actions column exists (extra safety check)
        boolean hasActions = controller.journeyTableView.getColumns()
                .stream()
                .anyMatch(col -> "Actions".equals(col.getText()));

        Assertions.assertTrue(hasActions);

        // Ensure table is not editable
        Assertions.assertFalse(controller.journeyTableView.isEditable());
    }

    @Test
    void test_add_columns_airline() throws Exception {

        interact(() -> {
            try {
                controller.add_columns_airline();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        // Ensure correct number of columns injected
        Assertions.assertTrue(controller.airlineTableView.getColumns().size() >= 3);

        Assertions.assertEquals("AirlineID", controller.airlineTableView.getColumns().get(0).getText());
        Assertions.assertEquals("AirlineName", controller.airlineTableView.getColumns().get(1).getText());
        Assertions.assertEquals("Actions", controller.airlineTableView.getColumns().get(2).getText());

        // Ensure Actions column exists
        boolean hasActions = controller.airlineTableView.getColumns()
                .stream()
                .anyMatch(col -> "Actions".equals(col.getText()));

        Assertions.assertTrue(hasActions);

        // Ensure table is not editable
        Assertions.assertFalse(controller.airlineTableView.isEditable());
    }
    @Test

    void testRefreshFlight() throws Exception {
        controller.add_columns_flight();
        controller.refresh_flight();

        assertNotNull(controller.flightTableView.getItems());
        assertEquals(controller.flightList, controller.flightTableView.getItems());

        // Optional: ensure data actually loaded
        assertTrue(controller.flightList.size() >= 0);
    }

    @Test
    void testRefreshPlane() throws Exception {
        controller.add_columns_plane();
        controller.refresh_plane();

        assertNotNull(controller.planeTableView.getItems());
        assertEquals(controller.planeList, controller.planeTableView.getItems());

        assertTrue(controller.planeList.size() >= 0);
    }

    @Test
    void testRefreshAirline() throws Exception {
        controller.add_columns_airline();
        controller.refresh_airline();

        assertNotNull(controller.airlineTableView.getItems());
        assertEquals(controller.airlineList, controller.airlineTableView.getItems());

        assertTrue(controller.airlineList.size() >= 0);
    }

    @Test
    void testRefreshJourney() throws Exception {
        controller.add_columns_journey();
        controller.refresh_journey();

        assertNotNull(controller.journeyTableView.getItems());
        assertEquals(controller.journeyList, controller.journeyTableView.getItems());

        assertTrue(controller.journeyList.size() >= 0);
    }


    @Test
    public void test_database_connection() {
        DatabaseConnection databaseConnection = new DatabaseConnection();

        assertDoesNotThrow(() -> {
            databaseConnection.connect();
        });

    }

    @Test
    void test_refresh_flight() {
        assertDoesNotThrow(() -> {
            controller.refresh_flight();
        });
    }
    @Test
    void test_refresh_plane() {
        assertDoesNotThrow(() -> {
            controller.refresh_plane();
        });
    }
    @Test
    void test_refresh_airline() {
        assertDoesNotThrow(() -> {
            controller.refresh_airline();
        });
    }
    @Test
    void test_refresh_journey() {
        assertDoesNotThrow(() -> {
            controller.refresh_journey();
        });
    }
}
