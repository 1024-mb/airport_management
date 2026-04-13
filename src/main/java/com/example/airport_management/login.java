package com.example.airport_management;

//cd /Users/msajjad/IdeaProjects/airport_management/src/main/java/com/example/airport_management/

import com.example.airport_management.utilities.Session;
import com.example.airport_management.utilities.switchButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class login {

    public static void login(ActionEvent event, Button button) throws IOException {
        FXMLLoader loader = new FXMLLoader(login.class.getResource("/com/example/airport_management/login.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
        switchButton.setButton(button);
    }
    public static void logout(ActionEvent event, Button button) throws IOException {
        Session.getInstance().logout();
        switchButton.setButton(button);
    }
}
