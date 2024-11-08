package com.ooops.lms.Command;

public class AdminCommand implements Command {
    private String action;
    private Object data;

    public AdminCommand(String action, Object data) {
        this.action = action;
        this.data = data;
    }

    public void execute() {
        switch (action) {
            case "add":
                break;
            case "delete":
                break;
            case "edit":
                break;
            default:
                break;
        }
    }
}
