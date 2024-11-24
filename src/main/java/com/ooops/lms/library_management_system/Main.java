package com.ooops.lms.library_management_system;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/UserLogin.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            //stage.setResizable(false);
            stage.setOnCloseRequest(event -> {
                System.exit(0);
                Platform.exit();
            });
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
