package com.ooops.lms.controller;

import com.ooops.lms.animation.Animation;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import com.ooops.lms.util.ThemeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class InterfaceSettingController {
    @FXML
    VBox interfaceSettingBox, j97VBox;
    @FXML
    CheckBox soundCheckBox, characterCheckBox;
    @FXML
    ImageView j97Image;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String USER_MENU_FXML = "/com/ooops/lms/library_management_system/UserMenu-view.fxml";
    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";

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
            if (characterCheckBox.isSelected()) {
                Animation.getInstance().turnOnAnimation();
            } else {
                Animation.getInstance().turnOffAnimation();
            }
        });
    }

    /**
     * quay lại setting.
     * @param actionEvent khi ấn
     */
    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(SETTING_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        }
    }

    /**
     * thay màu giao diện thành màu be
     * @param mouseEvent
     */
    public void onBeigeColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("default.mp3");
    }

    /**
     * thay màu giao diện thành màu đen thùi lùi
     * @param mouseEvent
     */
    public void onBlackColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("dark");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("dark.mp3");
    }

    /**
     * thay màu giao diện thành màu hồng
     * @param mouseEvent
     */
    public void onPinkColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("pink");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("pink.mp3");
    }

    /**
     * thay màu giao diện thành màu j97
     * @param mouseEvent
     */
    public void onGoldColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("gold");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("jackLong.mp3");
        Animation.getInstance().changeSizeImage(j97Image, 150);
        Animation.getInstance().enlargeVBox(j97VBox, 100, 150);
        Animation.getInstance().zoomAnimation(j97Image);
    }

    /**
     * thay ảnh đại diện là j97.
     * @param actionEvent khi ấn
     */
    public void onJ97ButtonAction(ActionEvent actionEvent) {
        UserMenuController.getMember().getPerson().setImagePath("avatar/jack.jpg");
        try {
            UserMenuController userMenuController = FXMLLoaderUtil.getInstance().getController(USER_MENU_FXML);
            userMenuController.showInfo();
            SettingController settingController = FXMLLoaderUtil.getInstance().getController(SETTING_FXML);
            settingController.showInfo();
            InformationController informationController = FXMLLoaderUtil.getInstance().getController(INFORMATION_FXML);
            if(informationController != null) {
                informationController.showInfo();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
