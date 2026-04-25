package com.example.airport_management.controller;

//cd /Users/msajjad/IdeaProjects/airport_management/src/main/java/com/example/airport_management/

import com.example.airport_management.models.DatabaseConnection;
import com.example.airport_management.models.database;
import com.example.airport_management.main;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.airport_management.controller.databaseController.validation.*;

public class databaseController {
    @FXML public TableView<database.Plane> planeTableView;
    @FXML public TableView<database.Flight> flightTableView;
    @FXML public TableView<database.Airline> airlineTableView;
    @FXML public TableView<database.Journey> journeyTableView;

    @FXML public Button insertButton;
    @FXML public VBox root;
    @FXML public Label database_name;

    @FXML public Button submitButton;
    @FXML public Button cancelButton;

    @FXML public TextField Flight_AirlineID;
    @FXML public TextField Flight_Origin;
    @FXML public TextField Flight_Destination;
    @FXML public TextField Flight_Duration;
    @FXML public TextField Flight_FlightNumber;

    @FXML public TextField Journey_DepartureDateTime;
    @FXML public TextField Journey_DelayedDateTime;
    @FXML public TextField Journey_DepartureGate;
    @FXML public TextField Journey_PlaneID;
    @FXML public TextField Journey_FlightNumber;

    @FXML public Button yesButtonConfirm;
    @FXML public Button noButtonConfirm;

    @FXML public Label error_message;
    @FXML public Label error_msg;


    @FXML public TextField Plane_PlaneLayout;
    @FXML public TextField Plane_Manufacturer;
    @FXML public TextField Plane_Passengers;
    @FXML public TextField Plane_Model;
    @FXML public TextField Plane_FlightAttendants;
    @FXML public Button OKButton;

    @FXML public TextField Airline_AirlineName;

    public ObservableList<database.Plane> planeList;
    public ObservableList<database.Flight> flightList;
    public ObservableList<database.Airline> airlineList;
    public ObservableList<database.Journey> journeyList;


    // all other methods call the methods that have tests for them.
    @FXML
    public void displayData(String tableType) throws Exception {
        Set<Node> tableViews = root.lookupAll(".table-view");

        for(Node node : tableViews) {
            TableView nodetable = (TableView) node;
            nodetable.setEditable(false);
            nodetable.setVisible(false);
            nodetable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        if(tableType.equals("PLANE")) {
            planeTableView.setVisible(true);
            add_columns_plane();
            database_name.setText("PLANE");
        }

        else if(tableType.equals("FLIGHT")) {
            flightTableView.setVisible(true);
            add_columns_flight();
            database_name.setText("FLIGHT");
        }

        else if(tableType.equals("AIRLINE")) {
            airlineTableView.setVisible(true);
            add_columns_airline();
            database_name.setText("AIRLINE");
        }

        else if(tableType.equals("JOURNEY")) {
            journeyTableView.setVisible(true);
            add_columns_journey();
            database_name.setText("JOURNEY");
        }


    }


    public static void show_error_stage(Stage popupStage, String error_message) {
        Stage errorStage = new Stage();
        FXMLLoader errorLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/validationPopup.fxml"));

        Parent errorRoot = null;
        try {
            errorRoot = errorLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        databaseController errorController = errorLoader.getController();
        errorController.error_message.setText(error_message);
        errorStage.setScene(new Scene(errorRoot));
        errorStage.initOwner(popupStage);
        errorStage.show();
        errorController.OKButton.setOnAction(e3 -> {
            errorStage.close();
        });
    }


    public void add_columns_plane() throws Exception {
        planeTableView.setEditable(false);

        TableColumn PlaneID = new TableColumn("PlaneID");
        PlaneID.setCellValueFactory(new PropertyValueFactory<database.Plane, Integer>("PlaneID"));
        PlaneID.getStyleClass().add("table_column");

        TableColumn Manufacturer = new TableColumn("Manufacturer");
        Manufacturer.setCellValueFactory(new PropertyValueFactory<database.Plane, String>("Manufacturer"));
        Manufacturer.getStyleClass().add("table_column");

        TableColumn Model = new TableColumn("Model");
        Model.setCellValueFactory(new PropertyValueFactory<database.Plane, String>("Model"));
        Model.getStyleClass().add("table_column");

        TableColumn FlightAttendants = new TableColumn("FlightAttendants");
        FlightAttendants.setCellValueFactory(new PropertyValueFactory<database.Plane, Integer>("FlightAttendants"));
        FlightAttendants.getStyleClass().add("table_column");

        TableColumn Passengers = new TableColumn("Passengers");
        Passengers.setCellValueFactory(new PropertyValueFactory<database.Plane, Integer>("Passengers"));
        Passengers.getStyleClass().add("table_column");

        TableColumn PlaneLayout = new TableColumn("PlaneLayout");
        PlaneLayout.setCellValueFactory(new PropertyValueFactory<database.Plane, String>("PlaneLayout"));
        PlaneLayout.getStyleClass().add("table_column");


        TableColumn<database.Plane, Void> Actions = new TableColumn("Actions");
        Actions.setMinWidth(120);


        Actions.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Edit");
            private final Button btn2 = new Button("Delete");
            private final HBox box = new HBox(10, btn1, btn2);

            {
                btn1.getStyleClass().add("button_database");
                ImageView edit = new ImageView(main.class.getResource("/com/example/airport_management/static/edit.png").toExternalForm());
                edit.setFitHeight(12);
                edit.setFitWidth(12);
                btn1.setGraphic(new HBox(edit));

                btn2.getStyleClass().add("button_database");
                ImageView delete = new ImageView(main.class.getResource("/com/example/airport_management/static/delete.png").toExternalForm());
                delete.setFitHeight(12);
                delete.setFitWidth(12);
                btn2.setGraphic(new HBox(delete));

                setAlignment(Pos.CENTER);

                btn1.setOnAction(e -> {
                    database.Plane rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();

                    Stage popupStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/popup_plane.fxml"));
                    Parent root = null;

                    try {
                        root = fxmlLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    databaseController controller = fxmlLoader.getController();

                    popupStage.setScene(new Scene(root));
                    popupStage.initOwner(stage);
                    popupStage.show();

                    controller.Plane_PlaneLayout.setText(rowData.getPlaneLayout());
                    controller.Plane_Manufacturer.setText(rowData.getManufacturer());
                    controller.Plane_Model.setText(rowData.getModel());
                    controller.Plane_Passengers.setText(String.valueOf(rowData.getPassengers()));
                    controller.Plane_FlightAttendants.setText(String.valueOf(rowData.getFlightAttendants()));

                    validation validation = new validation();
                    validation.live_validation_plane(controller);

                    controller.submitButton.setOnAction(e2 -> {
                        if(validate_data_plane(controller, popupStage) == true) {
                            String connectionQuery = "UPDATE PLANE SET PlaneLayout=\"" + controller.Plane_PlaneLayout.getText() +
                                    "\", Manufacturer=\"" + controller.Plane_Manufacturer.getText()
                                    + "\"" + ", Model=\"" + controller.Plane_Model.getText() + "\"," +
                                    "Passengers=" + controller.Plane_Passengers.getText() +
                                    ", FlightAttendants=" + controller.Plane_FlightAttendants.getText() +
                                    " WHERE PlaneID=" + rowData.getPlaneID();

                            Connection connectDB = null;
                            try {
                                connectDB = DatabaseConnection.connect();
                                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                                pstmt.executeUpdate(connectionQuery);
                                popupStage.close();

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                btn2.setDisable(true);
                                btn1.setDisable(true);
                                refresh_plane();
                                btn2.setDisable(false);
                                btn1.setDisable(false);
                            } catch (Exception ex2) {
                                throw new RuntimeException(ex2);
                            }
                        }
                    });
                    controller.cancelButton.setOnAction(e3 -> {popupStage.close();});
                });
                btn2.setOnAction(e -> {
                    database.Plane rowData = getTableView().getItems().get(getIndex());
                    boolean return_value = confirm_delete();

                    Connection connectDB = null;

                    if(return_value==true) {
                        try {
                            try {
                                connectDB = DatabaseConnection.connect();
                                String connectionQuery = "DELETE FROM PLANE WHERE PlaneID=" + rowData.getPlaneID() + ";";
                                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                                pstmt.executeUpdate(connectionQuery);

                            } catch (Exception ex) {
                                show_error_stage(new Stage(), "Cannot Delete This Item - Foreign Key Constraint Failed");
                            }
                            btn2.setDisable(true);
                            btn1.setDisable(true);
                            refresh_plane();
                            btn2.setDisable(false);
                            btn1.setDisable(false);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        Connection connectDB = DatabaseConnection.connect();
        planeList = FXCollections.observableArrayList();
        String connectionQuery = "SELECT * from PLANE;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                planeList.add(new database.Plane(
                        new SimpleIntegerProperty(queryOutput.getInt("PlaneID")),
                        new SimpleStringProperty(queryOutput.getString("Manufacturer")),
                        new SimpleStringProperty(queryOutput.getString("Model")),
                        new SimpleIntegerProperty(queryOutput.getInt("FlightAttendants")),
                        new SimpleIntegerProperty(queryOutput.getInt("Passengers")),
                        new SimpleStringProperty(queryOutput.getString("PlaneLayout"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        planeTableView.setItems(planeList);
        planeTableView.getColumns().addAll(PlaneID, Manufacturer, Model, FlightAttendants, Passengers, PlaneLayout, Actions);

    }

    boolean confirm_delete() {
        Stage stage = (Stage) root.getScene().getWindow();
        AtomicBoolean return_value = new AtomicBoolean(false);

        Stage popupStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/confirmation_box.fxml"));
        Parent root = null;

        try {
            root = fxmlLoader.load();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        databaseController controller = fxmlLoader.getController();

        popupStage.setScene(new Scene(root));
        popupStage.initOwner(stage);

        controller.yesButtonConfirm.setOnAction(e -> {
            return_value.set(true);
            popupStage.close();
        });

        controller.noButtonConfirm.setOnAction(e -> {
            return_value.set(false);
            popupStage.close();
        });
        popupStage.showAndWait();

        return return_value.get();

    }

    public void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/databases_entry.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    public void insertData() {
        Stage stage = (Stage) root.getScene().getWindow();

        Stage popupStage = new Stage();

        Parent root = null;
        FXMLLoader fxmlLoader = null;
        databaseController controller;
        databaseController final_controller;

        switch(database_name.getText()){
            case "PLANE":
                fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_plane.fxml"));
                try {
                    root = fxmlLoader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                controller = fxmlLoader.getController();

                validation.live_validation_plane(controller);

                break;

            case "FLIGHT":
                fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_flight.fxml"));

                try {
                    root = fxmlLoader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                controller = fxmlLoader.getController();
                live_validation_flight(controller);

                break;

            case "AIRLINE":
                fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_airline.fxml"));
                try {
                    root = fxmlLoader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                controller = fxmlLoader.getController();
                live_validation_airline(controller);

                break;

            case "JOURNEY":
                fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_journey.fxml"));
                try {
                    root = fxmlLoader.load();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                controller = fxmlLoader.getController();
                live_validation_journey(controller);
                break;

            default:
                controller = null;
                break;
        }

        popupStage.setScene(new Scene(root));
        popupStage.initOwner(stage);
        popupStage.show();

        controller.submitButton.setOnAction(e2 -> {
            String connectionQuery = "";
            switch(database_name.getText()){
                case "PLANE":
                    if(validate_data_plane(controller, popupStage)) {
                        connectionQuery = "INSERT INTO PLANE(Manufacturer, Model, Passengers, " +
                                "FlightAttendants, PlaneLayout) " +
                                "VALUES(\"" + controller.Plane_Manufacturer.getText() + "\", \"" + controller.Plane_Model.getText() +
                                "\", " + controller.Plane_Passengers.getText() + ", " +
                                controller.Plane_FlightAttendants.getText() + ", \"" + controller.Plane_PlaneLayout.getText() + "\");";
                    }
                    break;
                case "FLIGHT":
                    if(validate_data_flight(controller, popupStage, true)) {
                        connectionQuery = "INSERT INTO FLIGHT(FlightNumber, AirlineID, Origin, " +
                                "Destination, Duration) " +
                                "VALUES(\"" + controller.Flight_FlightNumber.getText() + "\", " + controller.Flight_AirlineID.getText() + ", \"" + controller.Flight_Origin.getText() +
                                "\", \"" + controller.Flight_Destination.getText() + "\", " +
                                controller.Flight_Duration.getText() + ");";
                    }
                    break;

                case "AIRLINE":
                    if(validate_data_airline(controller, popupStage)) {
                        connectionQuery = "INSERT INTO AIRLINE(AirlineName) " +
                                "VALUES(\"" +
                                controller.Airline_AirlineName.getText() + "\");";
                    }
                    break;

                case "JOURNEY":
                    if(validate_data_journey(controller, popupStage)) {
                        if(!controller.Journey_DelayedDateTime.getText().isEmpty() && !controller.Journey_DelayedDateTime.getText().equals("null")) {
                            connectionQuery = "INSERT INTO JOURNEY(DepartureDateTime, DelayedDateTime, DepartureGate, PlaneID, FlightNumber) " +
                                    "VALUES(\"" + controller.Journey_DepartureDateTime.getText() + "\", \"" +
                                    controller.Journey_DelayedDateTime.getText() + "\", \"" + controller.Journey_DepartureGate.getText() +
                                    "\", " + controller.Journey_PlaneID.getText() + ", \"" + controller.Journey_FlightNumber.getText() + "\");";
                        }
                        else {
                            connectionQuery = "INSERT INTO JOURNEY(DepartureDateTime, DepartureGate, PlaneID, FlightNumber) " +
                                    "VALUES(\"" + controller.Journey_DepartureDateTime.getText() + "\", \"" +
                                    controller.Journey_DepartureGate.getText() +
                                    "\", " + controller.Journey_PlaneID.getText() + ", \"" + controller.Journey_FlightNumber.getText() + "\");";
                        }
                    }
                    break;
            }

            Connection connectDB = null;

            try {
                if(!connectionQuery.equals("")) {
                    connectDB = DatabaseConnection.connect();
                    PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                    pstmt.executeUpdate(connectionQuery);
                    popupStage.close();
                }
            }
            catch (Exception ex) {
                show_error_stage(popupStage, "An Error Occurred");
            }
            try {
                switch(database_name.getText()) {
                    case "FLIGHT":
                        refresh_flight();
                        break;

                    case "PLANE":
                        refresh_plane();
                        break;

                    case "AIRLINE":
                        refresh_airline();
                        break;

                    case "JOURNEY":
                        refresh_journey();
                        break;

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        });
        controller.cancelButton.setOnAction(e3 -> {
            popupStage.close();

        });
    }

    public void refresh_flight() {
        if(flightList != null) {
            flightList.clear();
        }

        String connectionQuery = "SELECT * from FLIGHT;";
        Connection connectDB = null;

        try {
            connectDB = DatabaseConnection.connect();

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                flightList.add(new database.Flight(
                        new SimpleStringProperty(queryOutput.getString("FlightNumber")),
                        new SimpleIntegerProperty(queryOutput.getInt("AirlineID")),
                        new SimpleStringProperty(queryOutput.getString("Origin")),
                        new SimpleStringProperty(queryOutput.getString("Destination")),
                        new SimpleIntegerProperty(queryOutput.getInt("Duration"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        flightTableView.setItems(flightList);
    }
    public void refresh_plane() {
        if(planeList != null) {
            planeList.clear();
        }
        String connectionQuery = "SELECT * from PLANE;";
        Connection connectDB = null;

        try {
            connectDB = DatabaseConnection.connect();

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                planeList.add(new database.Plane(
                        new SimpleIntegerProperty(queryOutput.getInt("PlaneID")),
                        new SimpleStringProperty(queryOutput.getString("Manufacturer")),
                        new SimpleStringProperty(queryOutput.getString("Model")),
                        new SimpleIntegerProperty(queryOutput.getInt("FlightAttendants")),
                        new SimpleIntegerProperty(queryOutput.getInt("Passengers")),
                        new SimpleStringProperty(queryOutput.getString("PlaneLayout"))

                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        planeTableView.setItems(planeList);
    }
    public void refresh_airline() {
        if(airlineList != null ) {
            airlineList.clear();
        }
        String connectionQuery = "SELECT * from AIRLINE;";
        Connection connectDB = null;

        try {
            connectDB = DatabaseConnection.connect();

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                airlineList.add(new database.Airline(
                        new SimpleIntegerProperty(queryOutput.getInt("AirlineID")),
                        new SimpleStringProperty(queryOutput.getString("AirlineName"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        airlineTableView.setItems(airlineList);
    }
    public void refresh_journey() {
        if(journeyList != null) {
            journeyList.clear();
        }

        String connectionQuery = "SELECT * from JOURNEY;";
        Connection connectDB = null;

        try {
            connectDB = DatabaseConnection.connect();

            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                journeyList.add(new database.Journey(
                        new SimpleIntegerProperty(queryOutput.getInt("JourneyID")),
                        new SimpleStringProperty(queryOutput.getString("DepartureDateTime")),
                        new SimpleStringProperty(queryOutput.getString("DelayedDateTime")),
                        new SimpleStringProperty(queryOutput.getString("DepartureGate")),
                        new SimpleIntegerProperty(queryOutput.getInt("PlaneID")),
                        new SimpleStringProperty(queryOutput.getString("FlightNumber"))
                ));
            }

        } catch (Exception e) {
            System.out.println("768" + e.getMessage());
        }

        journeyTableView.setItems(journeyList);
    }

    public void add_columns_flight() throws Exception {
        flightTableView.setEditable(false);

        TableColumn FlightNumber = new TableColumn("FlightNumber");
        FlightNumber.setCellValueFactory(new PropertyValueFactory<database.Flight, String>("FlightNumber"));
        FlightNumber.getStyleClass().add("table_column");

        TableColumn AirlineID = new TableColumn("AirlineID");
        AirlineID.setMinWidth(50);
        AirlineID.setCellValueFactory(
                new PropertyValueFactory<database.Flight, Integer>("AirlineID"));
        AirlineID.getStyleClass().add("table_column");

        TableColumn Origin = new TableColumn("Origin");
        Origin.setMinWidth(50);
        Origin.setCellValueFactory(
                new PropertyValueFactory<database.Flight, String>("Origin"));
        Origin.getStyleClass().add("table_column");


        TableColumn Destination = new TableColumn("Destination");
        Destination.setMinWidth(50);
        Destination.setCellValueFactory(
                new PropertyValueFactory<database.Flight, String>("Destination"));
        Destination.getStyleClass().add("table_column");

        TableColumn Duration = new TableColumn("Duration");
        Duration.setMinWidth(50);
        Duration.setCellValueFactory(
                new PropertyValueFactory<database.Flight, Integer>("Duration"));
        Duration.getStyleClass().add("table_column");


        TableColumn<database.Flight, Void> Actions = new TableColumn("Actions");
        Actions.setMinWidth(120);
        flightList = FXCollections.observableArrayList();


        Actions.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Edit");
            private final Button btn2 = new Button("Delete");
            private final HBox box = new HBox(10, btn1, btn2);

            {
                setAlignment(Pos.CENTER);
                btn1.getStyleClass().add("button_database");
                ImageView edit = new ImageView(main.class.getResource("/com/example/airport_management/static/edit.png").toExternalForm());
                edit.setFitHeight(12);
                edit.setFitWidth(12);
                btn1.setGraphic(new HBox(edit));

                btn2.getStyleClass().add("button_database");
                ImageView delete = new ImageView(main.class.getResource("/com/example/airport_management/static/delete.png").toExternalForm());
                delete.setFitHeight(12);
                delete.setFitWidth(12);
                btn2.setGraphic(new HBox(delete));

                btn1.setOnAction(e -> {
                    database.Flight rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();

                    Stage popupStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_flight.fxml"));
                    Parent root = null;

                    try {
                        root = fxmlLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    databaseController controller = fxmlLoader.getController();

                    popupStage.setScene(new Scene(root));
                    popupStage.initOwner(stage);
                    popupStage.show();

                    controller.Flight_FlightNumber.setDisable(true);
                    controller.Flight_FlightNumber.setText(rowData.getFlightNumber());
                    controller.Flight_AirlineID.setText(String.valueOf(rowData.getAirlineID()));
                    controller.Flight_Origin.setText(rowData.getOrigin());
                    controller.Flight_Destination.setText(rowData.getDestination());
                    controller.Flight_Duration.setText(String.valueOf(rowData.getDuration()));

                    live_validation_flight(controller);

                    controller.submitButton.setOnAction(e2 -> {
                        if(validate_data_flight(controller, popupStage, false)) {
                            String connectionQuery = "UPDATE FLIGHT SET AirlineID=\"" + controller.Flight_AirlineID.getText() +
                                    "\", Origin=\"" + controller.Flight_Origin.getText()
                                    + "\"" + ", Destination=\"" + controller.Flight_Destination.getText() + "\"," +
                                    "Duration=" + controller.Flight_Duration.getText() +
                                    " WHERE FlightNumber=\"" + controller.Flight_FlightNumber.getText() + "\";";

                            Connection connectDB = null;
                            try {
                                connectDB = DatabaseConnection.connect();
                                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                                pstmt.executeUpdate(connectionQuery);
                                popupStage.close();

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                            try {
                                btn2.setDisable(true);
                                btn1.setDisable(true);
                                refresh_flight();
                                btn2.setDisable(false);
                                btn1.setDisable(false);
                            } catch (Exception ex2) {
                                throw new RuntimeException(ex2);
                            }
                        }
                    });
                    controller.cancelButton.setOnAction(e3 -> {popupStage.close();});
                });
                btn2.setOnAction(e -> {
                    database.Flight rowData = getTableView().getItems().get(getIndex());
                    boolean return_value = confirm_delete();
                    if(return_value==true) {
                        Connection connectDB = null;
                        try {
                            connectDB = DatabaseConnection.connect();
                            String connectionQuery = "DELETE FROM FLIGHT WHERE FlightNumber=\"" + rowData.getFlightNumber() + "\";";
                            PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                            pstmt.executeUpdate(connectionQuery);

                        } catch (Exception ex) {
                            show_error_stage(new Stage(), "Cannot Delete This Item - Foreign Key Constraint Failed");
                        }
                        try {
                            btn2.setDisable(true);
                            btn1.setDisable(true);
                            refresh_flight();
                            btn1.setDisable(false);
                            btn2.setDisable(false);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        Connection connectDB = DatabaseConnection.connect();
        //
        flightList = FXCollections.observableArrayList();
        String connectionQuery = "SELECT * from FLIGHT;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                flightList.add(new database.Flight(
                        new SimpleStringProperty(queryOutput.getString("FlightNumber")),
                        new SimpleIntegerProperty(queryOutput.getInt("AirlineID")),
                        new SimpleStringProperty(queryOutput.getString("Origin")),
                        new SimpleStringProperty(queryOutput.getString("Destination")),
                        new SimpleIntegerProperty(queryOutput.getInt("Duration"))
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        flightTableView.setItems(flightList);
        flightTableView.getColumns().addAll(FlightNumber, Origin, Destination, Duration, Actions);

    }

    public void add_columns_airline() throws Exception {
        airlineTableView.setEditable(false);

        TableColumn AirlineID = new TableColumn("AirlineID");
        AirlineID.setMinWidth(50);
        AirlineID.setCellValueFactory(
                new PropertyValueFactory<database.Airline, Integer>("AirlineID"));

        TableColumn AirlineName = new TableColumn("AirlineName");
        AirlineName.setMinWidth(50);
        AirlineName.setCellValueFactory(
                new PropertyValueFactory<database.Plane, String>("AirlineName"));

        TableColumn<database.Airline, Void> Actions = new TableColumn("Actions");
        Actions.setMinWidth(120);

        Actions.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Edit");
            private final Button btn2 = new Button("Delete");
            private final HBox box = new HBox(10, btn1, btn2);

            {
                setAlignment(Pos.CENTER);
                btn1.getStyleClass().add("button_database");
                ImageView edit = new ImageView(main.class.getResource("/com/example/airport_management/static/edit.png").toExternalForm());
                edit.setFitHeight(12);
                edit.setFitWidth(12);
                btn1.setGraphic(new HBox(edit));

                btn2.getStyleClass().add("button_database");
                ImageView delete = new ImageView(main.class.getResource("/com/example/airport_management/static/delete.png").toExternalForm());
                delete.setFitHeight(12);
                delete.setFitWidth(12);
                btn2.setGraphic(new HBox(delete));

                btn1.setOnAction(e -> {
                    database.Airline rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();


                    Stage popupStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(databaseController.class.getResource("/com/example/airport_management/popup_airline.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    databaseController controller = fxmlLoader.getController();

                    controller.Airline_AirlineName.setText(rowData.getAirlineName());
                    live_validation_airline(controller);

                    popupStage.setScene(new Scene(root));
                    popupStage.initOwner(stage);
                    popupStage.show();


                    controller.submitButton.setOnAction(e2 -> {
                        if(validate_data_airline(controller, popupStage)) {
                            String connectionQuery = "UPDATE AIRLINE SET AirlineName=\"" + controller.Airline_AirlineName.getText() + "\" WHERE AirlineID =" + rowData.getAirlineID() + ";";

                            Connection connectDB = null;
                            try {
                                connectDB = DatabaseConnection.connect();
                                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                                pstmt.executeUpdate(connectionQuery);
                                popupStage.close();

                            } catch (Exception ex) {
                                show_error_stage(new Stage(), "Could Not perform action");

                            }
                            try {
                                btn2.setDisable(true);
                                btn1.setDisable(true);
                                refresh_airline();
                                btn2.setDisable(false);
                                btn1.setDisable(false);
                            } catch (Exception ex2) {
                                throw new RuntimeException(ex2);
                            }
                        }
                    });
                    controller.cancelButton.setOnAction(e3 -> {popupStage.close();});
                });
                btn2.setOnAction(e -> {
                    database.Airline rowData = getTableView().getItems().get(getIndex());
                    boolean return_value = confirm_delete();
                    if(return_value==true) {
                        Connection connectDB = null;
                        try {
                            connectDB = DatabaseConnection.connect();
                            String connectionQuery = "DELETE FROM AIRLINE WHERE AirlineID=" + rowData.getAirlineID() + ";";
                            PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                            pstmt.executeUpdate(connectionQuery);

                        } catch (Exception ex) {
                            show_error_stage(new Stage(), "Cannot Delete This Item - Foreign Key Constraint Failed");
                        }
                        try {
                            btn2.setDisable(true);
                            btn1.setDisable(true);
                            refresh_airline();
                            btn1.setDisable(false);
                            btn2.setDisable(false);
                        } catch (Exception ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(box);
                }
            }
        });

        Connection connectDB = DatabaseConnection.connect();
        airlineList = FXCollections.observableArrayList();
        String connectionQuery = "SELECT * from AIRLINE;";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                airlineList.add(new database.Airline(
                        new SimpleIntegerProperty(queryOutput.getInt("AirlineID")),
                        new SimpleStringProperty(queryOutput.getString("AirlineName"))
                ));
            }
            airlineTableView.setItems(airlineList);


        } catch (Exception e) {
            e.printStackTrace();
        }


        airlineTableView.getColumns().addAll(AirlineID, AirlineName, Actions);
    }


    public void add_columns_journey() throws Exception {
        journeyTableView.setEditable(false);

        TableColumn JourneyID = new TableColumn("JourneyID");
        JourneyID.setMinWidth(50);
        JourneyID.setCellValueFactory(
                new PropertyValueFactory<database.Journey, Integer>("JourneyID"));

        TableColumn DepartureDateTime = new TableColumn("DepartureDateTime");
        DepartureDateTime.setMinWidth(50);
        DepartureDateTime.setCellValueFactory(
                new PropertyValueFactory<database.Journey, String>("DepartureDateTime"));

        TableColumn DelayedDateTime = new TableColumn("DelayedDateTime");
        DelayedDateTime.setMinWidth(50);
        DelayedDateTime.setCellValueFactory(
                new PropertyValueFactory<database.Journey, String>("DelayedDateTime"));

        TableColumn DepartureGate = new TableColumn("DepartureGate");
        DepartureGate.setMinWidth(50);
        DepartureGate.setCellValueFactory(
                new PropertyValueFactory<database.Journey, Integer>("DepartureGate"));


        TableColumn PlaneID = new TableColumn("PlaneID");
        PlaneID.setMinWidth(50);
        PlaneID.setCellValueFactory(
                new PropertyValueFactory<database.Journey, Integer>("PlaneID"));

        TableColumn FlightNumber = new TableColumn("FlightNumber");
        FlightNumber.setMinWidth(50);
        FlightNumber.setCellValueFactory(
                new PropertyValueFactory<database.Journey, Integer>("FlightNumber"));


        Connection connectDB = DatabaseConnection.connect();
        journeyList = FXCollections.observableArrayList();
        String connectionQuery = "SELECT * from JOURNEY;";

        TableColumn<database.Journey, Void> Actions = new TableColumn("Actions");
        Actions.setMinWidth(120);

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectionQuery);

            while(queryOutput.next()) {
                journeyList.add(new database.Journey(
                        new SimpleIntegerProperty(queryOutput.getInt("JourneyID")),
                        new SimpleStringProperty(queryOutput.getString("DepartureDateTime")),
                        new SimpleStringProperty(queryOutput.getString("DelayedDateTime")),
                        new SimpleStringProperty(queryOutput.getString("DepartureGate")),
                        new SimpleIntegerProperty(queryOutput.getInt("PlaneID")),
                        new SimpleStringProperty(queryOutput.getString("FlightNumber"))
                ));
            }
            journeyTableView.setItems(journeyList);



            //button.setGraphic();
            //button.setStyle();
            //ImageView edit = new ImageView(main.class.getResource("/com/example/airport_management/static/edit.png").toExternalForm());
            //edit.setFitHeight(20);
            //edit.setFitWidth(20);

            Actions.setCellFactory(col -> new TableCell<>() {
                private final Button btn1 = new Button("Edit");
                private final Button btn2 = new Button("Delete");
                private final HBox box = new HBox(10, btn1, btn2);

                {
                    setAlignment(Pos.CENTER);
                    btn1.getStyleClass().add("button_database");
                    ImageView edit = new ImageView(main.class.getResource("/com/example/airport_management/static/edit.png").toExternalForm());
                    edit.setFitHeight(12);
                    edit.setFitWidth(12);
                    btn1.setGraphic(new HBox(edit));

                    btn2.getStyleClass().add("button_database");
                    ImageView delete = new ImageView(main.class.getResource("/com/example/airport_management/static/delete.png").toExternalForm());
                    delete.setFitHeight(12);
                    delete.setFitWidth(12);
                    btn2.setGraphic(new HBox(delete));

                    btn1.setOnAction(e -> {
                        database.Journey rowData = getTableView().getItems().get(getIndex());
                        Stage stage = (Stage) root.getScene().getWindow();

                        Stage popupStage = new Stage();

                        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/popup_journey.fxml"));
                        Parent root = null;

                        try {
                            root = fxmlLoader.load();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                        databaseController controller = fxmlLoader.getController();

                        popupStage.setScene(new Scene(root));
                        popupStage.initOwner(stage);
                        popupStage.show();

                        live_validation_journey(controller);

                        controller.Journey_PlaneID.setText(String.valueOf(rowData.getPlaneID()));
                        controller.Journey_FlightNumber.setText(rowData.getFlightNumber());
                        controller.Journey_DepartureGate.setText(rowData.getDepartureGate());
                        controller.Journey_DepartureDateTime.setText(String.valueOf(rowData.getDepartureDateTime()));
                        controller.Journey_DelayedDateTime.setText(String.valueOf(rowData.getDelayedDateTime()));

                        rowData.getJourneyID();
                        controller.submitButton.setOnAction(e2 -> {
                            if(validate_data_journey(controller, popupStage)) {
                                String connectionQuery = "";
                                if(!controller.Journey_DelayedDateTime.getText().isEmpty() && !controller.Journey_DelayedDateTime.getText().equals("null")) {
                                    connectionQuery = "UPDATE JOURNEY" +
                                            " SET DepartureDateTime = \"" + controller.Journey_DepartureDateTime.getText()
                                            + "\", DelayedDateTime = \"" + controller.Journey_DelayedDateTime.getText() +
                                            "\", DepartureGate = \"" + controller.Journey_DepartureGate.getText() +
                                            "\", PlaneID = \"" + controller.Journey_PlaneID.getText() +
                                            "\", FlightNumber = \"" + controller.Journey_FlightNumber.getText() + "\"" +
                                            " WHERE JourneyID = " + rowData.getJourneyID() + ";";
                                }
                                else {
                                    connectionQuery = "UPDATE JOURNEY" +
                                            " SET DepartureDateTime = \"" + controller.Journey_DepartureDateTime.getText()
                                            + "\", DelayedDateTime = NULL"
                                            + ", DepartureGate = \"" + controller.Journey_DepartureGate.getText() +
                                            "\", PlaneID = \"" + controller.Journey_PlaneID.getText() +
                                            "\", FlightNumber = \"" + controller.Journey_FlightNumber.getText() + "\"" +
                                            " WHERE JourneyID = " + rowData.getJourneyID() + ";";
                                }

                                Connection connectDB = null;
                                try {
                                    connectDB = DatabaseConnection.connect();
                                    PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                                    pstmt.executeUpdate(connectionQuery);
                                    popupStage.close();

                                } catch (Exception ex) {
                                    show_error_stage(new Stage(), "Could not Perform This Action.");
                                }
                                try {
                                    btn2.setDisable(true);
                                    btn1.setDisable(true);
                                    refresh_journey();
                                    btn2.setDisable(false);
                                    btn1.setDisable(false);
                                } catch (Exception ex2) {
                                    throw new RuntimeException(ex2);
                                }
                            }

                        });
                        controller.cancelButton.setOnAction(e3 -> {popupStage.close();});
                    });
                    btn2.setOnAction(e -> {
                        database.Journey rowData = getTableView().getItems().get(getIndex());
                        boolean return_value = confirm_delete();

                        Connection connectDB = null;
                        try {
                            connectDB = DatabaseConnection.connect();
                            String deleteBefore = "DELETE FROM TICKET WHERE JourneyID=" + rowData.getJourneyID() + ";";
                            String connectionQuery = "DELETE FROM JOURNEY WHERE JourneyID=" + rowData.getJourneyID() + ";";

                            PreparedStatement to_delete = connectDB.prepareStatement(deleteBefore);
                            to_delete.executeUpdate(deleteBefore);

                            PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                            pstmt.executeUpdate(connectionQuery);

                        } catch (Exception ex) {
                            show_error_stage(new Stage(), "Could not Perform This Action.");
                        }
                        if(return_value==true) {
                            try {
                                btn2.setDisable(true);
                                btn1.setDisable(true);
                                refresh_journey();
                                btn2.setDisable(false);
                                btn1.setDisable(false);
                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        }

                    });
                }
                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(box);
                    }
                }
            });



        } catch (Exception e) {
            e.printStackTrace();
        }

        journeyTableView.getColumns().addAll(JourneyID, DepartureDateTime, DelayedDateTime, DepartureGate, PlaneID, FlightNumber, Actions);
    }

    public class validation {
        public static void live_validation_flight(databaseController controller) {
            controller.Flight_Duration.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                try {
                    int value = Integer.parseInt(newValue);
                    if(value < 0 || value > 20) {controller.error_msg.setText("Invalid Duration"); controller.submitButton.setDisable(true);}
                }
                catch(Exception exc) {controller.error_msg.setText("Invalid Duration"); controller.submitButton.setDisable(true);}
            });
            controller.Flight_FlightNumber.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                if (!controller.Flight_FlightNumber.getText().matches("[A-Z]{2,6}[0-9]{1,7}")) {
                    controller.error_msg.setText("Invalid Flight Number");
                    controller.submitButton.setDisable(true);
                }
            });
            controller.Flight_AirlineID.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                try {
                    int value = Integer.parseInt(newValue);
                    if(value < 0 || value> 2000) {controller.error_msg.setText("Invalid AirlineID"); controller.submitButton.setDisable(true);}
                }
                catch(Exception exc) {controller.error_msg.setText("Invalid AirlineID"); controller.submitButton.setDisable(true);}
            });
        }
        public static void live_validation_journey(databaseController controller) {

            controller.Journey_DepartureGate.textProperty().addListener(
                    (observable, oldValue, newValue) -> {
                        controller.submitButton.setDisable(false);
                        controller.error_msg.setText("");

                        if(!newValue.matches("[A-Z]{1}[0-9]*")) {
                            controller.error_msg.setText("Invalid Departure Gate");
                            controller.submitButton.setDisable(true);
                        }
                    }
            );

            controller.Journey_DepartureDateTime.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                if(!newValue.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                    controller.error_msg.setText("Invalid DateTime");
                    controller.submitButton.setDisable(true);
                }
            });
            controller.Journey_DelayedDateTime.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                if(!newValue.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d") && !newValue.isEmpty() && !newValue.equalsIgnoreCase("null")) {
                    controller.error_msg.setText("Invalid DateTime");
                    controller.submitButton.setDisable(true);
                }
            });
            controller.Journey_FlightNumber.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                if (!controller.Journey_FlightNumber.getText().matches("[A-Z]{2,6}[0-9]{1,7}")) {
                    controller.error_msg.setText("Invalid Flight Number");
                    controller.submitButton.setDisable(true);
                }
            });
            controller.Journey_PlaneID.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                try {
                    int out = Integer.parseInt(newValue);
                } catch (NumberFormatException EC) {
                    controller.error_msg.setText("Invalid PlaneID");
                    controller.submitButton.setDisable(true);
                }
            });
        }
        public static void live_validation_airline(databaseController controller) {
            controller.Airline_AirlineName.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                if(!newValue.matches("[a-zA-Z ]+")) {
                    controller.error_msg.setText("Invalid Airline Name");
                    controller.submitButton.setDisable(true);
                }

            });
        }
        public static void live_validation_plane(databaseController controller) {

            controller.Plane_FlightAttendants.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                try {
                    int value = Integer.parseInt(newValue);
                    if(value < 0 || value> 20) {controller.error_msg.setText("Invalid Number of Flight Attendants."); controller.submitButton.setDisable(true);}
                }
                catch(Exception exc) {controller.error_msg.setText("Invalid Number of Flight Attendants."); controller.submitButton.setDisable(true);}
            });
            controller.Plane_Passengers.textProperty().addListener((observable, oldValue, newValue) -> {
                controller.submitButton.setDisable(false);
                controller.error_msg.setText("");

                try {
                    int value = Integer.parseInt(newValue);
                    if(value < 0 || value> 900) {controller.error_msg.setText("Invalid Number of Passengers."); controller.submitButton.setDisable(true);}
                }
                catch(Exception exc) {controller.error_msg.setText("Invalid Number of Passengers."); controller.submitButton.setDisable(true);}
            });

        }
        public static boolean validate_data_airline(databaseController controller, Stage popupStage) {
            if(!controller.Airline_AirlineName.getText().isEmpty()) {
                if(controller.Airline_AirlineName.getText().matches("[a-zA-Z ]+")) {
                    return true;
                }
                else {
                    show_error_stage(popupStage, "Invalid Airline Name");
                    return false;
                }
            }
            else {
                return false;
            }
        }

        public static boolean validate_data_plane(databaseController controller, Stage popupStage) {
            if(!controller.Plane_Passengers.getText().isEmpty() && !controller.Plane_PlaneLayout.getText().isEmpty()
                    && !controller.Plane_Model.getText().isEmpty() && !controller.Plane_Manufacturer.getText().isEmpty()) {

                String error_message = "";
                try {
                    new URL(controller.Plane_PlaneLayout.getText());

                    int passengers = Integer.parseInt(controller.Plane_Passengers.getText());
                    int attendants = Integer.parseInt(controller.Plane_FlightAttendants.getText());

                    if (passengers <= 0 || attendants <= 0 || passengers > 900 || attendants > 20) {
                        error_message = "Abnormal Number Values (Passengers/Attendants)";
                    }

                } catch (MalformedURLException ex) {
                    error_message = "Invalid URL";
                } catch (NumberFormatException ex) {
                    error_message = "Please enter valid numbers";
                }

                if (!error_message.isEmpty()) {
                    show_error_stage(popupStage, error_message);
                    return false;

                } else {
                    return true;
                }
            }
            else {
                show_error_stage(popupStage, "Please ensure all fields are filled");
                return false;
            }
        }

        public static boolean validate_data_journey(databaseController controller, Stage popupStage) {
            if(!controller.Journey_DepartureDateTime.getText().isEmpty() && !controller.Journey_PlaneID.getText().isEmpty()
                    && !controller.Journey_DepartureGate.getText().isEmpty()) {

                if(!controller.Journey_DepartureGate.getText().matches("[A-Z]{1}[0-9]*")) {
                    show_error_stage(popupStage, "Incorrect Gate Format");
                    return false;
                }

                String time = controller.Journey_DepartureDateTime.getText();
                String delayed_time = controller.Journey_DelayedDateTime.getText();

                if(!time.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                    show_error_stage(popupStage, "Incorrect DateTime Format");
                    return false;
                }

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime dateTime = LocalDateTime.parse(time, formatter);
                LocalDateTime now = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Singapore")));

                if(now.isAfter(dateTime)) {
                    show_error_stage(popupStage, "Can only set Future Flights");
                    return false;
                }

                if(!(delayed_time.isEmpty() && !delayed_time.equalsIgnoreCase("null")) && !delayed_time.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                    show_error_stage(popupStage, "Incorrect DateTime Format");
                    return false;
                }
                else if(delayed_time.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                    LocalDateTime delayedTime = LocalDateTime.parse(delayed_time, formatter);

                    if(!delayedTime.isAfter(dateTime)) {
                        show_error_stage(popupStage, "Delayed Time must be after original time");
                        return false;
                    }

                }

                String verification = "SELECT * FROM PLANE WHERE PlaneID = " + controller.Journey_PlaneID.getText() + ";";
                Connection connectDB = null;

                try {
                    connectDB = DatabaseConnection.connect();
                    Statement statement = connectDB.createStatement();
                    ResultSet queryOutput = statement.executeQuery(verification);

                    if(!queryOutput.next()) {
                        show_error_stage(popupStage, "No Plane Was Found With a Matching PlaneID");
                        return false;
                    }
                    else {
                        String verification_flight = "SELECT * FROM FLIGHT WHERE FlightNumber = \"" + controller.Journey_FlightNumber.getText() + "\"";
                        Connection connectDB2 = null;

                        try {
                            connectDB2 = DatabaseConnection.connect();
                            Statement statement2 = connectDB2.createStatement();
                            ResultSet queryOutput2 = statement2.executeQuery(verification_flight);

                            if (!queryOutput2.next()) {
                                show_error_stage(popupStage, "No Flight Was Found With a Matching Flight Number");
                                return false;
                            } else {
                                return true;
                            }
                        } catch (Exception e) {
                            show_error_stage(popupStage, "An Error Occurred");
                            return false;
                        }

                    }

                } catch (Exception e) {
                    show_error_stage(popupStage, "Invalid PlaneID");
                    return false;
                }

            }
            else {
                show_error_stage(popupStage, "Please Ensure All Fields Are Filled");
                return false;
            }
        }

        public static boolean validate_data_flight(databaseController controller, Stage popupStage, boolean insert) {
            if(!controller.Flight_FlightNumber.getText().isEmpty() && !controller.Flight_Origin.getText().isEmpty() &&
                    !controller.Flight_AirlineID.getText().isEmpty() && !controller.Flight_Duration.getText().isEmpty() &&
                    !controller.Flight_Destination.getText().isEmpty() ) {

                String error_message = "";

                String connectionQuery = "SELECT * from AIRLINE WHERE AirlineID=" + controller.Flight_AirlineID.getText() + ";";
                Connection connectDB = null;
                String flight_confirm = "SELECT * FROM FLIGHT WHERE FlightNumber= \"" + controller.Flight_FlightNumber.getText() + "\";";

                try {
                    connectDB = DatabaseConnection.connect();
                    Statement statement = connectDB.createStatement();

                    if(insert) {
                        ResultSet out = statement.executeQuery(flight_confirm);
                        if (out.next()) {
                            error_message = "A Flight With That FlightNumber Already Exists";
                        }
                    }

                    ResultSet queryOutput = statement.executeQuery(connectionQuery);
                    Integer duration = 0;
                    Integer airlineID = 0;

                    try {
                        duration = Integer.valueOf(controller.Flight_Duration.getText());
                        airlineID = Integer.valueOf(controller.Flight_AirlineID.getText());

                    } catch (NumberFormatException ex) {
                        error_message = "enter an integer";
                    }


                    if (!queryOutput.next()) {
                        error_message = "No airline matching AirlineID " + airlineID;
                    } else if (duration <= 0 || duration > 20) {
                        error_message = "Invalid Flight Duration";
                    } else if (!controller.Flight_FlightNumber.getText().matches("[A-Z]{2,6}[0-9]{1,7}")) {
                        error_message = "Invalid Flight Number Format";
                    }

                    if (!error_message.isEmpty()) {
                        show_error_stage(popupStage, error_message);
                        return false;
                    } else {
                        return true;
                    }

                } catch (Exception e) {
                    show_error_stage(popupStage, "Invalid AirlineID");
                    e.printStackTrace();
                    return false;
                }
            }
            else {
                show_error_stage(popupStage, "Please ensure all fields are filled");
                return false;

            }

        }
    }
}
