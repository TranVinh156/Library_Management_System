package com.ooops.lms.library_management_system;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class ExMain extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        //fxml
        FXMLLoader fxmlLoader = new FXMLLoader(ExMain.class.getResource("UserMenu-view.fxml"));
        Parent root = fxmlLoader.load();

        //css
        Scene scene = new Scene(root);

        //show
        stage.setTitle("GiaoDienDemo!");
        //stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Logo.png")));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}