package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.database.dao.BookItemDAO;
import com.ooops.lms.model.Book;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.Category;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private TextField categoryText;

    @FXML
    private Button choiceImageButton;

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
    private HBox mainPane;

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

    private AdminBookPageController mainController;
    private Book book;

    private boolean editMode = false;
    private boolean addMode = false;
    private boolean isPage1 = true;
    private ObservableList<BookItem> bookItemList = FXCollections.observableArrayList();
    private BookItemDAO bookItemDAO = new BookItemDAO();

    public void setMainController(AdminBookPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    private void initialize() {

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        System.out.println("onAddButtonAction");
        if (getNewBookInformation()) {
            boolean confirmYes = CustomerAlter.showAlter("Thêm sách mới?");
            if (confirmYes) {
                Command addCommand = new AdminCommand("add", this.book);
                commandInvoker.setCommand(addCommand);
                if (commandInvoker.executeCommand()) {
                    mainController.loadData();
                    setAddMode(false);
                    System.out.println("Đã lưu thay đổi");
                }
            }
        }
    }

    @FXML
    public void onChoiceImageButtonAction(ActionEvent event) {
        book.setImagePath(getImagePath());

        if(book.getImagePath() != null) {
            Image image = new Image(book.getImagePath());
            bookImage.setImage(image);
        }
    }

    private boolean getNewBookInformation() {
        book.setISBN(Long.parseLong(ISBNText.getText()));
        book.setTitle(bookNameText.getText());
        book.setPlaceAt(locationText.getText());
        book.setImagePath(Book.DEFAULT_IMAGE_PATH);
        //book.setPublishingHouse
        book.setDescription(bookContentText.getText());
        if(book.getImagePath() == null) {
            book.setImagePath(Book.DEFAULT_IMAGE_PATH);
        }
        if (checkInformation(book)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkInformation(Book book) {
        System.out.println("DANG KIEM TRA");
        String isbnText = ISBNText.getText().trim();
        try {
            // Parse ISBN only if it's a valid long number
            if (!isbnText.isEmpty()) {
                Long isbn = Long.parseLong(isbnText);
                book.setISBN(isbn);
            } else {
                CustomerAlter.showMessage("ISBN cannot be empty.");
                return false;
            }
        } catch (NumberFormatException e) {
            CustomerAlter.showMessage("Please enter a valid numeric ISBN.");
            return false;
        }
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            CustomerAlter.showMessage("Không được để tên sách trống");
            return false;
        }
        if (book.getPlaceAt() == null || book.getPlaceAt().trim().isEmpty()) {
            CustomerAlter.showMessage("Không được để vị trí sách trống");
            return false;
        }

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
        boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
        if (confirmYes) {
            //nhớ thêm hàm save thông tin ở đây
            setEditMode(false);
            System.out.println("Đã lưu thay đổi");
        } else {
            System.out.println("Tiếp tục edit");
        }
    }

    private boolean getItemInfomation() {
        book.setTitle(bookNameText.getText());
        //book.setAuthors(authorNameText.getText());
        //book.setISBN(ISBNText.getText());
        book.setDescription(bookContentText.getText());
        book.setPlaceAt(locationText.getText());

        if (checkInformation(book)) {
            return true;
        } else {
            return false;
        }
    }


    @FXML
    void onScanButtonAction(ActionEvent event) {

    }

    private void loadData() {
        bookItemList.clear();
        copyBookTableVbox.getChildren().clear();

        try{
            Map<String, Object> searchCriteria = new HashMap<>();
            searchCriteria.put("ISBN", book.getISBN());
            bookItemList.addAll(bookItemDAO.searchByCriteria(searchCriteria));
        }catch (Exception e) {
            System.out.println("Lỗi bookItemList addAll:" + e.getMessage());
        }

        for(BookItem bookItem: bookItemList) {
            try {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(BOOK_COPY_ROW_FXML));
                    HBox row = loader.load();

                    AdminBookTableRowController rowController = loader.getController();
                    rowController.setMainController(mainController);
                    rowController.setBook(bookItem);
                    childFitWidthParent(row, rowController);
                    copyBookTableVbox.getChildren().add(row);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setItem(Book book) {
        this.book = book;

        bookNameText.setText(book.getTitle());
        ISBNText.setText(String.valueOf(book.getISBN()));
        authorNameText.setText(getAuthors(book.getAuthors()));
        categoryText.setText(getCategories(book.getCategories()));
        publishingHouseText.setText("chua co");
        numberOfBookText.setText("0");
        numberOfBorrowText.setText(String.valueOf(book.getNumberOfLoanedBooks()));
        numberOfLostText.setText(String.valueOf(book.getNumberOfLostBooks()));
        locationText.setText(book.getPlaceAt());
        bookContentText.setText(book.getDescription());

        loadData();
    }

    public Book getItem() {
        return this.book;
    }

    public String getMode() {
        if (addMode) {
            return "addMode";
        } else if (editMode) {
            return "editMode";
        } else {
            return "noneMode";
        }
    }

    public void setEditMode(boolean edit) {
        editMode = edit;
        ediButton.setVisible(!edit);
        deleteButton.setVisible(edit);
        saveButton.setVisible(edit);
        //permit for edit
        ISBNText.setEditable(edit);
        bookNameText.setEditable(edit);
        locationText.setEditable(edit);
        authorNameText.setEditable(edit);
        bookContentText.setEditable(edit);
        //categoryText.setEditable(edit);
        publishingHouseText.setEditable(edit);
        numberOfBookText.setEditable(edit);

    }

    public void setAddMode(boolean add) {
        addMode = add;
        ediButton.setVisible(!add);
        addButtonPane.setVisible(add);
        addButton.setVisible(add);
        choiceImageButton.setVisible(add);
        scanButton.setVisible(add);

        //permit for edit
        ISBNText.setEditable(add);
        bookNameText.setEditable(add);
        locationText.setEditable(add);
        authorNameText.setEditable(add);
        //categoryText.setEditable(add);
        publishingHouseText.setEditable(add);
        numberOfBookText.setEditable(add);
        if (addMode) {
            deleteButton.setVisible(!add);
            saveButton.setVisible(!add);
            ISBNText.setText(null);
            bookNameText.setText(null);
            locationText.setText(null);
            authorNameText.setText(null);
            categoryText.setText(null);
            publishingHouseText.setText(null);
            numberOfBookText.setText(null);
            numberOfBorrowText.setText(null);
            numberOfLostText.setText(null);
            bookImage.setImage(defaultBookImage);
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
        if (isPage1) {
            //choice button color darker
            page1Button.setStyle("-fx-background-color: #DDDCDC;");
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
