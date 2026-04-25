package com.example.airport_management.controller;

import com.example.airport_management.*;
import com.example.airport_management.models.DatabaseConnection;
import com.example.airport_management.models.database;
import com.example.airport_management.utilities.Session;
import com.example.airport_management.utilities.airlineLogo;
import com.example.airport_management.utilities.flightStatus;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;

import static com.example.airport_management.utilities.calculateTotal.befTaxTot;


public class departuresController {
    @FXML public TableView<database.TerminalFlightList> terminal1_table;
    @FXML public TableView<database.TerminalFlightList> terminal2_table;
    @FXML public AnchorPane root;
    @FXML public Spinner<Integer> quantity_choice;
    @FXML public ChoiceBox<String> seat_type;
    @FXML public Button submitButton;
    @FXML public Button cancelButton;

    @FXML public VBox flights;
    @FXML public Button history_btn;
    @FXML public Button back;

    @FXML public Label error_label;
    @FXML public Label net_total;
    @FXML public Label gross_total;

    @FXML public TextField cardNumber;
    @FXML public TextField expiryMonth;
    @FXML public TextField expiryYear;

    @FXML public Label seat_type_label;
    @FXML public Label quantity_label;


    public void home(ActionEvent event) throws IOException {
        home.home(event);}

    public void add_columns_departures() throws Exception {
        if(!Session.getInstance().isAuthenticated()) {
            history_btn.setDisable(true);
            history_btn.setVisible(false);
        }

        terminal1_table.setEditable(false);
        terminal2_table.setEditable(false);

        TableColumn airlineLogo_t1 = new TableColumn("");
        airlineLogo_t1.setMaxWidth(80);
        airlineLogo_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, ImageView>("airlineLogo"));
        airlineLogo_t1.getStyleClass().add("table_column");

        TableColumn flightNumber_t1 = new TableColumn("Flight No.");
        flightNumber_t1.setPrefWidth(130);
        flightNumber_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("flightNumber"));
        flightNumber_t1.getStyleClass().add("table_column");

        TableColumn destination_t1 = new TableColumn("Destination");
        destination_t1.setMinWidth(160);
        destination_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("destination"));
        destination_t1.getStyleClass().add("table_column");

        TableColumn departureTime_t1 = new TableColumn("Departure");
        departureTime_t1.setMinWidth(117.14);
        departureTime_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, Label>("departureTime"));
        departureTime_t1.getStyleClass().add("table_column");

        TableColumn gate_t1 = new TableColumn("Gate");
        gate_t1.setMinWidth(50);
        gate_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("gate"));
        gate_t1.getStyleClass().add("table_column");

        TableColumn status_t1 = new TableColumn("Status");
        status_t1.setMinWidth(117.14);
        status_t1.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("status"));
        status_t1.getStyleClass().add("table_column");


        TableColumn<database.TerminalFlightList, Void> Actions_t1 = new TableColumn("Actions");
        Actions_t1.setMinWidth(219);

        terminal1_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        terminal2_table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        DecimalFormat df = new DecimalFormat("#.00");


        Actions_t1.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Book");
            private final Button btn2 = new Button("Info");
            private final HBox box = new HBox(10, btn1, btn2);
            {
                if(!Session.getInstance().isAuthenticated()) {
                    btn1.setVisible(false);
                    btn1.setManaged(false);
                    HBox.setMargin(btn2, new Insets(0, 0, 0, 75));
                }
                btn1.getStyleClass().add("button_database");
                ImageView book = new ImageView(main.class.getResource("/com/example/airport_management/static/book.png").toExternalForm());
                book.setFitHeight(14);
                book.setFitWidth(14);
                btn1.setGraphic(new HBox(book));

                btn2.getStyleClass().add("button_database");
                ImageView Info = new ImageView(main.class.getResource("/com/example/airport_management/static/details.png").toExternalForm());
                Info.setFitHeight(14);
                Info.setFitWidth(14);
                btn2.setGraphic(new HBox(Info));

                HBox.setMargin(btn1, new Insets(0, 0, 0, 45));


                btn1.setOnAction(e -> {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());
                    Stage stage = (Stage) root.getScene().getWindow();

                    Stage booking = new Stage();
                    FXMLLoader bookingLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/booking.fxml"));

                    Parent bookingRoot = null;

                    try {
                        bookingRoot = bookingLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    departuresController departuresController = bookingLoader.getController();


                    booking.setScene(new Scene(bookingRoot));
                    booking.initOwner(stage);
                    booking.show();

                    departuresController.seat_type.getSelectionModel().selectedItemProperty().addListener(
                            (obs, oldVal, newVal) -> {
                                departuresController.submitButton.setDisable(false);
                                departuresController.error_label.setText("");

                                if(departuresController.quantity_choice.getValue() > 0) {
                                    double total = befTaxTot(newVal, departuresController.quantity_choice.getValue());

                                    departuresController.gross_total.setText("RM " + df.format(total));

                                    departuresController.net_total.setText("RM " + df.format(total * 1.05));
                                }
                                else {
                                    departuresController.error_label.setText("Please Select a Valid Number of Seats");
                                    departuresController.submitButton.setDisable(true);
                                }
                            }
                    );

                    departuresController.quantity_choice.valueProperty().addListener(
                            (obs, oldVal, newVal) -> {
                                departuresController.error_label.setText("");
                                departuresController.submitButton.setDisable(false);

                                if(departuresController.seat_type.getValue() != null) {

                                    double total = befTaxTot(departuresController.seat_type.getValue(), newVal);

                                    departuresController.gross_total.setText("RM " + total);
                                    departuresController.net_total.setText("RM " + (total * 1.05));
                                }
                                else {
                                    departuresController.error_label.setText("Please Select a Seat Type");
                                    departuresController.submitButton.setDisable(true);
                                }
                            }
                    );

                    departuresController.cancelButton.setOnAction(event -> {
                        booking.close();
                    });

                    departuresController.submitButton.setOnAction(event -> {
                        String gross = departuresController.gross_total.getText();
                        String net = departuresController.net_total.getText();
                        String seatType = departuresController.seat_type.getValue();
                        Integer quantity = departuresController.quantity_choice.getValue();

                        if(!gross.isEmpty() && !net.isEmpty()) {
                            if (Float.parseFloat(gross.substring(3)) > 0 && Float.parseFloat(net.substring(3)) > 0) {
                                Stage checkout = new Stage();
                                FXMLLoader checkoutLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/checkout.fxml"));
                                Parent checkoutRoot = null;

                                try {
                                    checkoutRoot = checkoutLoader.load();
                                } catch (IOException ex) {
                                    throw new RuntimeException(ex);
                                }

                                departuresController checkoutLoaderController = checkoutLoader.getController();

                                checkoutLoaderController.net_total.setText(net);
                                checkoutLoaderController.gross_total.setText(gross);
                                checkoutLoaderController.seat_type_label.setText(seatType);
                                checkoutLoaderController.quantity_label.setText(String.valueOf(quantity));

                                checkout.setScene(new Scene(checkoutRoot));
                                checkout.initOwner(stage);
                                checkout.show();

                                checkoutLoaderController.cardNumber.textProperty().addListener((observable, oldValue, newValue) -> {
                                    checkoutLoaderController.error_label.setText("");
                                    checkoutLoaderController.submitButton.setDisable(false);

                                    if(!newValue.matches("^[0-9 ]{13,19}$")) {
                                        checkoutLoaderController.error_label.setText("Invalid Card Number.");
                                        checkoutLoaderController.submitButton.setDisable(true);
                                    }

                                });

                                checkoutLoaderController.expiryMonth.textProperty().addListener((observable, oldValue, newValue) -> {
                                    checkoutLoaderController.error_label.setText("");
                                    checkoutLoaderController.submitButton.setDisable(false);

                                    try {
                                        int value = Integer.parseInt(newValue);

                                        if (!(value <= 12 && value >= 1)) {
                                            checkoutLoaderController.error_label.setText("Invalid Expiry Month.");
                                            checkoutLoaderController.submitButton.setDisable(true);
                                        }
                                    } catch (RuntimeException ex) {
                                        checkoutLoaderController.error_label.setText("Invalid Expiry Month.");
                                        checkoutLoaderController.submitButton.setDisable(true);
                                    }
                                });

                                checkoutLoaderController.expiryYear.textProperty().addListener((observable, oldValue, newValue) -> {
                                    checkoutLoaderController.error_label.setText("");
                                    checkoutLoaderController.submitButton.setDisable(false);

                                    try {
                                        int value = Integer.parseInt(newValue);

                                        if(!(26 <= value && value <= 34)) {
                                            checkoutLoaderController.error_label.setText("Expiry Year must be between 2026 and 2034.");
                                            checkoutLoaderController.submitButton.setDisable(true);
                                        }
                                    } catch (RuntimeException ex) {
                                        checkoutLoaderController.error_label.setText("Invalid Expiry Year.");
                                        checkoutLoaderController.submitButton.setDisable(true);
                                    }
                                });



                                checkoutLoaderController.submitButton.setOnAction(event2 -> {
                                    boolean insert = true;
                                    String card = checkoutLoaderController.cardNumber.getText();
                                    checkoutLoaderController.error_label.setText("");
                                    checkoutLoaderController.submitButton.setDisable(false);

                                    if(!card.matches("^[0-9 ]{13,19}$")) {
                                        checkoutLoaderController.error_label.setText("Your Card Number is Invalid.");
                                        checkoutLoaderController.submitButton.setDisable(true);
                                        insert= false;
                                    }

                                    try {
                                        Integer expiry_month = Integer.parseInt(checkoutLoaderController.expiryMonth.getText());
                                        Integer expiry_year = Integer.parseInt(checkoutLoaderController.expiryYear.getText());

                                        if(!(1 <= expiry_month && expiry_month <= 12)) {
                                            checkoutLoaderController.error_label.setText("Invalid Expiry Month.");
                                            checkoutLoaderController.submitButton.setDisable(true);
                                            insert= false;
                                        }

                                        if(!(26 <= expiry_year && expiry_year <= 34)) {
                                            checkoutLoaderController.error_label.setText("Expiry Year must be between 2026 and 2034.");
                                            checkoutLoaderController.submitButton.setDisable(true);
                                            insert = false;
                                        }

                                        if(expiry_month < 4 && expiry_year <= 26 || expiry_year <= 25) {
                                            checkoutLoaderController.error_label.setText("Your Card is Expired.");
                                            checkoutLoaderController.submitButton.setDisable(true);
                                            insert=false;
                                        }

                                    }
                                    catch(Exception ex) {
                                        checkoutLoaderController.error_label.setText("Ensure the Numbers Entered Are Valid.");
                                        checkoutLoaderController.submitButton.setDisable(true);
                                        insert = false;
                                    }

                                    String connectionQuery = "";
                                    int ticketType = 0;
                                    int cost;

                                    if(insert) {
                                        switch (seatType) {
                                            case "Economy Class":
                                                ticketType = 1;

                                                break;
                                            case "Premium Economy":
                                                ticketType = 2;
                                                break;

                                            case "Business Class":
                                                ticketType = 3;
                                                break;

                                            case "First Class":
                                                ticketType = 4;
                                                break;
                                        }

                                        connectionQuery = "INSERT INTO TICKET(JourneyID, TicketTypeID, UserID) " +
                                                "VALUES(" + rowData.getJourneyID() + ", " + ticketType + ", " +
                                                Session.getInstance().get_user_id() +
                                                ");";

                                        Connection connectDB = null;

                                        int quantity_2 = quantity;
                                        try {
                                            connectDB = DatabaseConnection.connect();
                                            PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);

                                            while (quantity_2 > 0) {
                                                pstmt.executeUpdate(connectionQuery);
                                                quantity_2--;
                                            }
                                            checkout.close();
                                            booking.close();

                                        } catch (Exception ex) {
                                            throw new RuntimeException(ex);
                                        }
                                    }
                                });

                                checkoutLoaderController.cancelButton.setOnAction(event2 -> {
                                    checkout.close();
                                });
                            }
                        }
                        else if(departuresController.seat_type.getValue() == null) {
                            departuresController.error_label.setText("Please Select a Seat Type");
                            departuresController.submitButton.setDisable(true);

                        }
                        else if(!(departuresController.quantity_choice.getValue() >= 1) ) {
                            departuresController.error_label.setText("Please Enter a Valid Number of Seats");
                            departuresController.submitButton.setDisable(true);

                        }

                    });


                });

                btn2.setOnAction(e -> {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(main.class.getResource("/com/example/airport_management/flight_details.fxml"));

                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    flightDetailController controller = loader.getController();
                    try {
                        controller.add_details(rowData.getJourneyID());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());
                    if(rowData.getStatus().equals("Departed")) {
                        btn1.setDisable(true);
                    }

                    setGraphic(box);
                }
            }
        });


        TableColumn airlineLogo_t2 = new TableColumn("");
        airlineLogo_t2.setMaxWidth(80);
        airlineLogo_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, ImageView>("airlineLogo"));
        airlineLogo_t2.getStyleClass().add("table_column");

        TableColumn flightNumber_t2 = new TableColumn("Flight No");
        flightNumber_t2.setPrefWidth(130);
        flightNumber_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("flightNumber"));
        flightNumber_t2.getStyleClass().add("table_column");

        TableColumn destination_t2 = new TableColumn("Destination");
        destination_t2.setMinWidth(160);
        destination_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("destination"));
        destination_t2.getStyleClass().add("table_column");

        TableColumn departureTime_t2 = new TableColumn("Departure");
        departureTime_t2.setMinWidth(117.14);
        departureTime_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, Label>("departureTime"));
        departureTime_t2.getStyleClass().add("table_column");

        TableColumn gate_t2 = new TableColumn("Gate");
        gate_t2.setMinWidth(50);
        gate_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("gate"));
        gate_t2.getStyleClass().add("table_column");

        TableColumn status_t2 = new TableColumn("Status");
        status_t2.setMinWidth(117.14);
        status_t2.setCellValueFactory(
                new PropertyValueFactory<database.TerminalFlightList, String>("status"));
        status_t2.getStyleClass().add("table_column");


        TableColumn<database.TerminalFlightList, Void> Actions_t2 = new TableColumn("Actions");
        Actions_t2.setMinWidth(219);


        Actions_t2.setCellFactory(col -> new TableCell<>() {
            private final Button btn1 = new Button("Book");
            private final Button btn2 = new Button("Info");
            private final HBox box = new HBox(10, btn1, btn2);
            {
                if(!Session.getInstance().isAuthenticated()) {
                    btn1.setVisible(false);
                    btn1.setManaged(false);
                    HBox.setMargin(btn2, new Insets(0, 0, 0, 75));
                }


                btn1.getStyleClass().add("button_database");
                ImageView book = new ImageView(main.class.getResource("/com/example/airport_management/static/book.png").toExternalForm());
                book.setFitHeight(14);
                book.setFitWidth(14);
                btn1.setGraphic(new HBox(book));

                btn2.getStyleClass().add("button_database");
                ImageView Info = new ImageView(main.class.getResource("/com/example/airport_management/static/details.png").toExternalForm());
                Info.setFitHeight(14);
                Info.setFitWidth(14);
                btn2.setGraphic(new HBox(Info));

                HBox.setMargin(btn1, new Insets(0, 0, 0, 45));


                if(getTableView().getItems().get(getIndex()).getStatus().equals("Departed")) {
                    btn1.setDisable(true);
                }


                btn1.setOnAction(e -> {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());


                    Stage stage = (Stage) root.getScene().getWindow();

                    Stage booking = new Stage();
                    FXMLLoader bookingLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/booking.fxml"));
                    Parent bookingRoot = null;

                    try {
                        bookingRoot = bookingLoader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    departuresController departuresController = bookingLoader.getController();



                    booking.setScene(new Scene(bookingRoot));
                    booking.initOwner(stage);
                    booking.show();

                    departuresController.seat_type.getSelectionModel().selectedItemProperty().addListener(
                            (obs, oldVal, newVal) -> {
                                double total = befTaxTot(newVal, departuresController.quantity_choice.getValue());

                                departuresController.gross_total.setText("RM " + total);
                                departuresController.net_total.setText("RM " + (total * 1.05));
                            }
                    );

                    departuresController.quantity_choice.valueProperty().addListener(
                            (obs, oldVal, newVal) -> {
                                double total = befTaxTot(departuresController.seat_type.getValue(), newVal);

                                departuresController.gross_total.setText("RM " + total);
                                departuresController.net_total.setText("RM " + (total * 1.05));                            }
                    );

                    departuresController.cancelButton.setOnAction(event -> {
                        booking.close();
                    });

                    departuresController.submitButton.setOnAction(event -> {
                        String gross = departuresController.gross_total.getText();
                        String net = departuresController.net_total.getText();

                        Stage checkout = new Stage();
                        FXMLLoader checkoutLoader = new FXMLLoader(main.class.getResource("/com/example/airport_management/checkout.fxml"));
                        Parent checkoutRoot = null;

                        try {
                            checkoutRoot = checkoutLoader.load();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }

                        departuresController checkoutLoaderController = checkoutLoader.getController();

                        checkoutLoaderController.net_total.setText(net);
                        checkoutLoaderController.gross_total.setText(gross);

                        checkout.setScene(new Scene(checkoutRoot));
                        checkout.initOwner(stage);
                        checkout.show();

                        checkoutLoaderController.submitButton.setOnAction(event2 -> {
                            String seatType = departuresController.seat_type.getValue();
                            int quantity = departuresController.quantity_choice.getValue();

                            String connectionQuery = "";
                            int ticketType = 0;
                            int cost;



                            switch(seatType) {
                                case "Economy Class":
                                    ticketType = 1;

                                    break;
                                case "Premium Economy":
                                    ticketType = 2;
                                    break;

                                case "Business Class":
                                    ticketType = 3;
                                    break;

                                case "First Class":
                                    ticketType = 4;
                                    break;
                            }

                            connectionQuery = "INSERT INTO TICKET(JourneyID, TicketTypeID) " +
                                    "VALUES(" + rowData.getJourneyID() + ", " + ticketType +
                                    ");";

                            Connection connectDB = null;

                            try {
                                connectDB = DatabaseConnection.connect();
                                PreparedStatement pstmt = connectDB.prepareStatement(connectionQuery);

                                while(quantity > 0) {
                                    pstmt.executeUpdate(connectionQuery);
                                    quantity--;
                                }
                                checkout.close();
                                booking.close();

                            } catch (Exception ex) {
                                throw new RuntimeException(ex);
                            }
                        });

                        checkoutLoaderController.cancelButton.setOnAction(event2 -> {
                            checkout.close();
                        });

                    });


                });

                btn2.setOnAction(e -> {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());

                    FXMLLoader loader = new FXMLLoader(main.class.getResource("com/example/airport_management/flight_details.fxml"));

                    Parent root = null;
                    try {
                        root = loader.load();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    Scene scene = new Scene(root);
                    Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.show();

                    flightDetailController controller = loader.getController();
                    try {
                        controller.add_details(rowData.getJourneyID());
                    } catch (Exception ex) {
                        throw new RuntimeException(ex);
                    }

                });
            }
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    database.TerminalFlightList rowData = getTableView().getItems().get(getIndex());
                    if(rowData.getStatus().equals("Departed")) {
                        btn1.setDisable(true);
                    }
                    setGraphic(box);
                }

            }
        });

        Connection connectDB = DatabaseConnection.connect();
        ObservableList<database.TerminalFlightList> flights_t1;
        flights_t1 = FXCollections.observableArrayList();
        ObservableList<database.TerminalFlightList> flights_t2;
        flights_t2 = FXCollections.observableArrayList();

        String connectionQuery_t1 =
                "SELECT AIRLINE.AirlineID, JOURNEY.FlightNumber, FLIGHT.Destination, JOURNEY.DepartureDateTime, " +
                "JOURNEY.DelayedDateTime, PLANE.PlaneID, JOURNEY.DepartureGate, JOURNEY.JourneyID " +
                "FROM JOURNEY " +
                "INNER JOIN FLIGHT ON JOURNEY.FlightNumber = FLIGHT.FlightNumber " +
                "INNER JOIN AIRLINE ON FLIGHT.AirlineID = AIRLINE.AirlineID " +
                "INNER JOIN PLANE ON PLANE.PlaneID = JOURNEY.PlaneID " +
                "WHERE ABS(TIMESTAMPDIFF(HOUR, JOURNEY.DepartureDateTime, NOW())) < 13 " +
                "AND (JOURNEY.DepartureGate LIKE 'C%' OR JOURNEY.DepartureGate LIKE 'G%' OR JOURNEY.DepartureGate LIKE 'H%' OR JOURNEY.DepartureGate LIKE 'B%' OR JOURNEY.DepartureGate LIKE 'A%');";

        String connectionQuery_t2 =
                "SELECT AIRLINE.AirlineID, JOURNEY.FlightNumber, FLIGHT.Destination, JOURNEY.DepartureDateTime, " +
                "JOURNEY.DelayedDateTime, PLANE.PlaneID, JOURNEY.DepartureGate, JOURNEY.JourneyID " +
                "FROM JOURNEY " +
                "INNER JOIN FLIGHT ON JOURNEY.FlightNumber = FLIGHT.FlightNumber " +
                "INNER JOIN AIRLINE ON FLIGHT.AirlineID = AIRLINE.AirlineID " +
                "INNER JOIN PLANE ON PLANE.PlaneID = JOURNEY.PlaneID " +
                "WHERE ABS(TIMESTAMPDIFF(HOUR, JOURNEY.DepartureDateTime, NOW())) < 13 " +
                "AND (JOURNEY.DepartureGate LIKE 'P%' OR JOURNEY.DepartureGate LIKE 'J%' OR JOURNEY.DepartureGate LIKE 'K%' OR JOURNEY.DepartureGate LIKE 'L%' OR JOURNEY.DepartureGate LIKE 'Q%');";

        try {
            Statement statement_1 = connectDB.createStatement();
            ResultSet queryOutput = statement_1.executeQuery(connectionQuery_t1);

            while(queryOutput.next()) {
                int AirlineID = queryOutput.getInt("AirlineID");

                ImageView airlinelogo = new ImageView();
                String image_address = airlineLogo.get_logo(AirlineID);

                airlinelogo.setImage(new Image(main.class.getResource(image_address).toExternalForm()));

                airlinelogo.setFitWidth(30);
                airlinelogo.setFitHeight(30);

                String departure;
                if(queryOutput.getString("DelayedDateTime") != null) {
                    departure = queryOutput.getString("DelayedDateTime");
                }

                else {
                    departure = queryOutput.getString("DepartureDateTime");
                }

                flightStatus flightStatus = new flightStatus();
                String status = flightStatus.getStatus(departure);

                departure = departure.substring(11, 16);


                flights_t1.add(new database.TerminalFlightList(
                        new SimpleStringProperty(queryOutput.getString("FlightNumber")),
                        new SimpleStringProperty(queryOutput.getString("PlaneID")),
                        new SimpleStringProperty(departure),
                        airlinelogo,
                        new SimpleStringProperty(queryOutput.getString("Destination")),
                        new SimpleStringProperty(status),
                        new SimpleStringProperty(queryOutput.getString("DepartureGate")),
                        new SimpleIntegerProperty(queryOutput.getInt("JourneyID"))
                ));



            }


            Statement statement_2 = connectDB.createStatement();
            queryOutput = statement_2.executeQuery(connectionQuery_t2);
            while(queryOutput.next()) {
                int AirlineID = queryOutput.getInt("AirlineID");

                ImageView airlinelogo = new ImageView();
                String image_address = airlineLogo.get_logo(AirlineID);
                airlinelogo.setImage(new Image(main.class.getResource(image_address).toExternalForm()));

                airlinelogo.setFitWidth(30);
                airlinelogo.setFitHeight(30);

                String departure;
                if(queryOutput.getString("DelayedDateTime") != null) {
                    departure = queryOutput.getString("DelayedDateTime");
                }

                else {
                    departure = queryOutput.getString("DepartureDateTime");
                }

                flightStatus flightStatus = new flightStatus();
                String status = flightStatus.getStatus(departure);

                departure = departure.substring(11, 16);

                flights_t2.add(new database.TerminalFlightList(
                        new SimpleStringProperty(queryOutput.getString("FlightNumber")),
                        new SimpleStringProperty(queryOutput.getString("PlaneID")),
                        new SimpleStringProperty(departure),
                        airlinelogo,
                        new SimpleStringProperty(queryOutput.getString("Destination")),
                        new SimpleStringProperty(status),
                        new SimpleStringProperty(queryOutput.getString("DepartureGate")),
                        new SimpleIntegerProperty(queryOutput.getInt("JourneyID"))
                ));



            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        terminal1_table.setItems(flights_t1);
        terminal1_table.getColumns().addAll(airlineLogo_t1, flightNumber_t1, destination_t1, departureTime_t1, gate_t1, status_t1, Actions_t1);


        terminal2_table.getColumns().addAll(airlineLogo_t2, flightNumber_t2, destination_t2, departureTime_t2, gate_t2, status_t2, Actions_t2);
        terminal2_table.setItems(flights_t2);

    }

    @FXML
    public void order_history(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/airport_management/order_history.fxml"));
        Parent root = loader.load();

        departuresController controller = loader.getController();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        String query_booked = "SELECT JOURNEY.JourneyID, " +
                "SUM(" +
                "CASE" +
                "\n  WHEN TICKET.TicketTypeID = 1 THEN 100" +
                "\n  WHEN TICKET.TicketTypeID = 2 THEN 110" +
                "\n  WHEN TICKET.TicketTypeID = 3 THEN 120" +
                "\n  WHEN TICKET.TicketTypeID = 4 THEN 140" +
                "\nEND" +
                ") AS TOTAL" +
                " FROM JOURNEY" +
                " INNER JOIN TICKET" +
                " ON TICKET.JourneyID = JOURNEY.JourneyID" +
                " WHERE TICKET.UserID = " + Session.getInstance().get_user_id() +
                " GROUP BY JOURNEY.JourneyID" +
                " ORDER BY JOURNEY.JourneyID DESC" +
                " LIMIT 5;";


        Connection connectDB = DatabaseConnection.connect();
        String[][] array = new String[100][];

        try {
            Statement statement = connectDB.createStatement();
            Statement innerStatement = connectDB.createStatement();
            ResultSet queryOutput = statement.executeQuery(query_booked);

            while (queryOutput.next()) {
                String total = queryOutput.getString("TOTAL");
                int journey_id = queryOutput.getInt("JourneyID");

                String additional_details = "SELECT FLIGHT.Destination, FLIGHT.Destination, FLIGHT.Origin, DATE(JOURNEY.DepartureDateTime) AS DEPARTURE, " +
                                            "SUM(" +
                                            "CASE" +
                                            "\n  WHEN TICKET.TicketTypeID = 1 THEN 100" +
                                            "\n  WHEN TICKET.TicketTypeID = 2 THEN 110" +
                                            "\n  WHEN TICKET.TicketTypeID = 3 THEN 120" +
                                            "\n  WHEN TICKET.TicketTypeID = 4 THEN 140" +
                                            "\nEND" +
                                            ") AS TOTAL" +
                                            " FROM FLIGHT" +
                                            " INNER JOIN JOURNEY" +
                                            " ON JOURNEY.FlightNumber = FLIGHT.FlightNumber" +
                                            " INNER JOIN TICKET" +
                                            " ON TICKET.JourneyID = JOURNEY.JourneyID" +
                                            " INNER JOIN TICKETTYPE ON TICKET.TicketTypeID = TICKETTYPE.TicketTypeID" +
                                            " WHERE JOURNEY.JourneyID = " + journey_id + ";";

                try {
                    ResultSet additionalOutput = innerStatement.executeQuery(additional_details);

                    while(additionalOutput.next()) {
                        VBox bookingBox = new VBox(5); // 5 px spacing within this box
                        bookingBox.setStyle("-fx-border-color: #ccc; -fx-border-radius: 5; -fx-padding: 10;");

                        String departureDate = additionalOutput.getString("DEPARTURE"); // e.g., 2026-04-07
                        String origin = additionalOutput.getString("Origin");
                        String destination = additionalOutput.getString("Destination");
                        String totalPrice = additionalOutput.getString("TOTAL"); // e.g., "340"

                        // Container for one flight
                        VBox flightBox = new VBox(5); // spacing inside the box
                        flightBox.setStyle(
                                "-fx-border-color: #cccccc; " +
                                "-fx-border-radius: 5; " +
                                "-fx-background-radius: 5; " +
                                "-fx-padding: 10; " +
                                "-fx-background-color: #f9f9f9;"
                        );

                        Region spacer = new Region();

                        // Top row: Date and route
                        HBox topRow = new HBox(10);
                        HBox.setHgrow(spacer, Priority.ALWAYS);

                        Label dateLabel = new Label(departureDate);
                        dateLabel.setFont(Font.font("Arial", 16));
                        Label routeLabel = new Label(origin + " → " + destination);
                        routeLabel.setFont(Font.font("Arial", 16));

                        topRow.getChildren().addAll(dateLabel, spacer, routeLabel);

                        // Bottom row: Total price
                        Label totalLabel = new Label("Total spent: RM" + totalPrice);
                        totalLabel.setFont(Font.font("Arial", 14));
                        totalLabel.setStyle("-fx-font-weight: bold;");

                        flightBox.getChildren().addAll(topRow, totalLabel);

                        controller.flights.getChildren().addAll(flightBox);

                    }


                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void back(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(main.class.getResource("/com/example/airport_management/terminals_page.fxml"));

        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();

        departuresController controller = loader.getController();
        controller.add_columns_departures();
    }
}
