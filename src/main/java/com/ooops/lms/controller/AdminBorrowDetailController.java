package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.SuggestionTable.SuggestionRowClickListener;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.*;
import com.ooops.lms.SuggestionTable.SuggestionTable;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookItemStatus;
import com.ooops.lms.model.enums.BookReservationStatus;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdminBorrowDetailController extends BasicBorrowController {

    @FXML
    private Button addButotn;

    @FXML
    private TextField authorNameText;

    @FXML
    private TextField barCodeText;

    @FXML
    private TextField bookNameText;

    @FXML
    private TextField borowDateText;

    @FXML
    private TextField categoryText;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField emailText;

    @FXML
    private TextField genderText;

    @FXML
    private TextField memberIDText;

    @FXML
    private TextField memberNameText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private TextField returnDateText;

    @FXML
    private Button saveButton;

    @FXML
    private AnchorPane savePane;

    @FXML
    private Button scanBookButton;

    @FXML
    private Button scanMemberButton;

    @FXML
    private TextField statusText;

    @FXML
    private ScrollPane suggestionPane;

    @FXML
    private TextField totalOFBorrowText;

    @FXML
    private TextField totalOfLostText;

    @FXML
    private VBox suggestionVbox;

    @FXML
    private HBox mainDetailPane;
    @FXML
    private ImageView memberImage;
    @FXML
    private ImageView bookImage;
    @FXML
    private ChoiceBox<String> borrowStatus;

    private AdminBorrowPageController mainController;
    private boolean addMode;
    private Member member;
    private BookItem bookItem;
    private BookReservation bookReservation;
    private BookIssue bookIssue;
    private ObservableList<Member> membersSuggestList = FXCollections.observableArrayList();
    private ObservableList<Object> suggestList = FXCollections.observableArrayList();
    private SuggestionTable suggestionTable;
    private TextField followTextField;

    private boolean isSettingMember = false;
    private boolean isSettingBook = false;


    @FXML
    public void initialize() {
        borrowStatus.getItems().addAll(
                BookReservationStatus.WAITING.toString(),
                BookReservationStatus.CANCELED.toString(),
                BookReservationStatus.COMPLETED.toString(),
                BookIssueStatus.BORROWED.toString(),
                BookIssueStatus.LOST.toString(),
                BookIssueStatus.RETURNED.toString()
        );


        suggestionTable = new SuggestionTable(this.suggestionPane, this.suggestionVbox);
        // Đăng ký listener để xử lý sự kiện click
        suggestionTable.setRowClickListener(new SuggestionRowClickListener() {
            @Override
            public void onRowClick(Object o) {
                if (o instanceof Member) {
                    isSettingMember = true;
                    setMember((Member) o);
                    suggestionVbox.getChildren().clear();
                    suggestionPane.setVisible(false);
                } else if (o instanceof BookItem) {
                    isSettingBook = true;
                    setBookItem((BookItem) o);
                    suggestionVbox.getChildren().clear();
                    suggestionPane.setVisible(false);
                }
            }
        });

        bookNameText.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                Stage stage = (Stage) bookNameText.getScene().getWindow();

                stage.widthProperty().addListener((obs, oldWidth, newWidth) ->
                        Platform.runLater(() -> suggestionTable.updateSuggestionPaneForActiveField()));

                stage.heightProperty().addListener((obs, oldHeight, newHeight) ->
                        Platform.runLater(() -> suggestionTable.updateSuggestionPaneForActiveField()));
            }
        });

        memberNameText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!isSettingMember && addMode) {
                    suggestionTable.loadFindData("memberName", newValue);
                    suggestionTable.updateSuggestionPanePosition(memberNameText);
                }
            }
        });

        // Lắng nghe sự thay đổi trong TextField tìm kiếm
        memberIDText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!isSettingMember && addMode) {
                    suggestionTable.loadFindData("memberID", newValue);
                    suggestionTable.updateSuggestionPanePosition(memberIDText);
                }
                isSettingMember = false;
            }
        });

        bookNameText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!isSettingBook && addMode) {
                    suggestionTable.loadFindData("bookItemName", newValue);
                    suggestionTable.updateSuggestionPanePosition(bookNameText);
                }
            }
        });

        barCodeText.textProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!isSettingBook && addMode) {
                    suggestionTable.loadFindData("bookBarCode", newValue);
                    suggestionTable.updateSuggestionPanePosition(barCodeText);
                }
                isSettingBook = false;
            }
        });


    }

    void setMainController(AdminBorrowPageController mainController) {
        this.mainController = mainController;
    }

    @FXML
    void oSaveButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
        if (confrimYes) {
            setEditMode(false);
        }
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        if(borrowStatus.getValue().equals("Đang chờ") ||borrowStatus.getValue().equals("Đã xong") || borrowStatus.getValue().equals("Đã hủy") ) {
            if (getNewBookReservation()) {
                boolean confirmYes = CustomerAlter.showAlter("Bạn muốn thêm đơn đặt sách này?");
                if (confirmYes) {
                    System.out.println(bookReservation.getBookItem().getBarcode());
                    Command addCommand = new AdminCommand("add", this.bookReservation);
                    commandInvoker.setCommand(addCommand);
                    if (commandInvoker.executeCommand()) {
                        //.loadData();
                        setAddMode(false);
                        System.out.println("Đã lưu thay đổi");
                    }
                }
            }
        } else {
            if(getNewBookIssuel()) {
                boolean confirmYes = CustomerAlter.showAlter("Bạn muốn thêm đơn mượn sách này?");
                if (confirmYes) {
                    System.out.println(bookIssue.getBookItem().getBarcode());
                    Command addCommand = new AdminCommand("add", this.bookIssue);
                    commandInvoker.setCommand(addCommand);
                    if (commandInvoker.executeCommand()) {
                        //.loadData();
                        setAddMode(false);
                        System.out.println("Đã lưu thay đổi");
                    }
                }
            }
        }
    }

    private boolean getNewBookIssuel() {
        if (member == null) {
            CustomerAlter.showMessage("Hãy điền bạn đọc vào!");
            return false;
        }
        if (bookItem == null) {
            CustomerAlter.showMessage("Hãy điền thông tin sách mượn vào!");
            return false;
        }
        if (borowDateText == null) {
            CustomerAlter.showMessage("Không được để trống ngày mượn!");
            return false;
        }
        String reformattedDate = reformatDate(borowDateText.getText());
        String reformattedReturnDate = reformatDate(returnDateText.getText());
        bookIssue = new BookIssue(member,bookItem,reformattedDate,reformattedReturnDate);
        if (checkInformation(bookReservation)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean getNewBookReservation() {
        if (member == null) {
            CustomerAlter.showMessage("Hãy điền bạn đọc vào!");
            return false;
        }
        if (bookItem == null) {
            CustomerAlter.showMessage("Hãy điền thông tin sách mượn vào!");
            return false;
        }
        if (borowDateText == null) {
            CustomerAlter.showMessage("Không được để trống ngày mượn!");
            return false;
        }
        String reformattedDate = reformatDate(borowDateText.getText());
        String reformattedReturnDate = reformatDate(returnDateText.getText());
        bookReservation = new BookReservation(member, bookItem, reformattedDate, reformattedReturnDate);
        if (checkInformation(bookReservation)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkInformation(BookReservation bookReservation) {
        return true;
    }

    @FXML
    void onDeleteButton(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn xóa người này?");
        if (confirmYes) {
            setEditMode(false);
        }
    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        setEditMode(true);
    }

    @FXML
    void onScanButtonAction(ActionEvent event) {

    }

    public void loadStartStatus() {
        setAddMode(false);
        setEditMode(false);
    }

    public void setAddMode(boolean add) {
        this.addMode = add;

        editButton.setVisible(!add);
        addButotn.setVisible(add);

        scanBookButton.setVisible(add);
        scanMemberButton.setVisible(add);

        memberIDText.setEditable(add);
        memberNameText.setEditable(add);
        barCodeText.setEditable(add);
        bookNameText.setEditable(add);

        borowDateText.setEditable(add);
        returnDateText.setEditable(add);
        borrowStatus.setMouseTransparent(!add);

        scanBookButton.setMouseTransparent(!add);
        scanMemberButton.setMouseTransparent(!add);

        if (add) {
            saveButton.setVisible(!add);
            savePane.setVisible(!add);

            memberIDText.setText(null);
            setMemberTextFieldNull();
            memberImage.setImage(defaultUserImage);

            barCodeText.setText(null);
            setBookTextFielNull();
            bookImage.setImage(defaultUserImage);
            borrowStatus.setValue("Đang mượn");
            //Xử lý ngày tháng mượn
            LocalDate borrowDate = LocalDate.now();
            LocalDate returnDate = borrowDate.plusWeeks(2); // Thêm 2 tuần

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            borowDateText.setText(borrowDate.format(formatter));
            returnDateText.setText(returnDate.format(formatter));
        }

    }

    private void setMemberTextFieldNull() {
        memberNameText.setText(null);
        phoneNumberText.setText(null);
        emailText.setText(null);
        genderText.setText(null);
        totalOFBorrowText.setText(null);
        totalOfLostText.setText(null);
    }

    private void setBookTextFielNull() {
        bookNameText.setText(null);
        categoryText.setText(null);
        authorNameText.setText(null);

    }

    public void setEditMode(boolean edit) {
        editButton.setVisible(!edit);
        saveButton.setVisible(edit);
        deleteButton.setVisible(edit);
        savePane.setVisible(edit);

        memberIDText.setEditable(edit);
        barCodeText.setEditable(edit);

        borowDateText.setEditable(edit);
        returnDateText.setEditable(edit);
        borrowStatus.setMouseTransparent(!edit);

        scanBookButton.setMouseTransparent(edit);
        scanMemberButton.setMouseTransparent(edit);
    }

    public void setMember(Member member) {
        this.member = member;
        memberNameText.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName());
        memberIDText.setText(String.valueOf(member.getPerson().getId()));
        phoneNumberText.setText(member.getPerson().getPhone());
        emailText.setText(member.getPerson().getEmail());
        genderText.setText(member.getPerson().getGender().toString());
        totalOFBorrowText.setText(String.valueOf(member.getTotalBooksCheckOut()));
        memberImage.setImage(new Image(member.getPerson().getImagePath()));
        totalOfLostText.setText("chua co");
    }

    private void setBookItem(BookItem bookItem) {
        this.bookItem = bookItem;
        bookNameText.setText(bookItem.getTitle());
        barCodeText.setText(String.valueOf(bookItem.getBarcode()));
        categoryText.setText(getCategories(bookItem.getCategories()));
        authorNameText.setText(getAuthors(bookItem.getAuthors()));
        bookImage.setImage(new Image(bookItem.getImagePath()));
    }
}
