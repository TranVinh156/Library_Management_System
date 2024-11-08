package com.ooops.lms.Command;

public class CommandInvoker {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public boolean executeCommand() {
        if (command != null) {
            return command.execute();
        }
        return false;
    }
}
