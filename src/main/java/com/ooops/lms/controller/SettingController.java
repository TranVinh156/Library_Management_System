package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class SettingController {
    @FXML
    private VBox settingBox;
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";
    private static final String HISTORY_FXML = "/com/ooops/lms/library_management_system/History-view.fxml";
    private static final String INTERFACE_SETTING_FXML = "/com/ooops/lms/library_management_system/InterfaceSetting-view.fxml";

    public void onInfomationButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(INFORMATION_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onHistoryButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(HISTORY_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onInterfaceSettingButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(INTERFACE_SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }
}
