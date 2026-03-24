package com.example.airport_management;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.util.Objects;
import java.util.Set;

public class starterController {

    @FXML
    private StackPane root;

    @FXML private Label shopping_text;
    @FXML private Label shopping_arrow;

    @FXML private Label flight_text;
    @FXML private Label flight_arrow;

    @FXML private Label welcomeLabel;

    @FXML Label header_btn;

    public void alternate_text(String[] messages, Label label) {
        SequentialTransition seqTransition = new SequentialTransition();

        for (String msg : messages) {
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), label);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), label);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);

            fadeOut.setOnFinished(e -> label.setText(msg));

            seqTransition.getChildren().addAll(fadeOut, fadeIn);
        }

        seqTransition.setCycleCount(SequentialTransition.INDEFINITE);
        seqTransition.play();


    }

    @FXML
    public void initialize() {
        FadeTransition fade_shopping = new FadeTransition(Duration.seconds(2), shopping_arrow);
        fade_shopping.setFromValue(1.0);
        fade_shopping.setToValue(0.0);
        fade_shopping.setCycleCount(Animation.INDEFINITE);
        fade_shopping.setAutoReverse(true);
        fade_shopping.play();

        FadeTransition fade_flight = new FadeTransition(Duration.seconds(2), flight_arrow);
        fade_flight.setFromValue(1.0);
        fade_flight.setToValue(0.0);
        fade_flight.setCycleCount(Animation.INDEFINITE);
        fade_flight.setAutoReverse(true);
        fade_flight.play();

        String[] messages_welcome = {"Selamat Datang", "欢迎", "வணக்கம்", "स्वागत है", "Welcome"};
        alternate_text(messages_welcome, welcomeLabel);


        String[] messages_shopping = {"Membeli-belah", "购物", "ஷாப்பிங்", "खरीदारी", "Shopping"};
        alternate_text(messages_shopping, shopping_text);

        String[] messages_flight = {"Penerbangan", "航班", "விமானங்கள்", "उड़ानें", "Flights"};
        alternate_text(messages_flight, flight_text);

        String[] messages_header_btn = {"Sekilas", "一览", "ஒரு பார்வை", "एक नज़र में", "At A Glance"};
        alternate_text(messages_header_btn, header_btn);

        // Source - https://stackoverflow.com/a/29049996
        // Posted by Elltz, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-22, License - CC BY-SA 3.0
        BackgroundImage myBI= new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("/static/Dawn.png")).toExternalForm(),777.0,764,false,false),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

    }
}
