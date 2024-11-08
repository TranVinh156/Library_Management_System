package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.BookDAO;
import com.ooops.lms.model.Author;
import com.ooops.lms.model.Book;
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

    private Book book;
    private BookDAO bookDAO;
    Map<String, Object> criteria = new HashMap<>();
    private ObservableList<Book> bookList;
    private AdminBookPageController mainController;

    public void registerNewItem(Book book) {
        addBook(book);
    }
    private void addBook(Book book) {
        bookList.add(book);
    }

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
        loadData();
    }

    @FXML
    public void initialize() {
        bookList = FXCollections.observableArrayList();

        // Tạo danh sách tác giả cho các quyển sách
        List<Author> authors1 = Arrays.asList(
                new Author(1, "John Doe"),
                new Author(2, "Jane Smith")
        );
        List<Author> authors2 = Arrays.asList(
                new Author(3, "Alice Johnson"),
                new Author(4, "Bob Brown")
        );
        List<Author> authors3 = Arrays.asList(
                new Author(5, "Chris White")
        );

        // Tạo danh sách thể loại cho các quyển sách
        List<Category> categories1 = Arrays.asList(
                new Category(1, "Programming"),
                new Category(2, "Technology")
        );
        List<Category> categories2 = Arrays.asList(
                new Category(3, "Science Fiction"),
                new Category(4, "Adventure")
        );
        List<Category> categories3 = Arrays.asList(
                new Category(5, "History"),
                new Category(6, "Biography")
        );

        setCategoryList(categoryFindButton,mainPane,categoryTable,categoryList);
        setVboxFitWithScrollPane();

        bookDAO = new BookDAO();
    }

    private void loadData() {
        bookList.clear();
        try {

            bookList.addAll(bookDAO.searchByCriteria(criteria));

            bookTableVbox.getChildren().clear();
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
        } catch (SQLException e) {
            CustomerAlter.showAlter(e.getMessage());
        }
    }

    private void getCriteria() {

        if (!ISBNFindText.getText().isEmpty()) {
            criteria.put("ISBN", ISBNFindText.getText());
        }

        if (!authorFindText.getText().isEmpty()) {
            criteria.put("author", authorFindText.getText());
        }

        if (!bookNameFindTExt.getText().isEmpty()) {
            criteria.put("book_name", bookNameFindTExt.getText());
        }

    }

    private void setVboxFitWithScrollPane() {
        childFitWidthParent(bookTableVbox,scrollPane);
        childFitHeightParent(bookTableVbox,scrollPane);
    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        updateCategoryTablePosition(mainPane,categoryFindButton,categoryTable);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.alterPage();
    }

}
