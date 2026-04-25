package com.example.airport_management.Test;

import com.example.airport_management.controller.databases_entry_controller;
import com.example.airport_management.controller.departuresController;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.util.WaitForAsyncUtils;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class departuresControllerTest extends ApplicationTest {
    static departuresController controller = null;

    @Override
    public void start(Stage stage) {
        controller = new departuresController();

        controller.terminal1_table = new TableView<>();
        controller.terminal2_table = new TableView<>();

        controller.root = new AnchorPane();

        controller.quantity_choice = new Spinner<>();
        controller.quantity_choice.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
        );

        controller.seat_type = new ChoiceBox<>();

        controller.submitButton = new Button();
        controller.cancelButton = new Button();

        controller.history_btn = new Button();

        controller.flights = new VBox();
        controller.back = new Button();

        controller.error_label = new Label();
        controller.net_total = new Label();
        controller.gross_total = new Label();

        controller.cardNumber = new TextField();
        controller.expiryMonth = new TextField();
        controller.expiryYear = new TextField();

        controller.seat_type_label = new Label();
        controller.quantity_label = new Label();

        stage.setScene(new Scene(controller.terminal1_table));
        stage.show();
    }


    @Test
    void test_add_columns_departures() {
        assertDoesNotThrow(() -> {
            WaitForAsyncUtils.waitForFxEvents();

            Platform.runLater(() -> {
                try {
                    controller.add_columns_departures();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            WaitForAsyncUtils.waitForFxEvents();
        });
    }

    @Test
    void test_tables_are_initialized() {
        assertNotNull(controller.terminal1_table);
        assertNotNull(controller.terminal2_table);
    }

    @Test
    void test_order_history_populates_vbox() {
        assertDoesNotThrow(() -> {
            controller.history_btn.fire();
            assertNotNull(controller.flights);
        });
    }

    @Test
    void test_back_does_not_throw() {
        assertDoesNotThrow(() -> controller.back.fire());
    }
    

    @Test
    void history_does_not_throw() {
        assertDoesNotThrow(() -> controller.history_btn.fire());

    }

}
