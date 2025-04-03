/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;



import javax.mail.*; //JavaMail API for handling email content.
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Base64; //Encodes email content before sending.

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
/**
 *
 * @author manul
 */


public class GmailService {
    public static void sendEmail(String recipient, String code, String messageText) {
        final String senderEmail = "manuldi2004@gmail.com"; 
        final String senderPassword = "ttsu vnas zukq xesk"; 
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(code);
//            message.setText("Your verification code is: " + code);
            message.setText(messageText);

            Transport.send(message);
            System.out.println("Email Sent Successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String recipient = "user@example.com"; // Replace with user email
        String verificationCode = "123456";
        String messageText = "";// Generate a random code
        sendEmail(recipient, verificationCode,messageText);
    }
}