package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.model.datatype.Person;
import com.ooops.lms.model.enums.Role;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ResignCommand implements Command {
    private Stage stage;
    private Person person;
    private String username;
    private String password;
    public ResignCommand(Stage stage, Person person, String username, String password) {
        this.stage = stage;
        this.person = person;
        this.username = username;
        this.password = password;
    }

    @Override
    public boolean execute() {
        try {
            if (AccountDAO.getInstance().registerMember(person, username, password)) {
                CustomerAlter.showMessage("Đăng ký tài khoản thành công.");
                return true;
            }
        }catch (SQLException e){
            CustomerAlter.showMessage(e.getMessage());
        }
        return false;
    }

}
