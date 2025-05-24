/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;



import javax.mail.*; //JavaMail API for handling email content.
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage; //Represents the email message to be sent. (content)
import java.io.*; //handle I/O operations to/from files
import java.nio.file.Files; //handle I/O operations to/from files
import java.nio.file.Paths;//handle file paths


import java.util.Properties;
import javax.mail.*; //provides classes to send, receive, and manage emails via SMTP
import javax.mail.internet.*; //represents an email address. "from" & "to" email addresses
/**
 *
 * @author manul
 */
/*
 * AI-generated suggestions were used for structuring the email handling.
 * Additional modifications and testing were done manually.
 */

public class GmailService {
    public static void sendEmail(String recipient, String subject, String messageText) {
        final String senderEmail = "manuldi2004@gmail.com"; 
        final String senderPassword = "ttsu vnas zukq xesk"; 
        
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true"); //Sets an SMTP property, enabling authentication for sending emails.
        properties.put("mail.smtp.starttls.enable", "true"); //ensures secure communication by upgrading the connection to TLS (Transport Layer Security)
        properties.put("mail.smtp.host", "smtp.gmail.com"); //Specifies the SMTP server (Gmail's SMTP server) to use for sending emails.
        properties.put("mail.smtp.port", "587"); //Specifies the port number (587) for sending emails via SMTP with encryption (STARTTLS).

        Session session = Session.getInstance(properties, new Authenticator() { //handle authentication.
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject(subject);
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