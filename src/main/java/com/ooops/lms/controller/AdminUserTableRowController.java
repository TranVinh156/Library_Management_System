package com.ooops.lms.controller;

import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class AdminUserTableRowController extends BasicUserController {

    @FXML
    private HBox mainRowHbox;
    @FXML
    private Label memberIDlabel;

    @FXML
    private Label memberNameLabel;

    @FXML
    private Label phoneNumberLabel;

    private AdminUserPageController mainController;
    private static AdminUserTableRowController selectedRow = null;
    private Member member;

    /**
     * Hàm để set mainController (PageController).
     *
     * @param controller là PageController truyền vào
     */
    public void setMainController(AdminUserPageController controller) {
        this.mainController = controller;
    }

    public void initialize() {
        //Nếu người dùng click vào row thì gọi handleClickRow
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleClickRow();
            }
        });

    }

    /**
     * Hàm truyền member đi và load thông tin trên bảng Detail.
     * Xử lý nếu như đang load Detail của member nào thì memberRow đấy được tô đậm.
     */
    public void handleClickRow() {
        //Xử lý màu cho Row được chọn
        if (selectedRow != null) {
            selectedRow.resetRowColor(); // Khôi phục màu nền của hàng trước
        }
        selectedRow = this;
        mainRowHbox.setStyle("-fx-background-color: #DDDCDC;");

        //Load thông tin member trên bảng Detail
        mainController.loadDetail(this.member);
    }

    /**
     * Khôi phục lại màu nền mặc định cho row
     */
    public void resetRowColor() {
        mainRowHbox.setStyle("");
    }

    /**
     * Set member cho Row.
     *
     * @param member
     */
    public void setMember(Member member) {
        this.member = member;

        //Gán thông tin cho các trường ID,Name,SDT
        memberIDlabel.setText(String.valueOf(member.getPerson().getId()));
        memberNameLabel.setText(member.getPerson().getFirstName() + " " + member.getPerson().getLastName());
        phoneNumberLabel.setText(member.getPerson().getPhone());
    }
}