package com.ooops.lms.Command;

import javafx.stage.Stage;

public class LoginCommand implements Command {
    private String role;
    private Stage stage;

    public LoginCommand(String role, Stage stage) {
        this.role = role;
        this.stage = stage;
    }
    @Override
    public void execute() {
        if("Admin".equals(role)) {

        } else if("User".equals(role)) {

        }
    }
}
