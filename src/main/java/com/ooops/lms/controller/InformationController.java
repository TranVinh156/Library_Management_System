package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.MemberDAO;
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

    /**
     * về Setting.
     * @param actionEvent khi ấn
     */
    public void onBackButtonAction(ActionEvent actionEvent) {
        VBox content = (VBox) FXMLLoaderUtil.getInstance().loadFXML(SETTING_FXML);
        if (content != null) {
            FXMLLoaderUtil.getInstance().updateContentBox(content);
        }
    }

    public void initialize() {
        genderChoiceBox.getItems().addAll("nữ", "nam", "bê đê slay");
        showInfo();
    }

    /**
     * hiển thị thông tin của user
     */
    public void showInfo() {
        lastNameText.setText(UserMenuController.getMember().getPerson().getLastName());
        firstNameText.setText(UserMenuController.getMember().getPerson().getFirstName());
        phoneText.setText(UserMenuController.getMember().getPerson().getPhone());
        emailText.setText(UserMenuController.getMember().getPerson().getEmail());
        genderChoiceBox.setValue(getGender(UserMenuController.getMember().getPerson().getGender().toString()));
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

    /**
     * tải ảnh lên từ local folder.
     * @param actionEvent khi ấn
     */
    public void onLoadImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Chọn ảnh để tải lên");

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );

        File selectedFile = fileChooser.showOpenDialog((Stage) this.userIDText.getScene().getWindow());
        if (selectedFile != null) {
            String imageFile = userIDText.getText() + getFileExtension(selectedFile.toPath());
            newImageFile = "avatar/" + imageFile;

            Path avatarFolder = Paths.get("avatar");

            try {
                if (Files.notExists(avatarFolder)) {
                    Files.createDirectories(avatarFolder);
                }

                Path destinationPath = avatarFolder.resolve(imageFile);

                if (Files.exists(destinationPath)) {
                    Files.delete(destinationPath);
                }

                BufferedImage originalImage = ImageIO.read(selectedFile);

                int width = originalImage.getWidth();
                int height = originalImage.getHeight();
                int size = Math.min(width, height);

                int x = (width - size) / 2;
                int y = (height - size) / 2;

                BufferedImage squareImage = originalImage.getSubimage(x, y, size, size);

                ImageIO.write(squareImage, "PNG", destinationPath.toFile());

                Image image = new Image(destinationPath.toUri().toString());
                avatarImage.setImage(image);
                avatarImage.setPreserveRatio(true);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * tìm cái đuôi file ảnh.
     * @param path đường dẫn
     * @return đuôi file ảnh
     */
    private String getFileExtension(Path path) {
        String fileName = path.toString();
        int dotIndex = fileName.lastIndexOf('.');
        if (dotIndex > 0) {
            return fileName.substring(dotIndex);
        }
        return "";
    }

    /**
     * lưu ảnh.
     * @param actionEvent ấn vào
     */
    public void onSaveButtonAction(ActionEvent actionEvent) {
        if(newImageFile == null) {
            System.out.println("there is no image to save");
        } else {
            UserMenuController.getMember().getPerson().setImagePath(newImageFile);
        }
        UserMenuController.getMember().getPerson().setLastName(lastNameText.getText());
        UserMenuController.getMember().getPerson().setFirstName(firstNameText.getText());
        UserMenuController.getMember().getPerson().setGender(getGenderEnum(genderChoiceBox.getValue().toString()));

        UserMenuController.getMember().getPerson().setEmail(emailText.getText());
        UserMenuController.getMember().getPerson().setPhone(phoneText.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = birthDate.getValue().format(formatter);
        UserMenuController.getMember().getPerson().setDateOfBirth(formattedDate);

        try {
            MemberDAO.getInstance().update(UserMenuController.getMember());
            CustomerAlter.showMessage("Đã lưu");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try {
            UserMenuController userMenuController = FXMLLoaderUtil.getInstance().getController(USER_MENU_FXML);
            userMenuController.showInfo();
            SettingController settingController = FXMLLoaderUtil.getInstance().getController(SETTING_FXML);
            settingController.showInfo();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * trả về String giới tính
     * @param genderEnum enum giới tính
     * @return String giới tính
     */
    private String getGender(String genderEnum) {
        if(genderEnum.equals("MALE")) {
            return "nam";
        } else if(genderEnum.equals("FEMALE")) {
            return "nữ";
        }
        return "bê đê slay";
    }

    /**
     * trả về enum giới tính
     * @param gender String
     * @return enum
     */
    private Gender getGenderEnum(String gender) {
        if(gender.equals("nữ")) {
            return Gender.FEMALE;
        } else if(gender.equals("nam")) {
            return Gender.MALE;
        }
        return Gender.OTHER;
    }
}
