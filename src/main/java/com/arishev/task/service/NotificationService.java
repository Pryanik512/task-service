package com.arishev.task.service;


import com.arishev.task.properties.EmailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private static final String TO = "some-email@gmail.com";

    private final EmailProperties emailProperties;
    private final JavaMailSender mailSender;

    public void notify(String message) {

        SimpleMailMessage emailMessage = new SimpleMailMessage();

        emailMessage.setFrom(emailProperties.getNoReplay());
        emailMessage.setTo(TO);
        emailMessage.setSubject("Task Status Updated");
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }
}
