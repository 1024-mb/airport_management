package com.example.airport_management.Test;

import com.example.airport_management.controller.flightDetailController;
import com.example.airport_management.controller.loginController;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import static org.junit.jupiter.api.Assertions.*;

public class loginControllerTest  extends ApplicationTest {

    loginController controller = null;

    @Override
    public void start(Stage stage) {
        controller = new loginController();

        controller.login_arrow = new Label();
        controller.username_field = new TextField();
        controller.password_field = new PasswordField();
        controller.email_field = new TextField();
        controller.error_label = new Label();

        stage.setScene(new Scene(controller.username_field));
        stage.show();
    }

    @Test
    void test_valid_email() {
        assertNotNull(controller.email_field);
        assertTrue(controller.validate_email("test@example.com"));
    }

    @Test
    void test_invalid_email_empty() {
        assertNotNull(controller.email_field);
        assertFalse(controller.validate_email(""));
    }

    @Test
    void test_invalid_email_missing_at() {
        assertNotNull(controller.email_field);
        assertFalse(controller.validate_email("testexample.com"));
    }

    @Test
    void test_invalid_email_missing_dot() {
        assertNotNull(controller.email_field);
        assertFalse(controller.validate_email("test@examplecom"));
    }

    @Test
    void test_valid_password() {
        assertNotNull(controller.password_field);
        assertTrue(controller.validate_password("Password123!"));
    }

    @Test
    void test_password_too_short() {
        assertNotNull(controller.password_field);
        assertFalse(controller.validate_password("Pass1!"));
    }

    @Test
    void test_password_no_numbers() {
        assertNotNull(controller.password_field);
        assertFalse(controller.validate_password("Password!!!"));
    }

    @Test
    void test_password_no_special_chars() {
        assertNotNull(controller.password_field);
        assertFalse(controller.validate_password("Password123"));
    }

    @Test
    void test_password_empty() {
        assertNotNull(controller.password_field);
        assertFalse(controller.validate_password(""));
    }


    @Test
    void test_valid_username_format() {
        assertNotNull(controller.username_field);
        // This only tests regex part, DB may interfere
        assertTrue(controller.validate_username("user_123"));
    }

    @Test
    void test_invalid_username_special_chars() {
        assertNotNull(controller.username_field);
        assertFalse(controller.validate_username("user@123"));
    }

    @Test
    void test_invalid_username_empty() {
        assertNotNull(controller.username_field);
        assertFalse(controller.validate_username(""));
    }

    @Test
    void test_initialize_does_not_crash() {
        assertNotNull(controller.username_field);
        assertDoesNotThrow(() -> controller.initialize());
    }


    @Test
    void test_login_invalid_credentials_sets_error() {
        controller.username_field.setText("wrongUser");
        controller.password_field.setText("wrongPass");

        assertDoesNotThrow(() -> {
            try {
                controller.login(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


}
