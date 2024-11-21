package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.SuggestionTable.SuggestionRowClickListener;
import com.ooops.lms.SuggestionTable.SuggestionTable;
import com.ooops.lms.model.BookIssue;
import com.ooops.lms.model.BookItem;
import com.ooops.lms.model.BookReservation;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.enums.BookIssueStatus;
import com.ooops.lms.model.enums.BookReservationStatus;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminReservationDetailController extends BaseDetailController<BookReservation> {

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
    private ChoiceBox<BookReservationStatus> borrowStatus;
    @FXML
    private Label borrowIDLabel;
    @FXML
    private ListView<HBox> sugestionList;

    private Member member;
    private BookItem bookItem;
    private SuggestionTable suggestionTable;

    private boolean isSettingMember = false;
    private boolean isSettingBook = false;

    @Override
    protected void loadItemDetails() {
        getTitlePageStack().push(item.getReservationId() + "");
        member = item.getMember();
        setMember(member);

        bookItem = item.getBookItem();
        setBookItem(bookItem);

        setDateIssue();
    }

    @Override
    protected void updateAddModeUI() {
        editButton.setVisible(!addMode);
        addButotn.setVisible(addMode);

        scanBookButton.setVisible(addMode);
        scanMemberButton.setVisible(addMode);

        memberIDText.setEditable(addMode);
        memberNameText.setEditable(addMode);
        barCodeText.setEditable(addMode);
        bookNameText.setEditable(addMode);

        borowDateText.setEditable(addMode);
        returnDateText.setEditable(addMode);
        borrowStatus.setMouseTransparent(!addMode);

        scanBookButton.setMouseTransparent(!addMode);
        scanMemberButton.setMouseTransparent(!addMode);


        if (addMode) {
            saveButton.setVisible(!addMode);
            savePane.setVisible(!addMode);

            memberIDText.setText(null);
            setMemberTextFieldNull();
            memberImage.setImage(defaultUserImage);

            barCodeText.setText(null);
            setBookTextFielNull();
            bookImage.setImage(defaultUserImage);

            borrowIDLabel.setText(null);

            borrowStatus.setValue(BookReservationStatus.WAITING);
            //Xử lý ngày tháng mượn
            LocalDate borrowDate = LocalDate.now();
            LocalDate returnDate = borrowDate.plusWeeks(2); // Thêm 2 tuần

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            borowDateText.setText(borrowDate.format(formatter));
            returnDateText.setText(returnDate.format(formatter));
        }
    }

    @Override
    protected void updateEditModeUI() {
        editButton.setVisible(!editMode);
        saveButton.setVisible(editMode);
        deleteButton.setVisible(editMode);
        savePane.setVisible(editMode);

        memberIDText.setEditable(editMode);
        barCodeText.setEditable(editMode);

        borowDateText.setEditable(editMode);
        returnDateText.setEditable(editMode);
        borrowStatus.setMouseTransparent(!editMode);

        scanBookButton.setMouseTransparent(editMode);
        scanMemberButton.setMouseTransparent(editMode);
    }

    @Override
    protected boolean validateInput() {

        return true;
    }

    @Override
    protected boolean getNewItemInformation() throws Exception {
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
        item = new BookReservation(member, bookItem, reformattedDate, reformattedReturnDate);
        return true;
    }


    @FXML
    public void initialize() {
        borrowStatus.getItems().addAll(BookReservationStatus.values());


        suggestionTable = new SuggestionTable(this.suggestionPane, this.suggestionVbox, this.sugestionList);
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


    @FXML
    void oSaveButtonAction(ActionEvent event) {
        saveChanges();
    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        saveChanges();
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
        getTitlePageStack().push("Edit");
        setEditMode(true);
    }

    @FXML
    void onScanBookButtonAction(ActionEvent event) {
        Command scanCommand = new AdminCommand("scan", bookItem);
        commandInvoker.setCommand(scanCommand);
        if (commandInvoker.executeCommand()) {
            bookItem = ((AdminCommand) scanCommand).getBookItemResult();
            setBookItem(bookItem);
        }
    }

    @FXML
    void onScanMemberButtonAction(ActionEvent event) {
        Command scanCommand = new AdminCommand("scan", member);
        commandInvoker.setCommand(scanCommand);
        if (commandInvoker.executeCommand()) {
            member = ((AdminCommand) scanCommand).getMemberResult();
            setMember(member);
        }
    }

    public void loadStartStatus() {
        setAddMode(false);
        setEditMode(false);
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

    private void setDateIssue() {
        borrowIDLabel.setText(String.valueOf(item.getReservationId()));
        borowDateText.setText(String.valueOf(item.getCreatedDate()));
        returnDateText.setText(String.valueOf(item.getDueDate()));
    }

}
