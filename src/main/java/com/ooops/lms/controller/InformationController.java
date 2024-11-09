package com.ooops.lms.controller;

import com.ooops.lms.model.Member;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InformationController {
    @FXML
    private VBox contentBox;

    @FXML
    private TextField nameText;

    @FXML
    private TextField phoneText;

    @FXML
    private TextField emailText;

    @FXML
    private DatePicker birthDate;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";

    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void initialize() {
        genderChoiceBox.getItems().addAll("FEMALE", "MALE", "OTHER");
        showInfo();
    }

    private void showInfo() {
        nameText.setText(UserMenuController.member.getPerson().getLastName() + " "
                + UserMenuController.member.getPerson().getFirstName());
        phoneText.setText(UserMenuController.member.getPerson().getPhone());
        emailText.setText(UserMenuController.member.getPerson().getEmail());
        genderChoiceBox.setValue(UserMenuController.member.getPerson().getGender().toString());
        String dateString = UserMenuController.member.getPerson().getDateOfBirth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dateString, formatter);
        birthDate.setValue(parsedDate);
    }

}
