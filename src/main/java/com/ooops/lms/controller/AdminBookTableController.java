package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseTableController;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.BookIssueDAO;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.database.dao.BookReservationDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.Category;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.model.enums.BookReservationStatus;
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
import org.checkerframework.checker.units.qual.C;

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
    private ChoiceBox<String> categoryChoiceBox;

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
    private ChoiceBox<String> statusFindBox;

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

    private int totalNumberBook;
    private int totalNumberLost;

    @FXML
    protected void initialize() {
        setCategoryFindList();
        statusFindBox.getItems().add("None");
        statusFindBox.getItems().addAll(BookStatus.AVAILABLE.toString(), BookStatus.UNAVAILAVBLE.toString().toString());
        bookNameFindTExt.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) bookNameFindTExt.getScene().getWindow();

                stage.widthProperty().addListener((obs, oldWidth, newWidth) ->
                        Platform.runLater(() -> categoryTable.setVisible(false)));

                stage.heightProperty().addListener((obs, oldHeight, newHeight) ->
                        Platform.runLater(() -> categoryTable.setVisible(false)));
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
    protected void getCriteria() {
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

        if (!categoryChoiceBox.getItems().isEmpty() && categoryChoiceBox.getValue() != "None" && categoryChoiceBox.getValue() != null) {
            findCriteria.put("category_name", categoryChoiceBox.getValue());
        }

        if (!statusFindBox.getItems().isEmpty() && statusFindBox.getValue() != "None" && statusFindBox.getValue() != null) {
            findCriteria.put("BookStatus", statusFindBox.getValue());
        }

    }

    @Override
    protected void searchCriteria() {
        getCriteria();
        if (findCriteria.isEmpty()) {
            loadData();
            return;
        }
        try {
            itemsList.clear();
            itemsList.addAll(BookDAO.getInstance().searchByCriteria(findCriteria));
        } catch (Exception e) {
            e.printStackTrace();
        }
        loadRows();
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        searchCriteria();
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    protected void setText() {
        try {
            totalNumberBook = 0;
            totalNumberLost = 0;
            findCriteria.put("BookItemStatus", BookItemStatus.AVAILABLE.toString());
            List<Book> bookList = BookDAO.getInstance().selectAll();
            for (Book book : bookList) {
                totalNumberBook += book.getQuantity();
                totalNumberLost += book.getNumberOfLostBooks();
            }
            totalNumberBookLabel.setText(String.valueOf(this.totalNumberBook-this.totalNumberLost));
            totalNumberLostLabel.setText(String.valueOf(this.totalNumberLost));
        } catch (Exception e) {
            e.printStackTrace();
        }
        adminDashboardController.setTotalBookLabel(totalNumberBookLabel.getText());
        try {
            findCriteria.put("BookItemStatus", BookItemStatus.LOANED.toString());
            totalNumberBorrowLabel.setText(BookItemDAO.getInstance().searchByCriteria(findCriteria).size() + "");
            findCriteria.clear();
            findCriteria.put("BookReservationStatus", BookReservationStatus.WAITING.toString());
            totalNumberIssueLabel.setText(BookReservationDAO.getInstance().searchByCriteria(findCriteria).size() + "");
            findCriteria.clear();
            findCriteria.put("BookItemStatus", BookItemStatus.LOST.toString());
            totalNumberLostLabel.setText(BookItemDAO.getInstance().searchByCriteria(findCriteria).size() + "");
            findCriteria.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setCategoryFindList() {
        try {
            categoryChoiceBox.getItems().add("None");
            List<Category> categories = BookDAO.getInstance().selectAllCategory();
            for (Category category : categories) {
                categoryChoiceBox.getItems().add(category.toString());
            }
        } catch (Exception e) {
            System.out.println("Lỗi setCategoryFindList:" + e.getMessage());
        }

    }


}
