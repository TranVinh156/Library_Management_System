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

    private BookDAO bookDAO = new BookDAO();
    Map<String, Object> criteria = new HashMap<>();
    private ObservableList<Book> bookList = FXCollections.observableArrayList();;
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
        setCategoryList(categoryFindButton, mainPane, categoryTable, categoryList);
        setVboxFitWithScrollPane();

    }

    private void loadData() {
        bookList.clear();
        bookTableVbox.getChildren().clear();

        bookList.add(new Book(
                1234567890123L,
                "Effective Java",
                "/images/effective_java.jpg",
                "A comprehensive guide to programming in Java.",
                "Shelf A1",
                Arrays.asList(new Author("Joshua Bloch")),
                Arrays.asList(new Category("Programming")),
                10
        ));

        bookList.add(new Book(
                2345678901234L,
                "Clean Code",
                "/images/clean_code.jpg",
                "A handbook of agile software craftsmanship.",
                "Shelf B2",
                Arrays.asList(new Author("Robert C. Martin")),
                Arrays.asList(new Category("Programming")),
                8
        ));

        bookList.add(new Book(
                3456789012345L,
                "Design Patterns",
                "/images/design_patterns.jpg",
                "Elements of reusable object-oriented software.",
                "Shelf C3",
                Arrays.asList(new Author("Erich Gamma"),new Author( "Richard Helm"), new Author("Ralph Johnson")),
                Arrays.asList(new Category("Software Engineering")),
                12
        ));
            try {
                bookList.addAll(bookDAO.selectAll());
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
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
        childFitWidthParent(bookTableVbox, scrollPane);
        childFitHeightParent(bookTableVbox, scrollPane);
    }

    @FXML
    void onCategoryFindButton(ActionEvent event) {
        categoryTable.setVisible(!categoryTable.isVisible());
        updateCategoryTablePosition(mainPane, categoryFindButton, categoryTable);
    }

    @FXML
    void onFindButtonAction(ActionEvent event) {

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

}
