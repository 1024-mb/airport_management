package com.example.airport_management.controller;

import com.example.airport_management.main;
import com.example.airport_management.models.DatabaseConnection;
import com.example.airport_management.utilities.Session;
import com.example.airport_management.login;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.*;


public class loginController {
    @FXML private Label login_arrow;
    @FXML private TextField username_field;
    @FXML private PasswordField password_field;
    @FXML private TextField email_field;
    @FXML private Button signup_button;

    @FXML private Label error_label;

    @FXML
    void initialize() {
        FadeTransition fade_login = new FadeTransition(Duration.seconds(2), login_arrow);
        fade_login.setFromValue(1.0);
        fade_login.setToValue(0.0);
        fade_login.setCycleCount(Animation.INDEFINITE);
        fade_login.setAutoReverse(true);
        fade_login.play();
    }

    public void home(ActionEvent event) throws IOException {com.example.airport_management.home.home(event);}

    public boolean validate_email(String email) {
        if(email.isEmpty() || (!email.contains("@")) || (!email.contains("."))) {

            return false;
        }

        return true;
    }

    public boolean validate_password(String password) {
        if(password.isEmpty() || password.length() < 10 || password.matches("[a-zA-Z ]*") || password.matches("[a-zA-Z0-9 ]*")) {
            return false;
        }
        return true;
    }

    public boolean validate_username(String username) {
        if(username.isEmpty() || username.contains(" ") || username.contains("/") || username.contains("\\")) {

            return false;
        }
        try {
            Connection connectDB = DatabaseConnection.connect();
            String prefetchQuery = "SELECT UserName FROM USER;";
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(prefetchQuery);
            while(queryOutput.next()) {
                if(queryOutput.getString("UserName").equals(username)) {
                    return false;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public void signup(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/airport_management/signup.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        loginController controller = loader.getController();


        controller.username_field.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.signup_button.setDisable(false);
            controller.error_label.setText("");

            controller.password_field.setDisable(false);
            controller.username_field.setDisable(false);
            controller.email_field.setDisable(false);

            if(newValue.isEmpty()) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a Username");

            }
            if(newValue.contains(" ") || newValue.contains("/") || newValue.contains("\\")) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Invalid Username");

            }
            try {
                Connection connectDB = DatabaseConnection.connect();
                String prefetchQuery = "SELECT UserName FROM USER;";
                Statement statement = connectDB.createStatement();
                ResultSet queryOutput = statement.executeQuery(prefetchQuery);
                while(queryOutput.next()) {
                    if(queryOutput.getString("UserName").equals(newValue)) {
                        controller.signup_button.setDisable(true);
                        controller.error_label.setText("Error - Username Already in Use");

                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        controller.password_field.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.signup_button.setDisable(false);
            controller.error_label.setText("");

            controller.password_field.setDisable(false);
            controller.username_field.setDisable(false);
            controller.email_field.setDisable(false);

            if(newValue.isEmpty()) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a Password");

            }
            if(newValue.length() < 10 || newValue.isBlank()) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a String With More Than 10 Characters");


            }
            if(newValue.matches("[a-zA-Z ]*")) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Ensure there are numbers characters in the password");

            }
            if(newValue.matches("[a-zA-Z0-9 ]*")) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Ensure there are special characters in the password");

            }

        });

        controller.email_field.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.signup_button.setDisable(false);
            controller.error_label.setText("");

            controller.password_field.setDisable(false);
            controller.username_field.setDisable(false);
            controller.email_field.setDisable(false);

            if(newValue.isEmpty()) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a Valid Email");

            }
            if(!newValue.contains("@")) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a Valid Email");

            }
            if(!newValue.contains(".")) {
                controller.signup_button.setDisable(true);
                controller.error_label.setText("Error - Please Enter a Valid Email");

            }

        });

        controller.signup_button.setOnAction(e -> {
            String username = controller.username_field.getText();
            String password = controller.password_field.getText();
            String user_email = controller.email_field.getText();

            if(validate_email(user_email) && validate_password(password) && validate_username(username)) {

                String connectionQuery = "INSERT INTO USER(UserName, Password, Email, PRIVILEGE) " +
                        "VALUES(\"" + username + "\", \"" + password + "\", \"" + user_email + "\", " + "\"Regular\"" +
                        ");";


                Connection connectDB = null;

                try {
                    connectDB = DatabaseConnection.connect();
                    PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                    pstmt.executeUpdate(connectionQuery);

                    FXMLLoader loader2 = new FXMLLoader(login.class.getResource("/com/example/airport_management/login.fxml"));
                    Parent root2 = loader2.load();
                    Scene scene2 = new Scene(root2);
                    stage.setScene(scene2);
                    stage.show();


                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            else {
                if(!validate_email(user_email)) {controller.error_label.setText("Error - invalid email.");}
                if(!validate_username(username)) {controller.error_label.setText("Error - invalid username / username already in use");}
                if(!validate_password(password)) {controller.error_label.setText("Error - invalid password.");}

            }

        });


    }


    public void login(ActionEvent event) throws Exception {
        String username = username_field.getText();
        String password = password_field.getText();

        Connection connectDB = DatabaseConnection.connect();
        String connectionQuery = "SELECT * FROM USER WHERE UserName = ?;";

        try {
            PreparedStatement statement = connectDB.prepareStatement(connectionQuery);
            statement.setString(1, username);
            ResultSet queryOutput = statement.executeQuery();

            if(queryOutput.next()) {

                if (queryOutput.getString("Password").equals(password) && queryOutput.getString("PRIVILEGE").equals("Admin")) {
                    Session.getInstance().login(queryOutput.getInt("UserID"));
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/airport_management/databases_entry.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();
                } else if (queryOutput.getString("Password").equals(password) && queryOutput.getString("PRIVILEGE").equals("Regular")) {
                    Session.getInstance().login(queryOutput.getInt("UserID"));
                    home(event);
                }
                else{
                    error_label.setText("Incorrect Username or Password");
                }
            }
            else{
                error_label.setText("Incorrect Username or Password");
            }

        }
        catch (Exception ex2) {
            throw new RuntimeException(ex2);
        }
    }


}
