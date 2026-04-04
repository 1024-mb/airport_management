package com.example.airport_management;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Set;

public class databases_entry_controller {
    @FXML private AnchorPane root;

    public void initialize() {
        Set<Node> nodes = root.lookupAll(".arrow_in");

        for (Node node : nodes) {
            FadeTransition fade_shopping = new FadeTransition(Duration.seconds(2), node);
            fade_shopping.setFromValue(1.0);
            fade_shopping.setToValue(0.0);
            fade_shopping.setCycleCount(Animation.INDEFINITE);
            fade_shopping.setAutoReverse(true);
            fade_shopping.play();
        }
    }

    public void home(ActionEvent event) throws IOException {com.example.airport_management.home.home(event);}

    public void flight_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController(); // THIS is the real controller
        controller.displayData("FLIGHT");
    }

    public void airline_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController(); // THIS is the real controller
        controller.displayData("AIRLINE");
    }

    public void plane_database(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("databases.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        databaseController controller = loader.getController(); // THIS is the real controller
        controller.displayData("PLANE");
    }

}
