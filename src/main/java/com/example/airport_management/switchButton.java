package com.example.airport_management;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.io.IOException;

public class switchButton {
    public static void setButton(Button button) {
        HBox graphic = (HBox) button.getGraphic();

        if(Session.getInstance().isAuthenticated()) {
            ImageView imageView = (ImageView) graphic.getChildren().get(0);
            imageView.setImage(new Image(main.class.getResource("static/logout.png").toExternalForm()));
            button.setOnAction(e -> {
                try {
                    login.logout(e, button);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
        else {
            ImageView imageView = (ImageView) graphic.getChildren().get(0);
            imageView.setImage(new Image(main.class.getResource("static/user.png").toExternalForm()));
            button.setOnAction(e -> {
                try {
                    login.login(e, button);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }
}
