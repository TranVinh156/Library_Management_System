package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.controller.AdminMenuController;
import com.ooops.lms.controller.BasicController;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.model.enums.Role;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import com.ooops.lms.controller.UserMenuController;
public class LoginCommand extends BasicController implements Command {
    private Role role;
    private String password;
    private String username;
    private Stage stage;
    private int resultMemberID;
    private int adminID;

    public LoginCommand(Stage stage, Role role, String username, String password) {
        this.role = role;
        this.username = username;
        this.password = password;
        this.stage = stage;
    }

    @Override
    public boolean execute() {
        try {
            if (username.isEmpty() || password.isEmpty()) {
                System.out.println("Username or password is empty");
            }
            if (isAuthenticated()) {
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
            if(AccountDAO.getInstance().validateMemberLogin(username, password) != 0) {
                this.resultMemberID = AccountDAO.getInstance().validateMemberLogin(username,password);
               return CustomerAlter.showAlter("Hế lô chào mừng bạn tới với chúng tớ nhé");

            }
        } else if (role.equals(Role.ADMIN)) {
            if (AccountDAO.getInstance().validateAdminLogin(username, password)!=0) {
                this.adminID = AccountDAO.getInstance().validateAdminLogin(username,password);
                return CustomerAlter.showAlter("Hế lô chào mừng bạn tới với chúng tớ nhé");
            }
        }
        CustomerAlter.showAlter("Thông tin đăng nhập sai rồi ó.");
        return false;
    }

    private void openMenu() {
        if (role.equals(Role.ADMIN)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/AdminMenu.fxml"));
                Parent root = fxmlLoader.load();

                AdminMenuController controller = fxmlLoader.getController();
                controller.setAdminID(adminID);

                Scene scene = new Scene(root);
                this.stage.setResizable(true);
                stage.setWidth(stage.getWidth());
                stage.setHeight(stage.getHeight());
                this.stage.setScene(scene);
                this.stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (role.equals(Role.NONE)) {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/ooops/lms/library_management_system/UserMenu-view.fxml"));
                Parent root = fxmlLoader.load();

                UserMenuController userMenu = fxmlLoader.getController();

                userMenu.setMemberID(resultMemberID);
                stage.setResizable(true);
                stage.setWidth(stage.getWidth());
                stage.setHeight(stage.getHeight());
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
