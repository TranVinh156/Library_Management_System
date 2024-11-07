package com.ooops.lms.util;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class FXMLLoaderUtil {
    private static FXMLLoaderUtil instance = null;
    private Map<String, Pane> fxmlCache = new HashMap<>();

    private VBox container;

    private FXMLLoaderUtil() {
    }

    private FXMLLoaderUtil(VBox container) {
        this.container = container;
    }

    public static FXMLLoaderUtil getInstance() {
        if(instance == null) {
            instance = new FXMLLoaderUtil();
        }
        return instance;
    }

    public static FXMLLoaderUtil getInstance(VBox container) {
        if(instance == null) {
            instance = new FXMLLoaderUtil(container);
        }
        return instance;
    }

    public static <T> T loadAndGetController(String fxmlPath) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL resource = FXMLLoaderUtil.class.getResource(fxmlPath);
        fxmlLoader.setLocation(resource);
        Pane loaded = fxmlLoader.load();
        return fxmlLoader.getController();
    }

    // load and update scence
    public Pane loadFXML(String fxmlPath) {
        try {
            if (fxmlCache.containsKey(fxmlPath)) {
                return fxmlCache.get(fxmlPath);
            }

            Pane newContent = javafx.fxml.FXMLLoader.load(getClass().getResource(fxmlPath));
            ThemeManager.getInstance().addPane(newContent);
            fxmlCache.put(fxmlPath, newContent);
            return newContent;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public VBox getContainer() {
        return container;
    }

    public void updateContentBox(VBox content) {
        container.setPadding(Insets.EMPTY);
        container.getChildren().clear();
        VBox.setVgrow(content, Priority.ALWAYS);
        container.getChildren().add(content);
    }
}
