module com.example.airport_management {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.net.http;
    requires htmlunit;
    requires java.sql;
    requires java.desktop;
    requires com.gluonhq.charm.glisten;

    opens com.example.airport_management to javafx.fxml;
    exports com.example.airport_management;

}