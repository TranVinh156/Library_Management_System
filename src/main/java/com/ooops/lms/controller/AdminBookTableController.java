package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.enums.BookStatus;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;

public class AdminBookTableController extends BaseTableController<Book, AdminBookPageController, AdminBookTableRowController> {

    @FXML
    private TextField ISBNFindText;

    @FXML
    private Button addButton;

    @FXML
    private TextField authorFindText;

    @FXML
    private TextField bookNameFindTExt;

    @FXML
    private VBox tableVbox;

    @FXML
    private Button categoryFindButton;

    @FXML
    private VBox categoryList;

    @FXML
    private AnchorPane categoryTable;

    @FXML
    private Button findButton;

    @FXML
    private HBox mainPane;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ChoiceBox<BookStatus> statusFindBox;

    @FXML
    private AnchorPane tableBookPane;

    @FXML
    private Label totalNumberBookLabel;

    @FXML
    private Label totalNumberBorrowLabel;

    @FXML
    private Label totalNumberIssueLabel;

    @FXML
    private Label totalNumberLostLabel;


    private static final String ROW_FXML = "/com/ooops/lms/library_management_system/AdminBookTableRow.fxml";
    private AdminDashboardController adminDashboardController;

    @FXML
    protected void initialize() {
        statusFindBox.getItems().addAll(BookStatus.values());
        bookNameFindTExt.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) bookNameFindTExt.getScene().getWindow();

                stage.widthProperty().addListener((obs, oldWidth, newWidth) ->
                        Platform.runLater(() -> updateCategoryFindPanePosition()));

                stage.heightProperty().addListener((obs, oldHeight, newHeight) ->
                        Platform.runLater(() -> updateCategoryFindPanePosition()));
            }
        });
        adminDashboardController = dashboardLoader.getController();
    }

    @Override
    protected String getRowFXML() {
        return ROW_FXML;
    }

    @Override
    protected void loadDataFromSource() throws SQLException {
        itemsList.addAll(BookDAO.getInstance().selectAll());
        setText();
    }

    @Override
    protected void getCriteria(){
        findCriteria.clear();

        // Kiểm tra và thêm tiêu chí tìm kiếm theo ISBN
        if (!ISBNFindText.getText().isEmpty()) {
            findCriteria.put("ISBN", ISBNFindText.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo tên sách
        if (!bookNameFindTExt.getText().isEmpty()) {
            findCriteria.put("title", bookNameFindTExt.getText());
        }
        // Kiểm tra và thêm tiêu chí tìm kiếm theo tác giả
        if (!authorFindText.getText().isEmpty()) {
            findCriteria.put("author_name", authorFindText.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo trạng thái

    }
    @Override
    protected void searchCriteria() {
        getCriteria();
        if(findCriteria.isEmpty()) {
            loadData();
            return;
        }
        try {
            itemsList.clear();
            itemsList.addAll(BookDAO.getInstance().searchByCriteria(findCriteria));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(itemsList.size());
        loadRows();
    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        //updateCategoryTablePosition(mainPane, categoryFindButton, categoryTable);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    private void updateCategoryFindPanePosition() {
        // Lấy tọa độ của textField trong Scene
        Bounds boundsInScene = categoryFindButton.localToScene(categoryTable.getBoundsInLocal());

        // Chuyển đổi tọa độ này sang hệ tọa độ của parent chứa suggestionPane
        Bounds boundsInParent = categoryTable.getParent().sceneToLocal(boundsInScene);

        // Cập nhật vị trí của suggestionPane
        categoryTable.setLayoutX(boundsInParent.getMinX());
        categoryTable.setLayoutY(boundsInParent.getMinY()+30);
    }
    protected void setText() {
        totalNumberBookLabel.setText(String.valueOf(itemsList.size()));
        adminDashboardController.setTotalBookLabel(totalNumberBookLabel.getText());
        try {
            findCriteria.put("status", "BORROWED");
            totalNumberBorrowLabel.setText(BookIssueDAO.getInstance().searchByCriteria(findCriteria).size()+"");
            findCriteria.clear();
            findCriteria.put("status","PENDING");
            totalNumberIssueLabel.setText(BookReservationDAO.getInstance().searchByCriteria(findCriteria).size()+"");
            findCriteria.clear();
            findCriteria.put("status","LOST");
            totalNumberLostLabel.setText(BookItemDAO.getInstance().searchByCriteria(findCriteria).size()+"");
            findCriteria.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
