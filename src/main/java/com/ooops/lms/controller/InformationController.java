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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.print.DocFlavor;
import java.awt.image.BufferedImage;
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
        lastNameText.setText(UserMenuController.getMember().getPerson().getLastName());
        firstNameText.setText(UserMenuController.getMember().getPerson().getFirstName());
        phoneText.setText(UserMenuController.getMember().getPerson().getPhone());
        emailText.setText(UserMenuController.getMember().getPerson().getEmail());
        genderChoiceBox.setValue(UserMenuController.getMember().getPerson().getGender().toString());
        String dateString = UserMenuController.getMember().getPerson().getDateOfBirth();
        userIDText.setText(Integer.toString(UserMenuController.getMember().getPerson().getId()));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(dateString, formatter);
        birthDate.setValue(parsedDate);

        String imagePath = UserMenuController.getMember().getPerson().getImagePath();

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
            newImageFile = "avatar/" + imageFile;

            Path avatarFolder = Paths.get("avatar");

            try {
                // Tạo thư mục nếu chưa tồn tại
                if (Files.notExists(avatarFolder)) {
                    Files.createDirectories(avatarFolder);
                }

                Path destinationPath = avatarFolder.resolve(imageFile);

                // Xóa file nếu nó đã tồn tại
                if (Files.exists(destinationPath)) {
                    Files.delete(destinationPath);
                }

                // Đọc ảnh gốc từ file
                BufferedImage originalImage = ImageIO.read(selectedFile);

                // Tính toán chiều dài của hình vuông
                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                int size = Math.min(width, height);  // Lấy cạnh nhỏ nhất làm kích thước vuông

                // Cắt ảnh thành hình vuông ở giữa
                int x = (width - size) / 2;
                int y = (height - size) / 2;

                BufferedImage squareImage = originalImage.getSubimage(x, y, size, size);

                // Lưu ảnh vuông vào tệp
                ImageIO.write(squareImage, "PNG", destinationPath.toFile());

                // Tải ảnh vuông vào ImageView
                Image image = new Image(destinationPath.toUri().toString());
                avatarImage.setImage(image);
                avatarImage.setPreserveRatio(true); // Đảm bảo tỉ lệ ảnh được giữ nguyên trong ImageView

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
            System.out.println("there is no image to save");
        }
        UserMenuController.getMember().getPerson().setImagePath(newImageFile);
        UserMenuController.getMember().getPerson().setLastName(lastNameText.getText());
        UserMenuController.getMember().getPerson().setFirstName(firstNameText.getText());
        if(genderChoiceBox.getValue().toString().equals("nữ")) {
            UserMenuController.getMember().getPerson().setGender(Gender.FEMALE);
        } else if(genderChoiceBox.getValue().toString().equals("nam")) {
            UserMenuController.getMember().getPerson().setGender(Gender.MALE);
        } else {
            UserMenuController.getMember().getPerson().setGender(Gender.OTHER);
        }
        UserMenuController.getMember().getPerson().setEmail(emailText.getText());
        UserMenuController.getMember().getPerson().setPhone(phoneText.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = birthDate.getValue().format(formatter);
        UserMenuController.getMember().getPerson().setDateOfBirth(formattedDate);

        try {
            MemberDAO.getInstance().update(UserMenuController.getMember());
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
