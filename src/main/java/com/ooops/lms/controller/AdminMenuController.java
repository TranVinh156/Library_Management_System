package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Stack;

public class AdminMenuController extends BasicController {
    @FXML
    private Button addButton;

    @FXML
    private Button addNewBookButton;

    @FXML
    private Button addNewBorrowButtonAction;

    @FXML
    private Button addNewMemberButton;

    @FXML
    private Button addNewReservationButton;

    @FXML
    private AnchorPane addTablePane;

    @FXML
    private Button bookManagementButton;

    @FXML
    private ImageView bookManagementLogo;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button borrowButton;

    @FXML
    private ImageView borrowLogo;

    @FXML
    private Button dashboardButton;

    @FXML
    private ImageView dashboardLogo;

    @FXML
    private HBox hBoxMain;

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
    private VBox menuBar;

    @FXML
    private ImageView openMenuIcon;

    @FXML
    private Button readerManagementButton;

    @FXML
    private ImageView readerManagementlogo;

    @FXML
    private Button reservationButton;

    @FXML
    private Button settingButton;

    @FXML
    private ImageView settingLogo;

    private boolean isMenuExpanded = false;
    private static Image minimizeIconImage = new Image(BasicController.class.getResource("/image/icon/minimize.png").toExternalForm());;
    private static Image maximizeIconImage = new Image(BasicController.class.getResource("/image/icon/maximize.png").toExternalForm());

    private AdminDashboardController adminDashboardController;
    private AdminBookPageController adminBookPageController;
    private AdminUserPageController adminUserPageController;
    private AdminBorrowPageController adminBorrowPageController;
    private AdminReservationPageController adminReservationPageController;
    private AdminIssuePageController adminIssuePageController;

    public void initialize() throws IOException {
       // titlePageStack.push("Dashboard");
        setTitleLabel();
        adminBookPageController = bookPagePaneLoader.getController();
        adminUserPageController = userPagePaneLoader.getController();
        adminBorrowPageController = borrowPagePaneLoader.getController();
        adminReservationPageController = reservationPagePaneLoader.getController();
        adminIssuePageController = issuePagePaneLoader.getController();
        adminDashboardController = dashboardLoader.getController();
        adminDashboardController.setAdminMenuController(this);

        openPage(dashboardPane);
        hideButtonTexts();
        openMenuIcon.setOnMouseClicked(event -> toggleMenu());
        hanleAddTablePaneClose();
    }

    private void hanleAddTablePaneClose() {
        // Lắng nghe sự thay đổi của Scene
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            newScene.setOnMouseClicked(event -> {
                if(addTablePane.isVisible()) {
                    javafx.geometry.Bounds bounds = addTablePane.localToScene(addTablePane.getBoundsInLocal());

                    if (!bounds.contains(event.getSceneX(), event.getSceneY())) {
                        // Ẩn addTablePane nếu click ngoài
                        addTablePane.setVisible(false);
                    }
                }
            });
        });
    }

    @FXML
    void onAddNewBookButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý sách");
        openPage(bookPagePane);
        adminBookPageController.loadAddPane();
        addTablePane.setVisible(false);
    }

    @FXML
    void onAddNewBorrowButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý mượn sách");
        openPage(borrowPagePane);
        adminBorrowPageController.loadAddPane();
        addTablePane.setVisible(false);
    }

    @FXML
    void onAddNewMemberButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý độc giả");
        openPage(userPagePane);
        adminUserPageController.loadAddPane();
        addTablePane.setVisible(false);
    }
    @FXML
    void onAddNewReservationButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý đặt trước");
        openPage(reservationPagePane);
        adminReservationPageController.loadAddPane();
        addTablePane.setVisible(false);
    }

    @FXML
     void onDashboardButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Dashboard");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminDashboardController.loadRecentIssuel();
        openPage(dashboardPane);
    }

    @FXML
    void onBorrowButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý mượn sách");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminBorrowPageController.startPage();
        openPage(borrowPagePane);
    }
    @FXML
    void onReservationButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý đặt trước sách");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminReservationPageController.startPage();
        openPage(reservationPagePane);
    }

    @FXML
    void onBookManagmentButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý sách");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminBookPageController.startPage();
        openPage(bookPagePane);
    }

    @FXML
    void onReaderManagementButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Quản lý độc giả");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminUserPageController.loadData();
        openPage(userPagePane);
    }

    @FXML
    void onIssuesButtonAction(ActionEvent event) {
        while (!getTitlePageStack().isEmpty()) getTitlePageStack().pop();
        getTitlePageStack().push("Report");
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }
        adminIssuePageController.startPage();
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
        if(addTablePane.isVisible()) {
            addTablePane.setVisible(false);
        }

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        addTablePane.setVisible(!addTablePane.isVisible());
    }



    private void toggleMenu() {
        if (!isMenuExpanded) {
            menuBar.setMinWidth(238);
            menuBar.setMaxWidth(238);
            addButton.setMaxWidth(100);
            addButton.setMinWidth(100);
            openMenuIcon.setImage(minimizeIconImage);
            showButtonTexts();
        } else {
            menuBar.setMinWidth(62);
            menuBar.setMaxWidth(62);
            addButton.setMinWidth(54);
            addButton.setMaxWidth(54);

            openMenuIcon.setImage(maximizeIconImage);
            hideButtonTexts();
        }
        isMenuExpanded = !isMenuExpanded;
    }

    private void showButtonTexts() {
        addButton.setText("New");
        dashboardButton.setText("Dashboard");
        bookManagementButton.setText("Quản lý sách");
        reservationButton.setText("Đặt sách");
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
        reservationButton.setText("");
        issuesButton.setText("");
        settingButton.setText("");
        logoutButton.setText("");
    }

    private void openPage(Node e) {
        mainPane.getChildren().clear();
        mainPane.getChildren().add(e);
    }

    public void setTitleLabel() {
      //  titleLabel.setText(getAllTitles());
    }


}
