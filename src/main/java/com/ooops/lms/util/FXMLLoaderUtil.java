package com.ooops.lms.util;

import com.ooops.lms.controller.UserMenuController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
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
    private Map<String, Object> controllerCache = new HashMap<>();
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
        if (instance == null) {
            instance = new FXMLLoaderUtil(container);
        } else {
            instance.container = container;  // Đảm bảo cập nhật container nếu instance đã tồn tại
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
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL resource = FXMLLoaderUtil.class.getResource(fxmlPath);
            fxmlLoader.setLocation(resource);
            Pane newContent = fxmlLoader.load();
            controllerCache.put(fxmlPath, fxmlLoader.getController());
            fxmlCache.put(fxmlPath, newContent);
            ThemeManager.getInstance().addPane(newContent);
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

    public <T> T getController(String fxmlPath) throws IOException {
        return (T) controllerCache.get(fxmlPath); // Trả về controller đã lưu trong cache
    }

    public void addUserMenuController(UserMenuController userMenuController) {
        controllerCache.put("/com/ooops/lms/library_management_system/UserMenuController-view.fxml",userMenuController);
    }

    public void deleteAllInCache() {
        controllerCache.clear();
        fxmlCache.clear();
    }
}
