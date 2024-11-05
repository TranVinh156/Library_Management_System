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
    requires java.sql;
    requires java.desktop;
    requires com.google.zxing.javase;
    requires opencv;
    requires com.google.zxing;
    requires annotations;
    requires mysql.connector.j;
    requires java.mail;
    requires com.google.api.services.books;

    opens com.ooops.lms.library_management_system to javafx.fxml;
    opens com.ooops.lms.controller to javafx.fxml;
    exports com.ooops.lms.library_management_system;
}