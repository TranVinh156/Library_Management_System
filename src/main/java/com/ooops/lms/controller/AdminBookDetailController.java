package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AdminBookDetailController extends BasicBookController {

    @FXML
    private TextField ISBNText;

    @FXML
    private Button addButton;

    @FXML
    private AnchorPane addButtonPane;

    @FXML
    private TextField authorNameText;

    @FXML
    private TextArea bookContentText;

    @FXML
    private ImageView bookImage;

    @FXML
    private TextField bookNameText;

    @FXML
    private Label categoryLabel;

    @FXML
    private VBox categoryList;

    @FXML
    private AnchorPane categoryTable;

    @FXML
    private AnchorPane copyBookPane;

    @FXML
    private VBox copyBookTableVbox;

    @FXML
    private Button deleteButton;

    @FXML
    private Button ediButton;

    @FXML
    private TextField locationText;

    @FXML
    private TextField numberOfBookText;

    @FXML
    private TextField numberOfBorrowText;

    @FXML
    private TextField numberOfLostText;

    @FXML
    private Button page1Button;

    @FXML
    private Button page2Button;

    @FXML
    private TextField publishingHouseText;

    @FXML
    private Button saveButton;

    @FXML
    private Button scanButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private HBox mainPane;

    private AdminBookPageController mainController;
    private Book book;

    private boolean editMode = false;
    private boolean addMode = false;
    private boolean isPage1 = true;

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {
        setCategoryList(categoryLabel,mainPane,categoryTable,categoryList);
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        if(checkInformation()) {
            Book book = createBookFromInput();
        }
    }

    private Book createBookFromInput() {
        String ISBN = ISBNText.getText();
        String authorName = authorNameText.getText();

        return null;

    }

    private boolean checkInformation() {
        return true;
    }
    @FXML
    void onDeleteButtonAction(ActionEvent event) {

    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (book != null) {
            setEditMode(true);
        }
    }

    @FXML
    void onPage1ButtonAction(ActionEvent event) {
        System.out.println("Page 1");
        isPage1 = true;
        setButtonPageAnimation();
        openPagePane();
    }

    @FXML
    void onPage2ButtonAction(ActionEvent event) {
        System.out.println("Page 2");
        isPage1 = false;
        setButtonPageAnimation();
        openPagePane();
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
        if (addMode) {
            if(getItemInfomation()) {
                boolean confirmYes = CustomerAlter.showAlter("Lưu?");
                if (confirmYes) {
                    //mainController.registerNewItem(book);
                    System.out.println("Đã lưu thay đổi");
                    setAddMode(false);
                }
            }
            else {
                System.out.println("Tiếp tục edit");
            }
        } else {
            boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
            if (confirmYes) {
                //nhớ thêm hàm save thông tin ở đây
                setEditMode(false);
                System.out.println("Đã lưu thay đổi");
            } else {
                System.out.println("Tiếp tục edit");
            }
        }
    }

    private boolean getItemInfomation() {
        book.setTitle(bookNameText.getText());
        //book.setAuthors(authorNameText.getText());
        //book.setISBN(ISBNText.getText());
        book.setDescription(bookContentText.getText());
        book.setPlaceAt(locationText.getText());

        if(checkInformation(book)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkInformation(Book book) {
        return true;
    }

    @FXML
    void onScanButtonAction(ActionEvent event) {

    }

    public void setItem(Book book) {
        this.book = book;

        bookNameText.setText(book.getTitle());
        ISBNText.setText(String.valueOf(book.getISBN()));
        authorNameText.setText(getAuthors(book.getAuthors()));
        categoryLabel.setText(getCategories(book.getCategories()));
        publishingHouseText.setText("chua co");
        numberOfBookText.setText("khong co");
        numberOfBorrowText.setText(String.valueOf(book.getNumberOfLoanedBooks()));
        numberOfLostText.setText(String.valueOf(book.getNumberOfLostBooks()));
        locationText.setText(book.getPlaceAt());
        bookContentText.setText(book.getDescription());
    }

    public Book getItem() {
        return this.book;
    }

    public String getMode() {
        if(addMode) {
            return "addMode";
        } else if(editMode) {
            return "editMode";
        } else {
            return "noneMode";
        }
    }

    private void setEditMode(boolean edit) {
        editMode = edit;
        ediButton.setVisible(!edit);
        deleteButton.setVisible(edit);
        saveButton.setVisible(edit);
        //permit for edit
        ISBNText.setEditable(edit);
        bookNameText.setEditable(edit);
        locationText.setEditable(edit);
        authorNameText.setEditable(edit);
        //categoryText.setEditable(edit);
        publishingHouseText.setEditable(edit);
        numberOfBookText.setEditable(edit);

    }

    private void setAddMode(boolean add) {
        addMode = add;
        ediButton.setVisible(!add);
        deleteButton.setVisible(add);
        saveButton.setVisible(add);

        //permit for edit
        ISBNText.setEditable(add);
        bookNameText.setEditable(add);
        locationText.setEditable(add);
        authorNameText.setEditable(add);
        //categoryText.setEditable(add);
        publishingHouseText.setEditable(add);
        numberOfBookText.setEditable(add);
        if(addMode) {
            ISBNText.setText(null);
            bookNameText.setText(null);
            locationText.setText(null);
            authorNameText.setText(null);
            //categoryText.setText(null);
            publishingHouseText.setText(null);
            numberOfBookText.setText(null);
        }

    }

    public void loadStartStatus() {
        book = new Book();
        setAddMode(false);
        setEditMode(false);
    }

    private void openPagePane() {
        copyBookPane.setVisible(!isPage1);
    }

    private void setButtonPageAnimation() {
        if(isPage1) {
            //choice button color darker
            page1Button.setStyle("-fx-background-color: #DDDCDC;" );
            page2Button.setStyle("-fx-background-color: #FFF;");

            //choice button bring to front
            if (page1Button.getParent().getChildrenUnmodifiable().indexOf(page1Button) <
                    page1Button.getParent().getChildrenUnmodifiable().size() - 1) {
                page2Button.toBack();
            }
        } else {
            //choice button color darker
            page2Button.setStyle("-fx-background-color: #DDDCDC;");
            page1Button.setStyle("-fx-background-color: #FFF;");

            //choice button bring to front
            if (page2Button.getParent().getChildrenUnmodifiable().indexOf(page2Button) <
                    page2Button.getParent().getChildrenUnmodifiable().indexOf(page1Button)) {
                page1Button.toBack();
            }
        }
    }




}
