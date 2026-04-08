package com.example.airport_management;

//cd /Users/msajjad/IdeaProjects/airport_management/src/main/java/com/example/airport_management/

import javafx.beans.Observable;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

public class databaseController {
    @FXML private TableView<database.Plane> planeTableView;
    @FXML private TableView<database.Flight> flightTableView;
    @FXML private TableView<database.Airline> airlineTableView;
    @FXML private TableView<database.Journey> journeyTableView;

    @FXML private Button insertButton;
    @FXML private VBox root;
    @FXML private Label database_name;

    @FXML private Button submitButton;
    @FXML private Button cancelButton;

    @FXML private TextField Flight_AirlineID;
    @FXML private TextField Flight_Origin;
    @FXML private TextField Flight_Destination;
    @FXML private TextField Flight_Duration;
    @FXML private TextField Flight_FlightNumber;

    @FXML private TextField Journey_DepartureDateTime;
    @FXML private TextField Journey_DelayedDateTime;
    @FXML private TextField Journey_DepartureGate;
    @FXML private TextField Journey_PlaneID;
    @FXML private TextField Journey_FlightNumber;

    @FXML private Button yesButtonConfirm;
    @FXML private Button noButtonConfirm;

    @FXML private Label error_message;


    @FXML private TextField Plane_PlaneLayout;
    @FXML private TextField Plane_Manufacturer;
    @FXML private TextField Plane_Passengers;
    @FXML private TextField Plane_Model;
    @FXML private TextField Plane_FlightAttendants;
    @FXML private Button OKButton;

    @FXML private TextField Airline_AirlineName;

    ObservableList<database.Plane> planeList;
    ObservableList<database.Flight> flightList;
    ObservableList<database.Airline> airlineList;
    ObservableList<database.Journey> journeyList;

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

    boolean validate_data_airline(databaseController controller, Stage popupStage) {
        if(!controller.Airline_AirlineName.getText().isEmpty()) {
            if(controller.Airline_AirlineName.getText().matches("[a-zA-Z]+")) {
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

    boolean validate_data_plane(databaseController controller, Stage popupStage) {
        if(!controller.Plane_Passengers.getText().isEmpty() && !controller.Plane_PlaneLayout.getText().isEmpty()
                && !controller.Plane_Model.getText().isEmpty() && !controller.Plane_Manufacturer.getText().isEmpty()) {

            String error_message = "";
            try {
                new URL(controller.Plane_PlaneLayout.getText());

                int passengers = Integer.parseInt(controller.Plane_Passengers.getText());
                int attendants = Integer.parseInt(controller.Plane_FlightAttendants.getText());

                if (passengers <= 0 || attendants <= 0 || passengers > 300 || attendants > 20) {
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
        else {return false;}
    }

    boolean validate_data_journey(databaseController controller, Stage popupStage) {
        if(!controller.Journey_DepartureDateTime.getText().isEmpty() && !controller.Journey_PlaneID.getText().isEmpty()
        && !controller.Journey_DepartureGate.getText().isEmpty()) {

            String time = controller.Journey_DepartureDateTime.getText();
            String delayed_time = controller.Journey_DelayedDateTime.getText();

            if(!time.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                show_error_stage(popupStage, "Incorrect DateTime Format");
                return false;
            }
            if(!(delayed_time.isEmpty() || delayed_time.equals("null")) && !delayed_time.matches("\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]) ([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d")) {
                show_error_stage(popupStage, "Incorrect DateTime Format");
                return false;
            }

            String verification = "SELECT * FROM PLANE WHERE PlaneID = " + controller.Journey_PlaneID.getText();
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
                            show_error_stage(popupStage, "No Plane Was Found With a Matching PlaneID");
                            return false;
                        } else {
                            return true;
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        else {
            show_error_stage(popupStage, "Please Ensure All Fields Are Filled");
            return false;
        }
    }

    boolean validate_data_flight(databaseController controller, Stage popupStage) {
        if(!controller.Flight_FlightNumber.getText().isEmpty() && !controller.Flight_Origin.getText().isEmpty() &&
           !controller.Flight_AirlineID.getText().isEmpty() && !controller.Flight_Duration.getText().isEmpty() &&
           !controller.Flight_Destination.getText().isEmpty() ) {

            String error_message = "";

            flightList = FXCollections.observableArrayList();
            String connectionQuery = "SELECT * from FLIGHT WHERE AirlineID=" + controller.Flight_AirlineID.getText() + ";";
            Connection connectDB = null;

            try {
                connectDB = DatabaseConnection.connect();
                Statement statement = connectDB.createStatement();
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
                } else if (duration <= 0 || duration > 2000) {
                    error_message = "Invalid Flight Duration";
                } else if (!controller.Flight_FlightNumber.getText().matches("[a-zA-Z]{2}[0-9]{1,4}")) {
                    error_message = "Invalid Flight Number Format";
                }

                if (!error_message.isEmpty()) {
                    show_error_stage(popupStage, error_message);
                    return false;
                } else {
                    return true;
                }

            } catch (Exception e) {
                return false;
            }
        }
        else {
            show_error_stage(popupStage, "Please ensure all fields are filled");
            return false;

        }

    }

    void show_error_stage(Stage popupStage, String error_message) {
        Stage errorStage = new Stage();
        FXMLLoader errorLoader = new FXMLLoader(main.class.getResource("validationPopup.fxml"));
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


    void add_columns_plane() throws Exception {
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

        //button.setGraphic();
        //button.setStyle();
        //ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
        //edit.setFitHeight(20);
        //edit.setFitWidth(20);

        Actions.setCellFactory(col -> new TableCell<>() {
             private final Button btn1 = new Button("Edit");
             private final Button btn2 = new Button("Delete");
             private final HBox box = new HBox(10, btn1, btn2);

             {
                 btn1.getStyleClass().add("button_database");
                 ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
                 edit.setFitHeight(12);
                 edit.setFitWidth(12);
                 btn1.setGraphic(new HBox(edit));

                 btn2.getStyleClass().add("button_database");
                 ImageView delete = new ImageView(main.class.getResource("static/delete.png").toExternalForm());
                 delete.setFitHeight(12);
                 delete.setFitWidth(12);
                 btn2.setGraphic(new HBox(delete));

                 btn1.setOnAction(e -> {
                     database.Plane rowData = getTableView().getItems().get(getIndex());
                     Stage stage = (Stage) root.getScene().getWindow();

                     Stage popupStage = new Stage();
                     FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("popup_plane.fxml"));
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
                     try {
                         connectDB = DatabaseConnection.connect();
                         String connectionQuery = "DELETE FROM PLANE WHERE PlaneID=" + rowData.getPlaneID() + ";";
                         PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                         pstmt.executeUpdate(connectionQuery);

                     } catch (Exception ex) {
                         throw new RuntimeException(ex);
                     }
                     if(return_value==true) {
                         try {
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
        AtomicBoolean return_value = new AtomicBoolean(true);

        Stage popupStage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("confirmation_box.fxml"));
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("databases_entry.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }



    public void insertData() {
        Stage stage = (Stage) root.getScene().getWindow();

        Stage popupStage = new Stage();

        FXMLLoader fxmlLoader = null;
        switch(database_name.getText()){
            case "PLANE":
                fxmlLoader = new FXMLLoader(main.class.getResource("popup_plane.fxml"));
                break;

            case "FLIGHT":
                fxmlLoader = new FXMLLoader(main.class.getResource("popup_flight.fxml"));
                break;

            case "AIRLINE":
                fxmlLoader = new FXMLLoader(main.class.getResource("popup_airline.fxml"));
                break;

            case "JOURNEY":
                fxmlLoader = new FXMLLoader(main.class.getResource("popup_journey.fxml"));
                break;
        }
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


        controller.submitButton.setOnAction(e2 -> {
            String connectionQuery = "";
            switch(database_name.getText()){
                case "PLANE":
                    if(validate_data_plane(controller, popupStage) == true) {
                        connectionQuery = "INSERT INTO PLANE(Manufacturer, Model, Passengers, " +
                                "FlightAttendants, PlaneLayout) " +
                                "VALUES(\"" + controller.Plane_Manufacturer.getText() + "\", \"" + controller.Plane_Model.getText() +
                                "\", " + controller.Plane_Passengers.getText() + ", " +
                                controller.Plane_FlightAttendants.getText() + ", \"" + controller.Plane_PlaneLayout.getText() + "\");";
                    }
                    break;
                case "FLIGHT":
                    if(validate_data_plane(controller, popupStage) == true) {
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
                connectDB = DatabaseConnection.connect();
                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);
                pstmt.executeUpdate(connectionQuery);
                popupStage.close();

            } catch (Exception ex) {
                throw new RuntimeException(ex);
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

    void refresh_flight() {
        flightList.clear();

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
    void refresh_plane() {
        planeList.clear();

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
    void refresh_airline() {
        airlineList.clear();

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
    void refresh_journey() {
        journeyList.clear();

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
            e.printStackTrace();
        }

        journeyTableView.setItems(journeyList);
    }

    void add_columns_flight() throws Exception {
        flightTableView.setEditable(true);

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


        Actions.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Edit");
            private final Button btn2 = new Button("Delete");
            private final HBox box = new HBox(10, btn1, btn2);

            {
                btn1.getStyleClass().add("button_database");
                ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
                edit.setFitHeight(12);
                edit.setFitWidth(12);
                btn1.setGraphic(new HBox(edit));

                btn2.getStyleClass().add("button_database");
                ImageView delete = new ImageView(main.class.getResource("static/delete.png").toExternalForm());
                delete.setFitHeight(12);
                delete.setFitWidth(12);
                btn2.setGraphic(new HBox(delete));

                btn1.setOnAction(e -> {
                    database.Flight rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();

                    Stage popupStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("popup_flight.fxml"));
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

                    controller.submitButton.setOnAction(e2 -> {
                        if(validate_data_flight(controller, popupStage)) {
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
                            throw new RuntimeException(ex);
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
    void add_columns_airline() throws Exception {
        airlineTableView.setEditable(true);

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
                btn1.getStyleClass().add("button_database");
                ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
                edit.setFitHeight(12);
                edit.setFitWidth(12);
                btn1.setGraphic(new HBox(edit));

                btn2.getStyleClass().add("button_database");
                ImageView delete = new ImageView(main.class.getResource("static/delete.png").toExternalForm());
                delete.setFitHeight(12);
                delete.setFitWidth(12);
                btn2.setGraphic(new HBox(delete));

                btn1.setOnAction(e -> {
                    database.Airline rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();


                    Stage popupStage = new Stage();
                    FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("popup_airline.fxml"));
                    Parent root = null;
                    try {
                        root = fxmlLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    databaseController controller = fxmlLoader.getController();

                    controller.Airline_AirlineName.setText(rowData.getAirlineName());


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
                                throw new RuntimeException(ex);
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
                            throw new RuntimeException(ex);
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
    void add_columns_journey() throws Exception {
        journeyTableView.setEditable(true);

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
            //ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
            //edit.setFitHeight(20);
            //edit.setFitWidth(20);

            Actions.setCellFactory(col -> new TableCell<>() {
                private final Button btn1 = new Button("Edit");
                private final Button btn2 = new Button("Delete");
                private final HBox box = new HBox(10, btn1, btn2);

                {
                    btn1.getStyleClass().add("button_database");
                    ImageView edit = new ImageView(main.class.getResource("static/edit.png").toExternalForm());
                    edit.setFitHeight(12);
                    edit.setFitWidth(12);
                    btn1.setGraphic(new HBox(edit));

                    btn2.getStyleClass().add("button_database");
                    ImageView delete = new ImageView(main.class.getResource("static/delete.png").toExternalForm());
                    delete.setFitHeight(12);
                    delete.setFitWidth(12);
                    btn2.setGraphic(new HBox(delete));

                    btn1.setOnAction(e -> {
                        database.Journey rowData = getTableView().getItems().get(getIndex());
                        Stage stage = (Stage) root.getScene().getWindow();

                        Stage popupStage = new Stage();
                        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("popup_journey.fxml"));
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
                                            "\", FlightNumber = \"" + controller.Journey_FlightNumber.getText() + "\";";
                                }
                                else {
                                    connectionQuery = "UPDATE JOURNEY" +
                                            " SET DepartureDateTime = \"" + controller.Journey_DepartureDateTime.getText()
                                            + "\", DelayedDateTime = NULL"
                                            + ", DepartureGate = \"" + controller.Journey_DepartureGate.getText() +
                                            "\", PlaneID = \"" + controller.Journey_PlaneID.getText() +
                                            "\", FlightNumber = \"" + controller.Journey_FlightNumber.getText() + "\";";
                                }

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
                            throw new RuntimeException(ex);
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

}
