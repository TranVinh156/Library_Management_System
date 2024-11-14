package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public class AdminBookTableController extends BasicBookController {

    @FXML
    private TextField ISBNFindText;

    @FXML
    private Button addButton;

    @FXML
    private TextField authorFindText;

    @FXML
    private TextField bookNameFindTExt;

    @FXML
    private VBox bookTableVbox;

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
    private TextField stausText;

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

    private AdminBookPageController mainController;
    Map<String, Object> findCriteria = new HashMap<>();

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
        loadData();
    }

    @FXML
    public void initialize() {
        setCategoryList(categoryFindButton, mainPane, categoryTable, categoryList);
        setVboxFitWithScrollPane();

    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        updateCategoryTablePosition(mainPane, categoryFindButton, categoryTable);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {
        findCriteria.clear(); // Xóa các tiêu chí tìm kiếm cũ

        // Kiểm tra và thêm tiêu chí tìm kiếm theo ISBN
        if (!ISBNFindText.getText().trim().isEmpty()) {
            findCriteria.put("ISBN", ISBNFindText.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo tên sách
        if (!bookNameFindTExt.getText().trim().isEmpty()) {
            findCriteria.put("title", bookNameFindTExt.getText());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo tác giả
        if (!authorFindText.getText().trim().isEmpty()) {
            findCriteria.put("author", authorFindText.getText().trim());
        }

        // Kiểm tra và thêm tiêu chí tìm kiếm theo trạng thái
        if (!stausText.getText().trim().isEmpty()) {
            findCriteria.put("status", stausText.getText().trim().toUpperCase());
        }

        if (findCriteria != null && !findCriteria.isEmpty()) {
            System.out.println("Tải lại dữ liệu với các tiêu chí tìm kiếm mới");
            bookList.clear();
            bookTableVbox.getChildren().clear();
            findCriteria.clear();
            findCriteria.put("title", "Ha");
            try {
                bookList.addAll((BookDAO.getInstance().searchByCriteria(findCriteria)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            addRowToTable();
        } else {
            bookList.clear();
            bookTableVbox.getChildren().clear();
            try {
                bookList.addAll((BookDAO.getInstance().selectAll()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            addRowToTable();
        }
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    /**
     * Dùng để lấy dữ liệu books từ CSDL và đẩy vào bookList của Controller.
     */
    public void loadData() {
        System.out.println("Load lại các hàng trong bảng sách!");
        // Xóa tất cả dữ liệu trong các List và các Row trong bảng
        bookList.clear();
        bookItemList.clear();
        borrowBookList.clear();
        lostBookList.clear();
        reverserBookList.clear();
        bookTableVbox.getChildren().clear();

        try {
            //Lấy tất cả các book cho vào bookList
            bookList.addAll(BookDAO.getInstance().selectAll());

            //Xử lý để set totalNumberBookLabel (tổng số sách hiện có)
            Map<String, Object> criteriaTotal = new HashMap<>();
            criteriaTotal.put("status", "available");
            bookItemList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaTotal));
            Map<String, Object> criteriaTotal2 = new HashMap<>();
            criteriaTotal2.put("status", "LOANED");
            bookItemList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaTotal2));
            Map<String, Object> criteriaTotal3 = new HashMap<>();
            criteriaTotal3.put("status", "RESERVED");
            bookItemList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaTotal3));
            totalNumberBookLabel.setText(String.valueOf(bookItemList.size()));

            //Xử lý để set totalNumberBorrowLabel ( tổng số sách mượn)
            Map<String, Object> criteriaBorrow = new HashMap<>();
            criteriaBorrow.put("status", "LOANED");
            borrowBookList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaBorrow));
            totalNumberBorrowLabel.setText(String.valueOf(borrowBookList.size()));

            //Xử lý để set totalNumberLostLabel ( tổng số sách làm mất)
            Map<String, Object> criteriaLost = new HashMap<>();
            criteriaBorrow.put("status", "LOST");
            lostBookList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaBorrow));
            totalNumberLostLabel.setText(String.valueOf(lostBookList.size()));

            //Xử lý để set totalNumberIssueLabel (tổng số sách đặt trước)
            Map<String, Object> criteriaReserved = new HashMap<>();
            criteriaReserved.put("status", "RESERVED");
            reverserBookList.addAll(BookItemDAO.getInstance().searchByCriteria(criteriaReserved));
            totalNumberIssueLabel.setText(String.valueOf(reverserBookList.size()));

        } catch (SQLException e) {
            System.out.println("Lỗi khi addBook:" + e.getMessage());
        }

        addRowToTable();

    }

    /**
     * Thêm các row vào bookTable
     */
    private void addRowToTable() {
        for (Book book : bookList) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_TABLE_ROW_FMXL));
                HBox row = loader.load();

                AdminBookTableRowController rowController = loader.getController();
                rowController.setMainController(mainController);
                rowController.setBook(book);
                childFitWidthParent(row, rowController);
                bookTableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Điều chỉnh Vbox resize theo chiều dài, chiều rộng của scrollPane.
     */
    private void setVboxFitWithScrollPane() {
        childFitWidthParent(bookTableVbox, scrollPane);
        childFitHeightParent(bookTableVbox, scrollPane);
    }

}
