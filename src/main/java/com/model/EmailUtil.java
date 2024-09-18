package com.model;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class EmailUtil {

    // Method to send email
    public static void sendEmail(String recipient, String subject, String messageContent) throws MessagingException {
        // SMTP server information
        String host = "smtp.gmail.com";
        String port = "587";
        String user = "madusha.dev001@gmail.com";  // Your Gmail address
        String pass = "ahdo zjps bnma qraj";  // Your Gmail app password (generated for SMTP)

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true"); // Enable TLS encryption
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // Create a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        };

        Session session = Session.getInstance(properties, auth);

        // Create email message
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(user));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(messageContent);

        // Send the email
        Transport.send(message);
    }
}
