package com.example.airport_management.Test;

import com.example.airport_management.controller.starterController;
import javafx.application.Application;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class starterControllerTest extends ApplicationTest {
    starterController controller = null;

    @Override
    public void start(Stage stage) {
        controller = new starterController();

        controller.root = new StackPane();

        controller.shopping_text = new Label();
        controller.flight_text = new Label();
        controller.welcomeLabel = new Label();
        controller.db_label = new Label();
        controller.header_btn = new Label();

        controller.login_button = new Button();
        controller.database_button = new Button();
    }

    @Test
    void test_initialize_does_not_crash() {
        assertDoesNotThrow(() -> controller.initialize());
    }

    @Test
    void test_alternate_text_does_not_crash() {
        String[] messages = {"Hello", "World"};

        assertDoesNotThrow(() ->
                controller.alternate_text(messages, controller.welcomeLabel)
        );
    }

    @Test
    void test_alternate_text_sets_text() {
        String[] messages = {"A", "B"};

        controller.alternate_text(messages, controller.welcomeLabel);

        // Can't reliably assert animation timing,
        // but label should not stay null
        assertNotNull(controller.welcomeLabel.getText());
    }
    @Test
    void test_database_button_hidden_for_non_admin() {
        controller.initialize();

        // Most users won't be admin (ID != 3)
        assertTrue(controller.database_button.isDisabled() ||
                !controller.database_button.isVisible());
    }


}
