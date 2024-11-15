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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

public class AdminMenuController extends BasicController {


    @FXML
    private Button addButton;

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
    private ScrollPane scrollPane;

    @FXML
    private VBox menuBar;

    @FXML
    private ImageView openMenuIcon;

    @FXML
    private Button readerManagementButton;

    @FXML
    private ImageView readerManagementlogo;

    @FXML
    private Button settingButton;

    @FXML
    private HBox hBoxMain;

    @FXML
    private VBox vBoxMain;

    @FXML
    private ImageView settingLogo;

    private boolean isMenuExpanded = false;

    public void initialize() throws IOException {
        //vBoxMain.prefWidthProperty().bind(scrollPane.widthProperty());
        //vBoxMain.prefHeightProperty().bind(scrollPane.heightProperty());
        childFitHeightParent(mainPane,vBoxMain);
        childFitWidthParent(mainPane,vBoxMain);
        openPage(dashboardPane);

        hideButtonTexts();

        openMenuIcon.setOnMouseClicked(event ->toggleMenu());

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

    @FXML
    void onAddButtonAction(ActionEvent event) {

    }

    private void toggleMenu() {
        if(!isMenuExpanded) {
            menuBar.setMinWidth(210);
            menuBar.setMaxWidth(210);
            addButton.setMaxWidth(100);
            addButton.setMinWidth(100);
            showButtonTexts();
        } else {
            menuBar.setMinWidth(62);
            menuBar.setMaxWidth(62);
            addButton.setMinWidth(54);
            addButton.setMaxWidth(54);
            hideButtonTexts();
        }
        isMenuExpanded = !isMenuExpanded;
    }

    private void showButtonTexts() {
        addButton.setText("New");
        dashboardButton.setText("Dashboard");
        bookManagementButton.setText("Quản lý sách");
        borrowButton.setText("Mượn trả");
        readerManagementButton.setText("Quản lý độc giả");
        issuesButton.setText("Sự cố");
        settingButton.setText("Cài đặt");
        logoutButton.setText("Đăng xuất");
    }
    private void hideButtonTexts() {
        addButton.setText("");
        dashboardButton.setText("");
        bookManagementButton.setText("");
        borrowButton.setText("");
        readerManagementButton.setText("");
        issuesButton.setText("");
        settingButton.setText("");
        logoutButton.setText("");
    }
    private void openPage(Node e) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(e);
    }


}
