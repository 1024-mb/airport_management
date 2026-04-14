package com.example.airport_management.controller;

import com.example.airport_management.utilities.Session;
import com.example.airport_management.login;
import com.example.airport_management.main;
import com.example.airport_management.utilities.switchButton;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

public class starterController {

    @FXML public StackPane root;
    @FXML public Label shopping_text;
    @FXML public Label flight_text;
    @FXML public Label welcomeLabel;
    @FXML public Label db_label;
    @FXML public Button login_button;
    @FXML public Button database_button;
    @FXML public Label header_btn;


    public void database_entry(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("/com/example/airport_management/databases_entry.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

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
        if(Session.getInstance().get_user_id() != 3 || !(Session.getInstance().isAuthenticated())) {
            database_button.setVisible(false);
            database_button.setDisable(true);
            db_label.setVisible(false);
        }

        Platform.runLater(() -> {
            Set<Node> nodes = root.lookupAll(".arrow_in");

            ParallelTransition parallelTransition = new ParallelTransition();

            for (Node node : nodes) {
                FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
                fade.setFromValue(1.0);
                fade.setToValue(0.0);

                parallelTransition.getChildren().add(fade);
            }

            parallelTransition.setAutoReverse(true);
            parallelTransition.setCycleCount(SequentialTransition.INDEFINITE);
            parallelTransition.play();

            String[] messages_welcome = {"Selamat Datang", "欢迎", "வணக்கம்", "स्वागत है", "Welcome"};
            alternate_text(messages_welcome, welcomeLabel);

            String[] messages_shopping = {"Membeli-belah", "购物", "ஷாப்பிங்", "खरीदारी", "Shopping"};
            alternate_text(messages_shopping, shopping_text);

            String[] messages_flight = {"Penerbangan", "航班", "விமானங்கள்", "उड़ानें", "Flights"};
            alternate_text(messages_flight, flight_text);

            String[] messages_header_btn = {"Sekilas", "一览", "ஒரு பார்வை", "एक नज़र में", "At A Glance"};
            alternate_text(messages_header_btn, header_btn);

        });

        switchButton.setButton(login_button);
        // Source - https://stackoverflow.com/a/29049996
        // Posted by Elltz, modified by community. See post 'Timeline' for change history
        // Retrieved 2026-03-22, License - CC BY-SA 3.0
        BackgroundImage myBI= new BackgroundImage(new Image(Objects.requireNonNull(getClass().getResource("/com/example/airport_management/static/Dawn.png")).toExternalForm(),root.getWidth(),root.getHeight(),false,false),
                BackgroundRepeat.REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        root.setBackground(new Background(myBI));

    }


    public void shopping(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("/com/example/airport_management/shops_map.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void flights(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("/com/example/airport_management/terminals_page.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        departuresController controller = loader.getController();

        controller.add_columns_departures();
    }

    @FXML
    public void login(ActionEvent event) throws IOException {
        login.login(event, login_button);
    }
}
