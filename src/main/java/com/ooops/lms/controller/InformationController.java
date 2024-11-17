package com.ooops.lms.controller;

import com.google.common.io.MoreFiles;
import com.ooops.lms.database.dao.MemberDAO;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Gender;
import com.ooops.lms.util.FXMLLoaderUtil;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.io.IOException;


public class InformationController {
    @FXML
    private VBox contentBox;

    @FXML
    private TextField phoneText, emailText, lastNameText,firstNameText;

    @FXML
    private DatePicker birthDate;

    @FXML
    private ChoiceBox<String> genderChoiceBox;

    @FXML
    private Label userIDText;

    @FXML
    private ImageView avatarImage;

    private static final String USER_MENU_FXML = "/com/ooops/lms/library_management_system/UserMenu-view.fxml";
    private static final String SETTING_FXML = "/com/ooops/lms/library_management_system/Setting-view.fxml";
    private String newImageFile;
    private FXMLLoaderUtil fxmlLoaderUtil = FXMLLoaderUtil.getInstance();

    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) fxmlLoaderUtil.loadFXML(SETTING_FXML);
        if (content != null) {
            fxmlLoaderUtil.updateContentBox(content);
        }
    }

    public void initialize() {
        genderChoiceBox.getItems().addAll("nữ", "nam", "bê đê slay");
        showInfo();
    }

    private void showInfo() {
        lastNameText.setText(UserMenuController.member.getPerson().getLastName());
        firstNameText.setText(UserMenuController.member.getPerson().getFirstName());
        phoneText.setText(UserMenuController.member.getPerson().getPhone());
        emailText.setText(UserMenuController.member.getPerson().getEmail());
        genderChoiceBox.setValue(UserMenuController.member.getPerson().getGender().toString());
        String dateString = UserMenuController.member.getPerson().getDateOfBirth();
        userIDText.setText(Integer.toString(UserMenuController.member.getPerson().getId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dateString, formatter);
        birthDate.setValue(parsedDate);

        String imagePath = UserMenuController.member.getPerson().getImagePath();

        try {
            File file = new File(imagePath);
            avatarImage.setImage(new Image(file.toURI().toString()));
        } catch (Exception e) {
            avatarImage.setImage(new Image(getClass().getResourceAsStream("/image/avatar/default.png")));
        }
    }

    public void onLoadImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh để tải lên");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog((Stage) this.userIDText.getScene().getWindow());
        if (selectedFile != null) {
            // Tạo tên tệp mới dựa trên ID người dùng
            String imageFile = userIDText.getText() + getFileExtension(selectedFile.toPath());
            newImageFile = "Library_Management_System/avatar/"+ imageFile;

            Path avatarFolder = Paths.get("Library_Management_System\\avatar");

            try {
                if (Files.notExists(avatarFolder)) {
                    Files.createDirectories(avatarFolder);
                }

                Path destinationPath = avatarFolder.resolve(imageFile);

                if (Files.exists(destinationPath)) {
                    Files.delete(destinationPath);
                }

                Files.copy(selectedFile.toPath(), destinationPath);

                Image image = new Image(destinationPath.toUri().toString());
                avatarImage.setImage(image);
                avatarImage.setPreserveRatio(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getFileExtension(Path path) {
        String fileName = path.toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
        if(newImageFile == null) {
            System.out.println("a");
        }
        UserMenuController.member.getPerson().setImagePath(newImageFile);
        UserMenuController.member.getPerson().setLastName(lastNameText.getText());
        UserMenuController.member.getPerson().setFirstName(firstNameText.getText());
        if(genderChoiceBox.getValue().toString().equals("nữ")) {
            UserMenuController.member.getPerson().setGender(Gender.FEMALE);
        } else if(genderChoiceBox.getValue().toString().equals("nam")) {
            UserMenuController.member.getPerson().setGender(Gender.MALE);
        } else {
            UserMenuController.member.getPerson().setGender(Gender.OTHER);
        }
        UserMenuController.member.getPerson().setEmail(emailText.getText());
        UserMenuController.member.getPerson().setPhone(phoneText.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = birthDate.getValue().format(formatter);
        UserMenuController.member.getPerson().setDateOfBirth(formattedDate);

        try {
            MemberDAO.getInstance().update(UserMenuController.member);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            UserMenuController userMenuController = fxmlLoaderUtil.getController(USER_MENU_FXML);
            userMenuController.showInfo();
            SettingController settingController = fxmlLoaderUtil.getController(SETTING_FXML);
            settingController.showInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
