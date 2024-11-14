package com.ooops.lms.Command;

import com.ooops.lms.Alter.CustomerAlter;
import com.ooops.lms.database.dao.AccountDAO;
import com.ooops.lms.email.EmailUtil;

import java.sql.SQLException;

public class MailCommand implements Command {
    private String toEmail;
    private String subject;
    private String body;

    public MailCommand(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }

    @Override
    public boolean execute() {
        EmailUtil.sendAsyncEmail(toEmail, subject, body);
        CustomerAlter.showMessage("Đã gửi mail tới: " + toEmail);
       return true;
    }

}
