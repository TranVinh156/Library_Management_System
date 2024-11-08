package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AdminMenuController extends BasicController {

    @FXML
    private VBox menuBar;

    @FXML
    private Button bookManagementButton;

    @FXML
    private ImageView bookManagementLogo;

    @FXML
    private Button borrowButton;

    @FXML
    private ImageView borrowLogo;

    @FXML
    private Button dashboardButton;

    @FXML
    private ImageView dashboardLogo;

    @FXML
    private ImageView issueLogo;

    @FXML
    private Button issuesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private ImageView logoutLogo;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private VBox menuBarPlus;

    @FXML
    private AnchorPane menuPiece1Pane;

    @FXML
    private AnchorPane menuPiece2Pane;

    @FXML
    private AnchorPane menuPiece3Pane;

    @FXML
    private AnchorPane plusMenuPeice3Pane;

    @FXML
    private AnchorPane plusMenuPiece1Pane;

    @FXML
    private AnchorPane plusMenuPiece2Pane;

    @FXML
    private Button readerManagementButton;

    @FXML
    private ImageView readerManagementlogo;

    @FXML
    private Button settingButton;

    @FXML
    private ImageView settingLogo;

    private boolean isMouseOverMenuBar = false;

    public void initialize() throws IOException {

        setupHoverMenuBar();

        MenuItemHover(dashboardLogo, dashboardButton);
        MenuItemHover(borrowLogo, borrowButton);
        MenuItemHover(bookManagementLogo, bookManagementButton);
        MenuItemHover(readerManagementlogo, readerManagementButton);
        MenuItemHover(logoutLogo, logoutButton);
        MenuItemHover(issueLogo, issuesButton);
        MenuItemHover(settingLogo, settingButton);
        MenuItemHover(logoutLogo, logoutButton);

        openPage(dashboardPane);

    }

    @FXML
    void onDashboardButtonAction(ActionEvent event) {
       openPage(dashboardPane);
    }

    @FXML
    void onBorrowButtonAction(ActionEvent event) {
        openPage(borrowPagePane);
    }

    @FXML
    void onBookManagmentButtonAction(ActionEvent event) {
        openPage(bookPagePane);
    }

    @FXML
    void onReaderManagementButtonAction(ActionEvent event) {
        openPage(userPagePane);
    }

    @FXML
    void onIssuesButtonAction(ActionEvent event) {
        openPage(issuePagePane);
    }

    @FXML
    void onLogoutButtonAction(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn thoát?");
        if (confirmYes) {
            try {
                Stage stage = (Stage) mainPane.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/com/ooops/lms/library_management_system/UserLogin.fxml"));
                stage.setResizable(false);
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    void onSettingButtonAction(ActionEvent event) {

    }


    private void MenuItemHover(ImageView logo, Button button) {
        logo.setOnMouseEntered(event -> button.setOpacity(0.7)); // Thay đổi độ trong của nút khi hover
        logo.setOnMouseExited(event -> button.setOpacity(1.0)); // Trả về độ trong bình thường khi rời
        button.setOnMouseEntered(event -> button.setOpacity(0.7)); // Giữ hiệu ứng nếu chuột ở nút
        button.setOnMouseExited(event -> button.setOpacity(1.0)); // Trả về tính chất ban đầu

        logo.setOnMouseClicked(event -> {
            button.fire();
        });
    }


    private void setupHoverMenuBar() {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(1.2));
        menuBar.setOnMouseEntered(event -> {
            pauseTransition.play();
            isMouseOverMenuBar = true;
            pauseTransition.setOnFinished(event1 -> {
                if (menuBar.getOnMouseEntered() != null) {
                    menuBarPlus.setVisible(true);
                }
            });
        });
        menuBar.setOnMouseExited(event -> {
            pauseTransition.playFromStart();
            pauseTransition.pause();
        });
        menuBar.setOnMouseClicked(event -> {
            pauseTransition.playFromStart();
        });
        menuBarPlus.setOnMouseExited(event2 -> {
            menuBarPlus.setVisible(false);
            pauseTransition.playFromStart();
        });
    }

    private void openPage(Node e) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(e);
    }


}
