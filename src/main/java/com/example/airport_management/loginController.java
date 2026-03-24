package com.example.airport_management;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import javafx.util.Duration;


public class loginController {
    @FXML private Button login_button;

    @FXML
    void initialize() {
        FadeTransition fade_login = new FadeTransition(Duration.seconds(2), login_button);
        fade_login.setFromValue(1.0);
        fade_login.setToValue(0.0);
        fade_login.setCycleCount(Animation.INDEFINITE);
        fade_login.setAutoReverse(true);
        fade_login.play();
    }


}
