package com.example.airport_management;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

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

}
