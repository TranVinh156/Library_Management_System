package com.ooops.lms.controller;

import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class InterfaceSettingController {
    @FXML
    VBox interfaceSettingBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void onBeigeColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
    }

    public void onBlackColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("dark");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
    }

    public void onPinkColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("pink");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
    }

    public void onGoldColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("gold");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
    }
}
