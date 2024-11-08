package com.ooops.lms.controller;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.model.Member;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class AdminUserPageController extends BasicUserController{

    @FXML
    private AnchorPane messageLocationPane;

    @FXML
    private AnchorPane userDetailLocationPane;

    @FXML
    private AnchorPane userTableLocationPane;

    private AdminUserTableController adminUserTableController;
    private AdminUserDetailController adminUserDetailController;

    @FXML
    public void initialize() throws IOException, SQLException {
        userTableLocationPane.getChildren().add(userTablePane);
        adminUserTableController = userTablePaneLoader.getController();
        adminUserTableController.setMainController(this);

        messageLocationPane.getChildren().add(messagePane);

        userDetailLocationPane.getChildren().add(userDetailPane);
        adminUserDetailController = userDetailPaneLoader.getController();
        adminUserDetailController.setMainController(this);

    }

    public void loadDetail(Member member) {
        System.out.println("jdisjdijid");
        if(Objects.equals(adminUserDetailController.getMode(), "addMode")) {
            boolean confirmYes = CustomerAlter.showAlter("Thông tin bạn đang thêm sẽ bị mất");
            if(confirmYes) {
                adminUserDetailController.loadStartStatus();
                adminUserDetailController.setMember(member);
            }
        } else if(Objects.equals(adminUserDetailController.getMode(), "editMode")) {
            if(adminUserDetailController.getMember().equals(member)) {
                System.out.println("van mot nguoi ma dcm");
            } else {
                boolean confirmYes = CustomerAlter.showAlter("Thông tin bạn đang sửa sẽ bị mất");
                if(confirmYes) {
                    adminUserDetailController.loadStartStatus();
                    adminUserDetailController.setMember(member);
                }
            }
        } else if(Objects.equals(adminUserDetailController.getMode(), "noneMode")) {
            System.out.println("hdshud");
            adminUserDetailController.loadStartStatus();
            System.out.println("djsid");
            adminUserDetailController.setMember(member);
        } else {
            adminUserDetailController.setMember(member);
        }
    }

    public void loadAddPane() {
        if(Objects.equals(adminUserDetailController.getMode(), "editMode")) {
            boolean confirmYes = CustomerAlter.showAlter("Thông tin bạn đang sửa sẽ bị mất");
            if(confirmYes) {
                adminUserDetailController.loadStartStatus();
                adminUserDetailController.setAddMode(true);
            }
        } else if(Objects.equals(adminUserDetailController.getMode(), "noneMode")) {
            adminUserDetailController.setAddMode(true);
        }
    }

    public void loadData() {
        adminUserTableController.loadData();
    }

}
