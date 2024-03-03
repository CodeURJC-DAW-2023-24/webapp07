package com.daw.webapp07.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
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
            MimeMessage message = javaMailSender.createMimeMessage();
            message.setFrom(new InternetAddress("seedVentures3@gmail.com"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
            String template = "email-template.html";

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

            String htmlTemplate = readTemplate("src/main/resources/templates/" + template);
            String htmlContent = htmlTemplate.replace("${user}", name);

            message.setContent(htmlContent, "text/html; charset=utf-8");
            message.setSubject(subject);
            javaMailSender.send(message);

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String readTemplate(String path) throws IOException {
        Path filePath = Path.of(path);
        return Files.readString(filePath, StandardCharsets.UTF_8);
    }

}
