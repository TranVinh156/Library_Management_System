package com.ooops.lms.controller;

import com.ooops.lms.controller.BaseDetailController;
import com.ooops.lms.model.Report;
import com.ooops.lms.model.enums.ReportStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class AdminIssueDetailController extends BaseDetailController<Report> {

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
    private ChoiceBox<ReportStatus> statusBox;

    @FXML
    private Label titelLabel;

    public void initialize() {
        statusBox.getItems().addAll(ReportStatus.values());
    }

    @Override
    protected void loadItemDetails() {
        getTitlePageStack().push(item.getReportID() + "");
        IDreportLabel.setText(String.valueOf(item.getReportID()));
        nameMemberLabel.setText(item.getMember().getPerson().getFirstName() + " " + item.getMember().getPerson().getLastName());
        IDMemberLabel.setText(String.valueOf(item.getMember().getPerson().getId()));
        emailLabel.setText(item.getMember().getPerson().getEmail());
        noteText.setText("Chua co");
        titelLabel.setText(item.getTitle());
        detailIssueText.setText(item.getTitle());
    }

    @Override
    protected void updateAddModeUI() {

    }

    @Override
    protected void updateEditModeUI() {
        editButton.setVisible(!editMode);
        saveButton.setVisible(editMode);

        noteText.setMouseTransparent(!editMode);
        noteText.setEditable(editMode);
        statusBox.setMouseTransparent(!editMode);
    }

    @Override
    public void loadStartStatus() {
        setEditMode(editMode);
    }
    @Override
    protected boolean validateInput() {
        return true;
    }

    @Override
    protected boolean getNewItemInformation() throws Exception {
        return true;
    }


    @FXML
    private void onEditButtonAction(ActionEvent event) {
        getTitlePageStack().push("Edit");
        setEditMode(true);
    }

    @FXML
    private void onSaveButtonAction(ActionEvent event) {
        saveChanges();
    }


}
