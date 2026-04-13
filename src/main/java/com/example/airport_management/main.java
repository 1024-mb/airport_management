package com.example.airport_management;

import com.example.airport_management.controller.databaseController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/startPage.fxml"));
        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root);
        stage.setTitle("SkyFlow");
        stage.setScene(scene);
        stage.show();
    }

    public void flights(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/airport_management/main.fxml"));
        Parent root = fxmlLoader.load();


        Scene scene = new Scene(root);
        stage.setTitle("SkyFlow");
        stage.setScene(scene);
        stage.show();
    }

}
