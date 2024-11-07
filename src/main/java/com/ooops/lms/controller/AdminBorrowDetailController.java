package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminBorrowDetailController {

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
    private TextField totalOFBorrowText;

    @FXML
    private TextField totalOfLostText;

    private AdminBorrowPageController mainController;
    private boolean addMode;

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
        boolean confirmYes = CustomerAlter.showAlter("Bạn muốn thêm người này?");
        if (confirmYes) {}
        setAddMode(false);
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
        barCodeText.setEditable(add);

        borowDateText.setEditable(add);
        returnDateText.setEditable(add);
        statusText.setEditable(add);

        scanBookButton.setMouseTransparent(!add);
        scanMemberButton.setMouseTransparent(!add);

        if(add) {
            saveButton.setVisible(!add);
            savePane.setVisible(!add);

            memberNameText.setText(null);
            memberIDText.setText(null);
            memberNameText.setText(null);
            phoneNumberText.setText(null);
            emailText.setText(null);
            genderText.setText(null);
            totalOFBorrowText.setText(null);
            totalOfLostText.setText(null);

            barCodeText.setText(null);
            bookNameText.setText(null);
            categoryText.setText(null);
            authorNameText.setText(null);

            statusText.setText(null);
            borowDateText.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
            returnDateText.setText(null);
        }

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
        statusText.setEditable(edit);

        scanBookButton.setMouseTransparent(edit);
        scanMemberButton.setMouseTransparent(edit);
    }

}
