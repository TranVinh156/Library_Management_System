module com.ooops.lms.library_management_system {
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
    requires java.desktop;
    requires annotations;
    requires java.mail;
    requires com.google.zxing.javase;
    requires com.google.zxing;
    requires opencv;
    requires java.net.http;
    requires com.google.gson;
    requires java.sql;
    requires mysql.connector.j;
    requires org.json;
    requires com.google.api.services.books;


    opens com.ooops.lms.library_management_system to javafx.fxml;
    opens com.ooops.lms.controller to javafx.fxml;
    exports com.ooops.lms.library_management_system;
}