package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.Command.AdminCommand;
import com.ooops.lms.Command.Command;
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

    private AdminUserPageController mainController;
    private boolean addMode = false;
    private boolean editMode = false;
    private Member member;

    /**
     * Hàm để set mainController (PageController).
     *
     * @param controller là PageController truyền vào
     */
    public void setMainController(AdminUserPageController controller) {
        this.mainController = controller;
    }

    @FXML
    void initialize() {
        genderBox.getItems().addAll(Gender.values());
        statusBox.getItems().addAll(AccountStatus.values());
    }

    @FXML
    void onBlockButtonAction(ActionEvent event) {
        if(member.getStatus().equals(AccountStatus.ACTIVE)) {
            boolean confirmYes = CustomerAlter.showAlter("Bạn block người đọc này á?");
            if (confirmYes) {
                boolean confirmYes2 = CustomerAlter.showAlter("Bạn block thật đấy à?");
                if (confirmYes2) {
                    Command blockCommand = new AdminCommand("block",this.member);
                    commandInvoker.setCommand(blockCommand);
                    if(commandInvoker.executeCommand()) {
                        statusBox.getSelectionModel().select(AccountStatus.BLOCKED);
                        System.out.println("Đã block người đọc");
                    }
                }
            }
        } else if(member.getStatus().equals(AccountStatus.BLOCKED)) {
            boolean confirmYes = CustomerAlter.showAlter("Bạn unblock người đọc này?");
            if (confirmYes) {
                Command unblockCommand = new AdminCommand("unblock",this.member);
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
        member.getPerson().setImagePath(getImagePath());

        // Xử lý nếu như có chọn ảnh thì trường avatar của người dùng hiện ảnh
        if (member.getPerson().getImagePath() != null) {
            Image image = new Image(member.getPerson().getImagePath());
            userImage.setImage(image);
        }
    }

    @FXML
    void onEditButtonAction(ActionEvent event) {
        if (member != null) {
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
        if (addMode) {
            if (getNewMemberInfomation()) {
                boolean confirmYes = CustomerAlter.showAlter("Thêm bạn đọc mới?"); //Nếu như thông tin của member đã hợp lệ để nhập vào CSDL
                if (confirmYes) {
                    //thêm người dùng vào CSDL
                    Command addCommand = new AdminCommand("add", this.member);
                    commandInvoker.setCommand(addCommand);
                    if (commandInvoker.executeCommand()) {
                        mainController.loadData(); // cập nhật lại UserTable
                        setAddMode(false);
                        System.out.println("Đã lưu thay đổi");
                    }
                }
            } else {
                System.out.println("Tiếp tục edit");
            }
        } else {
            boolean confirmYes = CustomerAlter.showAlter("Bạn có muốn lưu thay đổi này không?");
            if (getNewMemberInfomation()) {
                if (confirmYes) {
                    // sửa người dùng trong CSDL
                    Command editCommand = new AdminCommand("edit", this.member);
                    commandInvoker.setCommand(editCommand);
                    if (commandInvoker.executeCommand()) {
                        mainController.loadData(); // cập nhật lại UserTable
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
            Command deleteCommand = new AdminCommand("delete", this.member);
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
     * Set edit Mode.
     *
     * @param edit true nếu là bật, false nếu tắt.
     */
    private void setEditMode(boolean edit) {
        editMode = edit;
        //Xử lý các nút ẩn hiện
        editButton.setVisible(!edit);
        choiceImageButton.setVisible(edit);
        saveButton.setVisible(edit);
        saveButton.setText("Save");
        deleteButton.setVisible(edit);
        blockMember.setVisible(edit);

        //Cho phép người dùng sửa các trường
        emailText.setEditable(edit);
        genderBox.setMouseTransparent(!edit);
        memberNameText.setEditable(edit);
        phoneNumberText.setEditable(edit);
        resignDateText.setEditable(edit);
        birthText.setEditable(edit);
        statusBox.setMouseTransparent(!edit);

    }

    /**
     * Set add Mode.
     *
     * @param add true nếu bật, false nếu tắt.
     */
    public void setAddMode(boolean add) {
        addMode = add;
        // Xử lý ẩn hiện các nút
        editButton.setVisible(!add);
        choiceImageButton.setVisible(add);
        saveButton.setVisible(add);
        saveButton.setText("Add");

        //Cho phép người dùng nhập các trường
        emailText.setEditable(add);
        genderBox.setMouseTransparent(!add);
        memberNameText.setEditable(add);
        phoneNumberText.setEditable(add);
        resignDateText.setEditable(add);
        birthText.setEditable(add);
        statusBox.setMouseTransparent(!add);

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

    /**
     * Hàm dùng để lấy thông tin từ Member và gán thông tin cho các trường.
     *
     * @param member gán thông tin
     */
    public void setMember(Member member) {
        this.member = member;

        // Gán thông tin cho các trường
        emailText.setText(member.getPerson().getEmail());
        genderBox.setValue(member.getPerson().getGender());
        memberIDText.setText(String.valueOf(member.getPerson().getId()));
        memberNameText.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName());
        numberOfBorrowText.setText(String.valueOf(member.getTotalBooksCheckOut()));
        numberOfLostText.setText("Chua co");
        phoneNumberText.setText(member.getPerson().getPhone());
        birthText.setText(reformatDate(member.getPerson().getDateOfBirth().toString()));
        statusBox.setValue(member.getStatus());

        //Xử lý ngày tháng đăng ký
        String resignDate = member.getCreatedDate().toString();
        String formatResignDate = reformatDate(resignDate);
        resignDateText.setText(formatResignDate);

        // Nếu như ảnh của member mà không có hoặc đường dẫn ảnh lỗi thì set mặc định
        if (member.getPerson().getImagePath() != null && isValidImagePath(member.getPerson().getImagePath())) {
            userImage.setImage(new Image(member.getPerson().getImagePath()));
        } else {
            userImage.setImage(defaultUserImage);
        }
    }

    /**
     * Hàm lấy thông tin để truyền vào member của fxml này.
     * Kết hợp hàm kiểm tra thông tin để biết thông tin người dùng đã hợp lệ chưa.
     *
     * @return true/false
     */
    public boolean getNewMemberInfomation() {
        // Xử lý firstName và lastName
        String memberName = memberNameText.getText();
        if (memberName != null) {
            String[] nameParts = memberName.trim().split("\\s+");
            member.getPerson().setFirstName(nameParts[0]); // Tên đầu tiên
            member.getPerson().setLastName(nameParts.length > 1 ? nameParts[nameParts.length - 1] : "");
        } else {
            member.getPerson().setFirstName(null);
            member.getPerson().setLastName(null);
        }

        // Lấy các thông tin khác.
        member.getPerson().setEmail(emailText.getText());
        member.getPerson().setGender(genderBox.getValue());
        member.getPerson().setPhone(phoneNumberText.getText());
        member.getPerson().setGender(genderBox.getValue());
        member.setStatus(statusBox.getValue());

        // đổi kiểu ngay từ dd/MM/yyyy thành yyyy-MM-dd
        String birthDate = birthText.getText();
        String reformattedDate = reformatDate(birthDate);
        member.getPerson().setDateOfBirth(reformattedDate);

        //nếu như person không có ảnh thì cho ảnh mặc định
        if (member.getPerson().getImagePath() == null) {
            member.getPerson().setImagePath(Person.DEFAULT_IMAGE_PATH);
        }

        //Kiểm tra các thông tin của person đã hợp lệ hay chưa
        if (checkInformation(member.getPerson())) {
            return true; // đã có thể đẩy thông tin person vào CSDL
        } else {
            return false; // chưa thể đẩy thông tin person vào CSDL
        }

    }

    /**
     * Hàm kiểm tra thông tin của Person đã đúng chưa trước khi nhập vào CSDL.
     *
     * @param person là person cần kiểm tra thông tin
     * @return true/false
     */
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


    /**
     * Hàm kiểm tra tính hợp lệ của ngày tháng năm.
     *
     * @param dateStr ngày tháng cần kiểm tra
     * @return true/false
     */
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


    /**
     * Hàm chuyển đổi format cho ngày tháng năm.
     * Nếu là định dạng dd/MM/yyyy thì chuyển sang yyyy-MM-dd.
     * Nếu là định dạng yyyy-MM-dd thì chuyển sang định dạng dd/MM/yyyy.
     *
     * @param dateStr ngày tháng năm cần chuyển đổi
     * @return ngày tháng năm đã dược chuyển đổi
     */
    private static String reformatDate(String dateStr) {
        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");

        try {
            // Thử parse theo định dạng dd/MM/yyyy
            try {
                Date date = format1.parse(dateStr);
                return format2.format(date); // Chuyển sang yyyy-MM-dd
            } catch (ParseException e1) {
                // Nếu không được, thử parse theo định dạng yyyy-MM-dd
                Date date = format2.parse(dateStr);
                return format1.format(date); // Chuyển sang dd/MM/yyyy
            }
        } catch (ParseException e2) {
            // Nếu cả hai định dạng đều không đúng
            return null;
        }
    }


    /**
     * Hàm để load bảng Detail về trạng thái rỗng ban đầu.
     */
    public void loadStartStatus() {
        //Set member của bảng Detail rỗng
        member = null;

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

    /**
     * Hàm để lấy trạng thái của bảng Detail (add/edit/none)
     *
     * @return addMode/editMode/noneMode
     */
    public String getMode() {
        if (addMode) {
            return "addMode";
        } else if (editMode) {
            return "editMode";
        } else {
            return "noneMode";
        }
    }


    public Member getMember() {
        return this.member;
    }
}
