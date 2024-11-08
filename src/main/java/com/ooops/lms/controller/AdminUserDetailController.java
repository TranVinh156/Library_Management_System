package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
import com.ooops.lms.model.Admin;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.AccountStatus;
import com.ooops.lms.model.enums.Gender;
import com.ooops.lms.model.enums.MemberStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AdminUserDetailController extends BasicUserController {
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

    private Member member;
    private Person person = new Person();
    private String imagePath;

    private AdminUserPageController mainController;
    private boolean addMode = false;
    private boolean editMode = false;

    @FXML
    void initialize() {
        genderBox.getItems().addAll(Gender.values());
        statusBox.getItems().addAll(AccountStatus.values());
    }

    @FXML
    void onBlockButtonAction(ActionEvent event) {
        boolean confirmYes = CustomerAlter.showAlter("Bạn block người dùng này á?");
        if (confirmYes) {
            boolean confirmYes2 = CustomerAlter.showAlter("Bạn block thật đấy à?");
            if (confirmYes2) {
                Admin.blockMember(member);
                reloadMemberStatus();
            }
        }
    }

    @FXML
    void onChoiceImageButtonAction(ActionEvent event) {
        person.setImagePath(getImagePath());
        if (person.getImagePath() != null) {
            Image image = new Image(person.getImagePath());
            userImage.setImage(image);
        }
    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (member != null) {
            setEditMode(true);
        }
    }

    private void setEditMode(boolean edit) {
        editMode = edit;
        editButton.setVisible(!edit);
        choiceImageButton.setVisible(edit);
        saveButton.setVisible(edit);
        saveButton.setText("Save");
        deleteButton.setVisible(edit);
        blockMember.setVisible(edit);

        emailText.setEditable(edit);
        genderBox.setMouseTransparent(!edit);
        memberNameText.setEditable(edit);
        phoneNumberText.setEditable(edit);
        resignDateText.setEditable(edit);
        birthText.setEditable(edit);
        if (member != null && member.getPerson() != null && member.getPerson().getImagePath() != null) {
            userImage.setImage(new Image(member.getPerson().getImagePath()));
        } else {
            userImage.setImage(defaultUserImage);
        }

    }

    public void setAddMode(boolean add) {
        addMode = add;

        editButton.setVisible(!add);
        choiceImageButton.setVisible(add);
        saveButton.setVisible(add);

        emailText.setEditable(add);
        genderBox.setMouseTransparent(!add);
        memberNameText.setEditable(add);
        phoneNumberText.setEditable(add);
        resignDateText.setEditable(add);
        birthText.setEditable(add);
        saveButton.setText("Add");
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
        } else {
            if(member != null) {
                memberIDText.setText(String.valueOf(member.getPerson().getId()));
            }
        }
    }

    @FXML
    void onSaveButtonAction(ActionEvent event) {
        if (addMode) {
            if (getNewMemberInfomation()) {
                boolean confirmYes = CustomerAlter.showAlter("Thêm bạn đọc mới?");
                if (confirmYes) {
                    //thêm người dùng vào CSDL
                    Command addCommand = new AdminCommand("add", this.member);
                    commandInvoker.setCommand(addCommand);
                    if (commandInvoker.executeCommand()) {
                        mainController.registerNewMember(member);
                        System.out.println("Đã lưu thay đổi");
                        setAddMode(false);
                    }
                }
            } else {
                System.out.println("Tiếp tục edit");
            }
        } else {
            boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
            if (getNewMemberInfomation()) {
                if (confirmYes) {
                    //nhớ thêm hàm save thông tin ở đây
                    Command editCommand = new AdminCommand("edit", this.member);
                    commandInvoker.setCommand(editCommand);

                    if (commandInvoker.executeCommand()) {
                        setEditMode(false);
                        System.out.println("Đã lưu thay đổi");
                    }

                }
            } else {
                System.out.println("Tiếp tục edit");
            }
        }
    }

    @FXML
    void onDeleteButtonAction(ActionEvent event) {
        boolean confrimYes = CustomerAlter.showAlter("Bạn muốn xóa người dùng này?");
        if (confrimYes) {

        }
    }

    public void setMainController(AdminUserPageController controller) {
        this.mainController = controller;
    }

    public void setMember(Member member) {
        this.member = member;
        emailText.setText(member.getPerson().getEmail());
        genderBox.setValue(member.getPerson().getGender());
        memberIDText.setText(String.valueOf(member.getPerson().getId()));
        memberNameText.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName());
        numberOfBorrowText.setText(String.valueOf(member.getTotalBooksCheckOut()));
        numberOfLostText.setText("0");
        phoneNumberText.setText(member.getPerson().getPhone());
        birthText.setText(member.getPerson().getDateOfBirth().toString());
        resignDateText.setText("01.01.09");
        statusBox.setValue(member.getStatus());
        if (member.getPerson().getImagePath() != null && isValidImagePath(member.getPerson().getImagePath())) {
            userImage.setImage(new Image(member.getPerson().getImagePath()));
        } else {
            userImage.setImage(defaultUserImage);
        }

    }

    public boolean getNewMemberInfomation() {
        String memberName = memberNameText.getText();
        if (memberName != null) {
            String[] nameParts = memberName.trim().split("\\s+");
            person.setFirstName(nameParts[0]); // Tên đầu tiên
            person.setLastName(nameParts.length > 1 ? nameParts[nameParts.length - 1] : "");
        } else {
            person.setFirstName(null);
            person.setLastName(null);
        }
        person.setEmail(emailText.getText());
        person.setGender(genderBox.getValue());
        person.setPhone(phoneNumberText.getText());
        person.setGender(genderBox.getValue());

        // đổi kiểu ngay từ dd/MM/yyyy thành yyyy-MM-dd
        String birthDate = birthText.getText();
        String reformattedDate = reformatDate(birthDate);
        person.setDateOfBirth(reformattedDate);

        if (checkInformation(person)) {
            if (person.getImagePath() == null) {
                person.setImagePath(Person.DEFAULT_IMAGE_PATH);
            }
            this.member = new Member(null, null, person);
            member.setStatus(statusBox.getValue());
            return true;
        } else {
            return false;
        }

    }

    private boolean checkInformation(Person person) {
        // Kiểm tra xem tên không được rỗng
        if (person.getFirstName() == null || person.getFirstName().isEmpty()) {
            CustomerAlter.showMessage("Tên không được để trống.");
            return false;
        }

        // Kiểm tra định dạng email hợp lệ
        String emailRegex = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (person.getEmail() == null || !person.getEmail().matches(emailRegex)) {
            CustomerAlter.showMessage("Email không hợp lệ");
            return false;
        }

        // Kiểm tra số điện thoại không rỗng và chỉ chứa số
        String phoneRegex = "^[0-9]{10,15}$"; // Ví dụ: chỉ chứa từ 10 đến 15 chữ số
        if (person.getPhone() == null || !person.getPhone().matches(phoneRegex)) {
            CustomerAlter.showMessage("Số điện thoại không hợp lệ.");
            return false;
        }

        // Kiểm tra giới tính không được rỗng
        if (person.getGender() == null) {
            CustomerAlter.showMessage("Giới tính không được để trống.");
            return false;
        }

        // Kiểm tra ngày sinh hợp lệ
        if (!isValidDate(person.getDateOfBirth())) {
            return false;
        }

        // Nếu tất cả kiểm tra đều đạt, trả về true
        return true;

    }

    public static String reformatDate(String dateStr) {
        // Định dạng ban đầu
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy");
        // Định dạng mong muốn
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Chuyển chuỗi thành đối tượng Date theo định dạng đầu vào
            Date date = inputFormat.parse(dateStr);
            // Định dạng lại đối tượng Date theo định dạng đầu ra
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null; // Trả về null nếu có lỗi
        }
    }

    private boolean isValidDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // Không cho phép ngày không hợp lệ

        try {
            Date date = dateFormat.parse(dateStr); // Kiểm tra định dạng và tính hợp lệ của ngày

            // Kiểm tra ngày không phải là ngày trong tương lai
            if (date.after(new Date())) {
                CustomerAlter.showMessage("Ngày sinh không được là ngày trong tương lai.");
                return false;
            }
        } catch (ParseException e) {
            // Xảy ra ngoại lệ nếu định dạng ngày không hợp lệ
            CustomerAlter.showMessage("Ngày sinh không hợp lệ");
            return false;
        }
        return true;
    }

    public void loadStartStatus() {
        person = new Person();
        member = new Member(null, null, null);
        setAddMode(false);
        setEditMode(false);
    }

    public Member getMember() {
        return member;
    }

    private void reloadMemberStatus() {

    }

    public String getMode() {
        if (addMode) {
            return "addMode";
        } else if (editMode) {
            return "editMode";
        } else {
            return "noneMode";
        }
    }
}
