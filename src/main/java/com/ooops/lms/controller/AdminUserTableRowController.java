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

    public void initialize()  {
        mainRowHbox.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                handleClickRow();
            }
        });

    }

    public void setMainController(AdminUserPageController controller) {
       this.mainController = controller;
    }

    public void setMember(Member member) {
        this.member = member;
        memberIDlabel.setText("Chua co");
        memberNameLabel.setText(member.getPerson().getFirstName()+" "+member.getPerson().getLastName());
        phoneNumberLabel.setText(member.getPerson().getPhone());
    }

    public void handleClickRow() {
        System.out.println("Click CLick");
        if (selectedRow != null) {
            selectedRow.resetRowColor(); // Khôi phục màu nền của hàng trước
        }
        selectedRow = this;

        mainRowHbox.setStyle("-fx-background-color: #DDDCDC;");
        mainController.loadDetail(member);
    }

    public void resetRowColor() {
        mainRowHbox.setStyle(""); // Khôi phục lại màu nền mặc định
    }

}