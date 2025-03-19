/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Code;

import com.google.api.client.auth.oauth2.Credential; //Handles OAuth2 authentication.
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp; 
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow; // Manages OAuth2 login process.
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory; 
import com.google.api.services.gmail.Gmail; //The main API class for sending emails.
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.Message;


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

/**
 *
 * @author manul
 */
public class GmailService { //Gmail API provides a secure way to integrate email functionality with Google's authentication and authorization system.
    private static final String APPLICATION_NAME = "Memory Matching Game";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\tokens"; //Stores OAuth2 tokens so that users don’t have to log in every time.

    private static final List<String> SCOPES = Collections.singletonList(GmailScopes.GMAIL_SEND); //Defines what the API is allowed to send emails
    private static final String CREDENTIALS_FILE_PATH = "C:\\Users\\manul\\OneDrive\\Desktop\\Y3S1\\DSA\\Assignment\\GameDemo\\src\\main\\java\\Code\\credentials.json"; // for authentication.

    public static Credential getCredentials(final HttpTransport HTTP_TRANSPORT) throws IOException //Handling OAuth2 Authentication //This method authenticates the user with Google and retrieves an OAuth2 access token to send emails. 
    {
        InputStream in = new FileInputStream(CREDENTIALS_FILE_PATH); //loads credential.json containing OAuth2 client ID and secret
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    public static Gmail getGmailService() throws IOException, GeneralSecurityException { //It initializes the Gmail API service so we can send emails.
        HttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = getCredentials(HTTP_TRANSPORT);
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void sendEmail(String recipient, String subject, String body) throws Exception {
        Gmail service = getGmailService();

        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        email.setFrom(new InternetAddress("your-email@gmail.com")); //The sender's email.
        email.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(recipient)); //The recipient's email.
        email.setSubject(subject); //The email subject.
        email.setText(body); //The email body.

        ByteArrayOutputStream buffer = new ByteArrayOutputStream(); //It constructs, encodes, and sends an email via Gmail API
        email.writeTo(buffer);
        byte[] rawMessageBytes = buffer.toByteArray();
        String encodedEmail = Base64.getUrlEncoder().encodeToString(rawMessageBytes);
        Message message = new Message();
        message.setRaw(encodedEmail);

        message = service.users().messages().send("me", message).execute();
        System.out.println("Message sent: " + message.getId());
    }
}
