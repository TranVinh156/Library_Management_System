package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.controller.BaseDetailController;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;
import com.ooops.lms.model.enums.Gender;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminUserDetailController extends BaseDetailController<Member> {
    @FXML
    private TextField birthText;

    @FXML
    private Button blockMember;

    @FXML
    private Button choiceImageButton;

    @FXML
    private Button editButton;

    @FXML
    private TextField emailText;

    @FXML
    private ChoiceBox<Gender> genderBox;

    @FXML
    private TextField memberIDText;

    @FXML
    private TextField memberNameText;

    @FXML
    private TextField numberOfBorrowText;

    @FXML
    private TextField numberOfLostText;

    @FXML
    private TextField phoneNumberText;

    @FXML
    private TextField resignDateText;

    @FXML
    private Button saveButton;

    @FXML
    private Button deleteButton;

    @FXML
    private ChoiceBox<AccountStatus> statusBox;

    @FXML
    private ImageView userImage;


    @Override
    protected void loadItemDetails() {
        emailText.setText(item.getPerson().getEmail());
        genderBox.setValue(item.getPerson().getGender());
        memberIDText.setText(String.valueOf(item.getPerson().getId()));
        memberNameText.setText(item.getPerson().getFirstName() + " " + item.getPerson().getLastName());
        numberOfBorrowText.setText(String.valueOf(item.getTotalBooksCheckOut()));
        numberOfLostText.setText("Chua co");
        phoneNumberText.setText(item.getPerson().getPhone());
        birthText.setText(reformatDate(item.getPerson().getDateOfBirth().toString()));
        statusBox.setValue(item.getStatus());

        //Xử lý ngày tháng đăng ký
        String resignDate = item.getCreatedDate().toString();
        String formatResignDate = reformatDate(resignDate);
        resignDateText.setText(formatResignDate);

        // Nếu như ảnh của member mà không có hoặc đường dẫn ảnh lỗi thì set mặc định
        if (item.getPerson().getImagePath() != null && isValidImagePath(item.getPerson().getImagePath())) {
            userImage.setImage(new Image(item.getPerson().getImagePath()));
        } else {
            userImage.setImage(defaultUserImage);
        }
    }
    @Override
    protected void updateAddModeUI() {
        // Xử lý ẩn hiện các nút
        editButton.setVisible(!addMode);
        choiceImageButton.setVisible(addMode);
        saveButton.setVisible(addMode);
        saveButton.setText("Add");

        //Cho phép người dùng nhập các trường
        emailText.setEditable(addMode);
        genderBox.setMouseTransparent(!addMode);
        memberNameText.setEditable(addMode);
        phoneNumberText.setEditable(addMode);
        resignDateText.setEditable(addMode);
        birthText.setEditable(addMode);
        statusBox.setMouseTransparent(!addMode);

        //Nếu như mà là mở addMode thì các trường sẽ rỗng (addMode == true)
        if (addMode) {
            userImage.setImage(defaultUserImage);
            emailText.setText(null);
            genderBox.setValue(null);
            birthText.setText(null);
            memberIDText.setText(null);
            memberNameText.setText(null);
            numberOfBorrowText.setText(null);
            numberOfLostText.setText(null);
            phoneNumberText.setText(null);
            resignDateText.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yy")));
            statusBox.setValue(AccountStatus.ACTIVE);
        }
    }
    @Override
    protected void updateEditModeUI() {
        //Xử lý các nút ẩn hiện
        editButton.setVisible(!editMode);
        choiceImageButton.setVisible(editMode);
        saveButton.setVisible(editMode);
        saveButton.setText("Save");
        deleteButton.setVisible(editMode);
        blockMember.setVisible(editMode);

        //Cho phép người dùng sửa các trường
        emailText.setEditable(editMode);
        genderBox.setMouseTransparent(!editMode);
        memberNameText.setEditable(editMode);
        phoneNumberText.setEditable(editMode);
        resignDateText.setEditable(editMode);
        birthText.setEditable(editMode);
        statusBox.setMouseTransparent(!editMode);
    }
    @Override
    protected boolean validateInput() {
        // Kiểm tra xem tên không được rỗng
        if (memberNameText.getText() == null || memberNameText.getText().isEmpty()) {
            CustomerAlter.showMessage("Tên không được để trống.");
            return false;
        }

        // Kiểm tra định dạng email hợp lệ
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (emailText.getText()== null || !emailText.getText().matches(emailRegex)) {
            CustomerAlter.showMessage("Email không hợp lệ");
            return false;
        }

        // Kiểm tra số điện thoại không rỗng và chỉ chứa số
        String phoneRegex = "^[0-9]{10,15}$"; // Ví dụ: chỉ chứa từ 10 đến 15 chữ số
        if (phoneNumberText.getText()== null || !phoneNumberText.getText().matches(phoneRegex)) {
            CustomerAlter.showMessage("Số điện thoại không hợp lệ.");
            return false;
        }

        // Kiểm tra giới tính không được rỗng
        if (genderBox.getValue() == null) {
            CustomerAlter.showMessage("Giới tính không được để trống.");
            return false;
        }

        // Kiểm tra ngày sinh hợp lệ
        if (!isValidDate(birthText.getText())) {
            return false;
        }

        // Nếu tất cả kiểm tra đều đạt, trả về true
        return true;
    }
    @Override
    protected boolean getNewItemInformation() throws Exception {
        if (item == null) {
            item = new Member(null,null,new Person());
        }
        // Xử lý firstName và lastName
        String memberName = memberNameText.getText();
        if (memberName != null) {
            String[] nameParts = memberName.trim().split("\\s+");
            item.getPerson().setFirstName(nameParts[0]); // Tên đầu tiên
            item.getPerson().setLastName(nameParts.length > 1 ? nameParts[nameParts.length - 1] : "");
        } else {
            item.getPerson().setFirstName(null);
            item.getPerson().setLastName(null);
        }

        // Lấy các thông tin khác.
        item.getPerson().setEmail(emailText.getText());
        item.getPerson().setGender(genderBox.getValue());
        item.getPerson().setPhone(phoneNumberText.getText());
        item.getPerson().setGender(genderBox.getValue());
        item.setStatus(statusBox.getValue());

        // đổi kiểu ngay từ dd/MM/yyyy thành yyyy-MM-dd
        String birthDate = birthText.getText();
        String reformattedDate = reformatDate(birthDate);
        item.getPerson().setDateOfBirth(reformattedDate);

        //nếu như person không có ảnh thì cho ảnh mặc định
        if (item.getPerson().getImagePath() == null) {
            item.getPerson().setImagePath(Person.DEFAULT_IMAGE_PATH);
        }

        return true;

    }

    @FXML
    void initialize() {
        genderBox.getItems().addAll(Gender.values());
        statusBox.getItems().addAll(AccountStatus.values());
    }

    @FXML
    void onBlockButtonAction(ActionEvent event) {
        if(item.getStatus().equals(AccountStatus.ACTIVE)) {
            boolean confirmYes = CustomerAlter.showAlter("Bạn block người đọc này á?");
            if (confirmYes) {
                boolean confirmYes2 = CustomerAlter.showAlter("Bạn block thật đấy à?");
                if (confirmYes2) {
                    Command blockCommand = new AdminCommand("block",this.item);
                    commandInvoker.setCommand(blockCommand);
                    if(commandInvoker.executeCommand()) {
                        statusBox.getSelectionModel().select(AccountStatus.BLOCKED);
                        System.out.println("Đã block người đọc");
                    }
                }
            }
        } else if(item.getStatus().equals(AccountStatus.BLOCKED)) {
            boolean confirmYes = CustomerAlter.showAlter("Bạn unblock người đọc này?");
            if (confirmYes) {
                Command unblockCommand = new AdminCommand("unblock",this.item);
                commandInvoker.setCommand(unblockCommand);
                if(commandInvoker.executeCommand()) {
                    statusBox.getSelectionModel().select(AccountStatus.ACTIVE);
                    System.out.println("Đã unblock người đọc");
                }
            }
        }
    }

    //Nút chọn ảnh
    @FXML
    void onChoiceImageButtonAction(ActionEvent event) {
        item.getPerson().setImagePath(getImagePath());

        // Xử lý nếu như có chọn ảnh thì trường avatar của người dùng hiện ảnh
        if (item.getPerson().getImagePath() != null) {
            Image image = new Image(item.getPerson().getImagePath());
            userImage.setImage(image);
        }
    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (item != null) {
            setEditMode(true);
        }
    }

    /**
     * Save button dùng chung cho cả AddMode và EditMode.
     *
     * @param event
     */
    @FXML
    void onSaveButtonAction(ActionEvent event) {
        saveChanges();
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn muốn xóa người dùng này?");
        if (confrimYes) {
            Command deleteCommand = new AdminCommand("delete", this.item);
            commandInvoker.setCommand(deleteCommand);
            if (commandInvoker.executeCommand()) {
                mainController.loadData();
                loadStartStatus();
                System.out.println("Đã xóa người dùng");
            } else {
                CustomerAlter.showMessage("Xóa người dùng thất bại");
            }
        } else {
            System.out.println("Tiếp tục edit");
        }
    }


    /**
     * Hàm để load bảng Detail về trạng thái rỗng ban đầu.
     */
    @Override
    public void loadStartStatus() {
        //Set member của bảng Detail rỗng
        item= null;

        // Set các trường nhập thành rỗng
        userImage.setImage(null);
        emailText.setText(null);
        genderBox.setValue(null);
        birthText.setText(null);
        memberIDText.setText(null);
        memberNameText.setText(null);
        numberOfBorrowText.setText(null);
        numberOfLostText.setText(null);
        phoneNumberText.setText(null);
        resignDateText.setText(null);
        statusBox.setValue(null);

        // Set trạng thái bảng về noneMode
        setAddMode(false);
        setEditMode(false);
    }

}
