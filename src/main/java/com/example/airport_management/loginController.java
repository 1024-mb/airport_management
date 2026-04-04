package com.example.airport_management;

import com.almasb.fxgl.entity.action.Action;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;


public class loginController {
    @FXML private Label login_arrow;
    @FXML private TextField username_field;
    @FXML private PasswordField password_field;

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

    public void login(ActionEvent event) throws IOException {
        String username = username_field.getText();
        String password = password_field.getText();

        // TODO: ADD FILE PERSISTENCE HERE
        if(username.equals("admin") && password.equals("password")) {
            Session.getInstance().login();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("databases_entry.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }


}
