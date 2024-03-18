package com.daw.webapp07.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    public boolean sendEmail(String name, String to, String subject) {

        try {
            // Create a new email
            MimeMessage message = javaMailSender.createMimeMessage();
            // Set the email's sender
            message.setFrom(new InternetAddress("seedVentures3@gmail.com"));
            // Set the email's recipient
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));

            String template = "email-template.html";

            // Set the email's content depending on the subject given
            switch (subject) {
                case "Your project has reached its goal":
                    template = "email-template-goal.html";
                    break;
                case "Welcome to SeedVentures":
                    template = "email-template.html";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + subject);
            }

            // Read the template and replace the user's name
            String htmlTemplate = readTemplate("src/main/resources/templates/" + template);
            String htmlContent = htmlTemplate.replace("${user}", name);

            // Set the email's content, subject and send it
            message.setContent(htmlContent, "text/html; charset=utf-8");
            message.setSubject(subject);
            javaMailSender.send(message);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Read the template from the file system
    public String readTemplate(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

}
