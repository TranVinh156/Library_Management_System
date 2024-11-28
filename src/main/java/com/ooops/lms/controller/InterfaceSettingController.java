package com.ooops.lms.controller;

import com.ooops.lms.animation.Animation;
import com.ooops.lms.util.FXMLLoaderUtil;
import com.ooops.lms.util.Sound;
import com.ooops.lms.util.ThemeManager;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;

public class InterfaceSettingController {
    @FXML
    VBox interfaceSettingBox, j97VBox;
    @FXML
    CheckBox soundCheckBox, characterCheckBox;
    @FXML
    ImageView j97Image,j97Gif1,j97Gif2;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private static final String USER_MENU_FXML = "/com/ooops/lms/library_management_system/UserMenu-view.fxml";
    private static final String INFORMATION_FXML = "/com/ooops/lms/library_management_system/Information-view.fxml";

    private ImageView[] imageViews;

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
        imageViews = new ImageView[] {
                j97Image,
                j97Gif1,
                j97Gif2
        };
        loadGifsInBackground();

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
        setUnVisibleJ97();
    }

    /**
     * thay màu giao diện thành màu đen thùi lùi
     * @param mouseEvent
     */
    public void onBlackColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("dark");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("dark.mp3");
        setUnVisibleJ97();
    }

    /**
     * thay màu giao diện thành màu hồng
     * @param mouseEvent
     */
    public void onPinkColorMouseClicked(MouseEvent mouseEvent) {
        ThemeManager.getInstance().changeTheme("pink");
        ThemeManager.getInstance().applyTheme(interfaceSettingBox);
        Sound.getInstance().playSound("pink.mp3");
        setUnVisibleJ97();
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
        setVisibleJ97();
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

    private void setVisibleJ97 () {
        j97Gif1.setVisible(true);
        j97Gif2.setVisible(true);
    }

    private void setUnVisibleJ97 () {
        j97Gif1.setVisible(false);
        j97Gif2.setVisible(false);
    }

    private void loadGifsInBackground() {
        Task<Void> loadGifsTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                List<String> gifPaths = List.of(
                        "/image/customer/general/jack.jpg",
                        "/image/customer/general/j97.gif",
                        "/image/customer/general/j97.gif"
                );
                for (int i = 0; i < gifPaths.size(); i++) {
                    final int index = i;
                    URL resourceUrl = getClass().getResource(gifPaths.get(i));

                    Image gifImage = new Image(resourceUrl.toExternalForm());

                    Platform.runLater(() -> {
                        if (index < imageViews.length) {
                            imageViews[index].setImage(gifImage);
                            System.out.println("helo");
                        }
                    });

                    Thread.sleep(300);
                }

                return null;
            }
        };

        Thread thread = new Thread(loadGifsTask);
        thread.setDaemon(true);
        thread.start();
    }
}
