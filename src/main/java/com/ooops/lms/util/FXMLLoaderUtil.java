package com.ooops.lms.util;

import com.ooops.lms.controller.SettingController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import com.ooops.lms.controller.UserMenuController;

public class FXMLLoaderUtil {

    private static FXMLLoaderUtil instance = null;
    private Map<String, Pane> fxmlCache = new HashMap<>();
    private Map<String, Object> controllerCache = new HashMap<>();
    private VBox container;

    private static final String USER_MENU_FXML = "/com/ooops/lms/library_management_system/UserMenu-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";


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

    public void addUserMenuController(Object o) {
        controllerCache.put(USER_MENU_FXML,o);
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

    public void deleteAllInCache() {
        fxmlCache.clear();
        controllerCache.clear();
    }

    public void updateInfo() {
        try {
            UserMenuController menuController = getController(USER_MENU_FXML);
            menuController.showInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            SettingController settingController = getController(SETTING_FXML);
            settingController.showInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public <T> T getController(String fxmlPath) throws IOException {
        return (T) controllerCache.get(fxmlPath);
    }

}
