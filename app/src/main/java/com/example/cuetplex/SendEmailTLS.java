package com.example.cuetplex;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class SendEmailTLS {

    public void send(String sendTo, String data) {

        final String username = "mnsakib355@gmail.com";
        final String password = "SaNave1419";
        System.out.println("mail start");
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("mnsakib355@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(sendTo)
            );
            message.setSubject("Email Verification");
            message.setText(data);

            Transport.send(message);

            System.out.println("mail sent Done to :"+sendTo);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}