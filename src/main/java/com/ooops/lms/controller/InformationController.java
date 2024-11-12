package com.ooops.lms.controller;

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
import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.nio.file.Path;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class InformationController {
    @FXML
    private VBox contentBox;

    @FXML
    private TextField lastNameText;

    @FXML
    private TextField firstNameText;

    @FXML
    private TextField phoneText;

    @FXML
    private TextField emailText;

    @FXML
    private DatePicker birthDate;

    @FXML
    Label userIDLabel;

    @FXML
    ImageView avatarImage;

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
        lastNameText.setText(UserMenuController.member.getPerson().getLastName());
        firstNameText.setText(UserMenuController.member.getPerson().getFirstName());
        phoneText.setText(UserMenuController.member.getPerson().getPhone());
        emailText.setText(UserMenuController.member.getPerson().getEmail());
        genderChoiceBox.setValue(UserMenuController.member.getPerson().getGender().toString());
        String dateString = UserMenuController.member.getPerson().getDateOfBirth();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dateString, formatter);
        birthDate.setValue(parsedDate);

        String imagePath = UserMenuController.member.getPerson().getImagePath();
        imagePath = imagePath.replace("//", "/");
        if (imagePath.contains("image/avatar/default.png")) {
            avatarImage.setImage(new Image(getClass().getResourceAsStream("/" + imagePath)));
        } else {
            try {
                File imageFile = new File(imagePath);
                if (imageFile.exists()) {
                    avatarImage.setImage(new Image(new FileInputStream(imageFile)));
                } else {
                    System.out.println("File not found: " + imagePath);
                    avatarImage.setImage(new Image(getClass().getResourceAsStream("/image/avatar/default.png")));  // Hiển thị ảnh mặc định nếu không tìm thấy ảnh
                }
            } catch (IOException e) {
                System.out.println("Error loading image: " + e.getMessage());
                avatarImage.setImage(new Image(getClass().getResourceAsStream("/image/avatar/default.png")));  // Hiển thị ảnh mặc định nếu có lỗi
            }
        }

    }

    private File selectedFile;
    public void onLoadImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));

        Stage stage = (Stage) avatarImage.getScene().getWindow();
        selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            avatarImage.setImage(image);
            saveImageToFile();
        }
    }

    private void saveImageToFile() {
        if (selectedFile != null) {
            try {
                Path destinationPath = Path.of("user_avatar_uploads/" + selectedFile.getName());

                File destinationDirectory = new File("user_avatar_uploads/");
                if (!destinationDirectory.exists()) {
                    boolean created = destinationDirectory.mkdirs();
                    if (created) {
                        System.out.println("Directory created: " + destinationDirectory.getPath());
                    } else {
                        System.out.println("Failed to create directory.");
                    }
                }

                if (Files.exists(destinationPath)) {
                    System.out.println("File already exists, it will be replaced.");
                }

                Files.copy(selectedFile.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Image saved to: " + destinationPath.toString());
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Error saving image: " + e.getMessage());
            }
        } else {
            System.out.println("No image selected.");
        }
    }

    public void onSaveButtonAction(ActionEvent actionEvent) {
        UserMenuController.member.getPerson().setFirstName(firstNameText.getText());
        System.out.println(firstNameText.getText());
        System.out.println(UserMenuController.member.getPerson().getFirstName());
        UserMenuController.member.getPerson().setLastName(lastNameText.getText());
        UserMenuController.member.getPerson().setEmail(emailText.getText());
        UserMenuController.member.getPerson().setGender(Gender.valueOf(genderChoiceBox.getValue().toUpperCase()));
        UserMenuController.member.getPerson().setPhone(phoneText.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = birthDate.getValue().format(formatter);
        UserMenuController.member.getPerson().setDateOfBirth(formattedDate);

        if (selectedFile != null) {
            UserMenuController.member.getPerson().setImagePath("user_avatar_uploads/" + selectedFile.getName());
        }
        UserMenuController.updateMember();
        fxmlLoaderUtil.updateInfo();
    }
}
