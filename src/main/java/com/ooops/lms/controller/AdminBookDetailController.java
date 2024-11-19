package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.SuggestionTable.SuggestionRowClickListener;
import com.ooops.lms.SuggestionTable.SuggestionTable;
import com.ooops.lms.controller.BaseDetailController;
import com.ooops.lms.model.Book;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AdminBookDetailController extends BaseDetailController<Book> {

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
    private Button saveButton;

    @FXML
    private Button scanButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ScrollPane suggestionPane;
    @FXML
    private VBox suggestionVbox;
    private SuggestionTable suggestionTable;

    private PauseTransition pauseTransition = new PauseTransition(Duration.seconds(0.5));
    private boolean isPage1 = true;
    private boolean isSettingItem = false;

    @Override
    protected void loadItemDetails() {
        bookNameText.setText(item.getTitle());
        ISBNText.setText(String.valueOf(item.getISBN()));
        authorNameText.setText(getAuthors(item.getAuthors()));
        categoryText.setText(getCategories(item.getCategories()));
        numberOfBookText.setText(item.getQuantity() + "");
        numberOfBorrowText.setText(String.valueOf(item.getNumberOfLoanedBooks()));
        numberOfLostText.setText(String.valueOf(item.getNumberOfLostBooks()));
        locationText.setText(item.getPlaceAt());
        bookContentText.setText(item.getDescription());

        if (item.getImagePath() != null && isValidImagePath(item.getImagePath())) {
            bookImage.setImage(new Image(item.getImagePath()));
        } else {
            bookImage.setImage(defaultBookImage);
        }
    }

    @Override
    protected void updateAddModeUI() {
        //Xử lý các nút bấm
        ediButton.setVisible(!addMode);
        addButtonPane.setVisible(addMode);
        addButton.setVisible(addMode);
        choiceImageButton.setVisible(addMode);
        scanButton.setVisible(addMode);

        //Cho phép các trường chỉnh sửa
        ISBNText.setEditable(addMode);
        bookNameText.setEditable(addMode);
        locationText.setEditable(addMode);
        authorNameText.setEditable(addMode);
        categoryText.setEditable(addMode);
        numberOfBookText.setEditable(addMode);

        //Nếu như mà là mở addMode thì các trường thông tin set về rỗng
        if (addMode) {
            deleteButton.setVisible(!addMode);
            saveButton.setVisible(!addMode);
            ISBNText.setText(null);
            bookNameText.setText(null);
            locationText.setText(null);
            authorNameText.setText(null);
            categoryText.setText(null);
            numberOfBookText.setText(null);
            numberOfBorrowText.setText(null);
            numberOfLostText.setText(null);
            bookContentText.setText(null);
            bookImage.setImage(defaultBookImage);
        }
    }

    @Override
    protected void updateEditModeUI() {
        //Xử lý ẩn hiện các nút bấm
        ediButton.setVisible(!editMode);
        addButtonPane.setVisible(!editMode);
        deleteButton.setVisible(editMode);
        saveButton.setVisible(editMode);
        choiceImageButton.setVisible(editMode);

        //cho phép các trường có thể sửa đổi
        ISBNText.setEditable(editMode);
        bookNameText.setEditable(editMode);
        locationText.setEditable(editMode);
        authorNameText.setEditable(editMode);
        bookContentText.setEditable(editMode);
        categoryText.setEditable(editMode);
        numberOfBookText.setEditable(editMode);
    }

    @Override
    protected boolean validateInput() {
        // Validate ISBN
        if (ISBNText.getText().trim().isEmpty()) {
            CustomerAlter.showMessage("ISBN không được để trống");
            return false;
        }

        // Validate title
        if (bookNameText.getText().trim().isEmpty()) {
            CustomerAlter.showMessage("Tên sách không được để trống");
            return false;
        }

        // Validate location
        if (locationText.getText().trim().isEmpty()) {
            CustomerAlter.showMessage("Vị trí không được để trống");
            return false;
        }

        // Validate quantity
        try {
            int quantity = Integer.parseInt(numberOfBookText.getText().trim());
            if (quantity < 0) {
                CustomerAlter.showMessage("Số lượng không hợp lệ");
                return false;
            }
        } catch (NumberFormatException e) {
            CustomerAlter.showMessage("Số lượng phải là số");
            return false;
        }

        return true;
    }

    @Override
    protected boolean getNewItemInformation() throws Exception {
        if (item == null) {
            item = new Book();
        }

        item.setISBN(Long.parseLong(ISBNText.getText().trim()));
        item.setTitle(bookNameText.getText().trim());
        item.setPlaceAt(locationText.getText().trim());
        item.setQuantity(Integer.parseInt(numberOfBookText.getText().trim()));
        item.setDescription(bookContentText.getText());
        return true;
    }

    @FXML
    private void initialize() {
        suggestionTable = new SuggestionTable(this.suggestionPane, this.suggestionVbox);
        suggestionTable.setRowClickListener(new SuggestionRowClickListener() {
            @Override
            public void onRowClick(Object o) {
                if(o instanceof Book) {
                    isSettingItem = true;
                    setItem((Book)o);
                    suggestionVbox.getChildren().clear();
                    suggestionPane.setVisible(false);
                }
            }
        });

        childFitWidthParent(copyBookTableVbox, scrollPane);

        // Listener cho ISBN TextField
        ISBNText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!isSettingItem && addMode) {
                suggestionTable.updateSuggestionPanePosition(ISBNText);
                // Reset và restart pause transition mỗi khi có thay đổi text
                pauseTransition.setOnFinished(event -> {
                    String isbnNumbers = newValue.replaceAll("[^0-9]", "");
                    suggestionTable.loadFindData("bookAPI", isbnNumbers);
                });
                pauseTransition.playFromStart();
            }
            isSettingItem = false;
        });

        bookNameText.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!isSettingItem && addMode) {
                suggestionTable.updateSuggestionPanePosition(bookNameText);
                // Reset và restart pause transition mỗi khi có thay đổi text
                pauseTransition.setOnFinished(event -> {
                    suggestionTable.loadFindData("bookAPI", newValue);
                });
                pauseTransition.playFromStart();
            }
        });
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        saveChanges();
    }

    @FXML
    public void onChoiceImageButtonAction(ActionEvent event) {
        item.setImagePath(getImagePath());

        //Nếu như có chọn ảnh thì set ảnh cho bookImage
        if (item.getImagePath() != null) {
            Image image = new Image(item.getImagePath());
            bookImage.setImage(image);
        }
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn muốn xóa quyển sách này?");
        if (confrimYes) {
            //Xóa sách trong CSDL
            Command deleteCommand = new AdminCommand("delete", this.item);
            commandInvoker.setCommand(deleteCommand);
            if (commandInvoker.executeCommand()) {
                mainController.loadData();
                mainController.alterPage();
                loadStartStatus();
                System.out.println("Đã xóa sách");
            } else {
                CustomerAlter.showMessage("Xóa sách thất bại");
            }
        } else {
            System.out.println("Tiếp tục edit");
        }

    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (item != null) {
            setEditMode(true);
        }
    }

    @FXML
    void onPage1ButtonAction(ActionEvent event) {
        isPage1 = true;
        setButtonPageAnimation();

        //Chuyển sang Page1
        copyBookPane.setVisible(!isPage1);
    }

    @FXML
    void onPage2ButtonAction(ActionEvent event) {
        isPage1 = false;
        setButtonPageAnimation();

        //Chuyển sang Page2
        copyBookPane.setVisible(!isPage1);
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
        saveChanges();

    }

    @FXML
    void onScanButtonAction(ActionEvent event) {
        Command scanCommand = new AdminCommand("scan",item);
        commandInvoker.setCommand(scanCommand);
        if(commandInvoker.executeCommand()) {
            item = ((AdminCommand) scanCommand).getBookResult();
            loadItemDetails();
        }
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
