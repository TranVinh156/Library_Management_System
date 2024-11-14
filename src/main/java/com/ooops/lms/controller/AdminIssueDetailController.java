package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AdminIssueDetailController extends BasicIssueController {

    @FXML
    private Label IDMemberLabel;

    @FXML
    private Label IDreportLabel;

    @FXML
    private TextArea detailIssueText;

    @FXML
    private Button editButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Label nameMemberLabel;

    @FXML
    private TextArea noteText;

    @FXML
    private Label phoneNumberLabel;

    @FXML
    private Button saveButton;

    @FXML
    private ChoiceBox<String> statusBox;

    @FXML
    private Label titelLabel;

    private boolean editMode;
    private AdminIssuePageController mainController;

    void setMainController(AdminIssuePageController mainController) {;
        this.mainController = mainController;
    }

    public void initialize() {
        statusBox.getItems().addAll("Chưa giải quyết","Đang giải quyết","Đã giải quyết");
    }

    @FXML
    private void onEditButtonAction(ActionEvent event) {
        setEditMode(true);
    }

    @FXML
    private void onSaveButtonAction(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
        if (confirmYes) {
            setEditMode(false);
            System.out.println("Đã lưu thay đổi");
        }
    }

    public void loadStartStatus() {
        setEditMode(editMode);
    }

    private void setEditMode(boolean addMode) {
        this.editMode = addMode;
        editButton.setVisible(!editMode);
        saveButton.setVisible(editMode);

        noteText.setMouseTransparent(!editMode);
        noteText.setEditable(editMode);
        statusBox.setMouseTransparent(!editMode);

    }


}
