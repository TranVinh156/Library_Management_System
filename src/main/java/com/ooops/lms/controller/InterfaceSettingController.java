package com.ooops.lms.controller;

import com.ooops.lms.animation.Animation;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import com.ooops.lms.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class InterfaceSettingController {
    @FXML
    VBox interfaceSettingBox;
    @FXML
    CheckBox soundCheckBox,characterCheckBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    public void initialize() {
        soundCheckBox.setSelected(true);

        soundCheckBox.setOnAction(event -> {
            if (soundCheckBox.isSelected()) {
                Sound.getInstance().turnOnSound();
            } else {
                Sound.getInstance().turnOffSound();
            }
        });

        characterCheckBox.setSelected(true);

        characterCheckBox.setOnAction(event -> {
            if(characterCheckBox.isSelected()) {
                Animation.getInstance().turnOnAnimation();
            } else {
                Animation.getInstance().turnOffAnimation();
            }
        });
    }

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(SETTING_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        }
    }

    public void onBeigeColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("default.mp3");
    }

    public void onBlackColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("dark");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("dark.mp3");
    }

    public void onPinkColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("pink");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("pink.mp3");
    }

    public void onGoldColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("gold");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("gold.mp3");

    }
}
