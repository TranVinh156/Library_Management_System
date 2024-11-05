package com.ooops.lms.email;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailUtil implements Runnable {
    private String toEmail;
    private String subject;
    private String body;

    public EmailUtil(String toEmail, String subject, String body) {
        this.toEmail = toEmail;
        this.subject = subject;
        this.body = body;
    }



    @Override
    public void run() {
        sendEmail();
    }

    public void sendEmail() {
        String from = "tranvanvinhab2005@gmail.com";
        String host = "smtp.gmail.com";
        String password = "ukqv qagj wpea izvs";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email đã được gửi tới " + toEmail);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public static void sendAsyncEmail(String toEmail, String subject, String body) {
        EmailUtil emailUtil = new EmailUtil(toEmail, subject, body);
        Thread emailThread = new Thread(emailUtil);
        emailThread.start(); // Khởi chạy luồng gửi email
    }
}
