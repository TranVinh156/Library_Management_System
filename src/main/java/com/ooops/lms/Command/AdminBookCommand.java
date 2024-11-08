package com.ooops.lms.Command;

public class AdminBookCommand implements Command {
    private String action;
    private Object data;

    public AdminBookCommand(String action, Object data) {
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
