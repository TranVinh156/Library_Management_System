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
    requires com.google.common;
    requires google.api.client;
    requires com.google.api.client;
    requires google.api.services.youtube.v3.rev222;
    requires com.google.api.client.json.jackson2;
    requires org.checkerframework.checker.qual;

    opens com.ooops.lms.library_management_system to javafx.fxml;
    opens com.ooops.lms.controller to javafx.fxml;
    opens com.ooops.lms to javafx.fxml;
    exports com.ooops.lms.library_management_system;
    exports com.ooops.lms;
    exports com.ooops.lms.controller;
    exports com.ooops.lms.model;
    exports com.ooops.lms.Command;
    opens com.ooops.lms.Command to javafx.fxml;
    exports com.ooops.lms.Alter;
    opens com.ooops.lms.Alter to javafx.fxml;
}