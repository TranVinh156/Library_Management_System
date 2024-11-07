package com.ooops.lms.controller;

import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.model.Member;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Gender;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AdminUserTableController extends BasicUserController {


    @FXML
    private Button addButton;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField searchText;

    @FXML
    private VBox userTableVbox;

    private AccountDAO accountDAO;

    private ObservableList<Member> membersList;

    private AdminUserPageController mainController;

    public void addMember(Member member) {
        membersList.add(member);
        System.out.println("đã thêm");
        loadRows();
    }

    @FXML
    public void initialize() {
        membersList = FXCollections.observableArrayList();
        membersList.add(new Member("user1", "password1", new Person("John", "Doe",null , Gender.Male,"1990-01-01", "john.doe@example.com", "0123456788")));
        membersList.add(new Member("user2", "password2", new Person("Johny", "Doee",null , Gender.Female,"1990-02-01", "johny.doee@example.com", "0123556789")));
        membersList.add(new Member("user3", "password3",new Person("Johnny", "Doeee",null , Gender.Other,"1990-03-01", "johnny.doeee@example.com", "0223456789")));

    }

    @FXML
    void onAddButtonAction(ActionEvent event) {
        mainController.loadAddPane();
    }

    public void setMainController(AdminUserPageController controller) {
        this.mainController = controller;
        loadRows();
        setVboxFitWithScrollPane();
    }

    public void loadRows() {
        userTableVbox.getChildren().clear();
        for (Member member : membersList) {
            try {
                FXMLLoader loader =new FXMLLoader(getClass().getResource(USER_TABLE_ROW_FXML)); // Use the actual path
                HBox row = loader.load();

                AdminUserTableRowController rowController = loader.getController();
                rowController.setMainController(mainController);
                rowController.setMember(member);
                childFitWidthParent(row,scrollPane);
                userTableVbox.getChildren().add(row);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace(); // Bắt các lỗi khác
            }
        }
    }

    private void setVboxFitWithScrollPane() {
        childFitWidthParent(userTableVbox,scrollPane);
        childFitHeightParent(userTableVbox,scrollPane);
    }

}
