package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.model.Account;
import com.ooops.lms.model.enums.Role;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginCommand implements Command {
    private Role role;
    private AccountDAO accountDAO = new AccountDAO();
    private String password;
    private String username;
    private Stage stage;

    public LoginCommand(Stage stage,Role role, String username, String password) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.stage = stage;
    }

    @Override
    public boolean execute() {
        try {
            if(username.isEmpty() || password.isEmpty()) {
                System.out.println("Username or password is empty");
            }
            if (isAuthenticated()){
                openMenu();
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    private boolean isAuthenticated() throws SQLException {
        if (role.equals(Role.NONE)) {
            if (accountDAO.validateMemberLogin(username, password)) {
                CustomerAlter.showAlter("Đăng nhập thành công");
                openMenu();
                return true;
            }
        } else if (role.equals(Role.ADMIN)) {
            if (accountDAO.validateAdminLogin(username, password)) {
                CustomerAlter.showAlter("Đăng nhập thành công");
                openMenu();
                return true;
            }
        }
        CustomerAlter.showAlter("Thông tin đăng nhập không chính xác.");
        return false;
    }
    private void openMenu() {
        if(role.equals(Role.ADMIN)) {
            loadView("/com/ooops/lms/library_management_system/AdminMenu.fxml");
        } else if (role.equals(Role.NONE)) {
            loadView("/com/ooops/lms/library_management_system/UserMenu-view.fxml");
        }
    }

    private void loadView(String fxmlPath) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(fxmlLoader.load());
            //stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
