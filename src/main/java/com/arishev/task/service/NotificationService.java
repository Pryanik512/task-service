package com.arishev.task.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private static final String NO_REPLAY = "no-replay@fake.com";

    private static final String TO = "some-email@gmail.com";

    private final JavaMailSender mailSender;

    public void notify(String message) {

        SimpleMailMessage emailMessage = new SimpleMailMessage();

        emailMessage.setFrom(NO_REPLAY);
        emailMessage.setTo(TO);
        emailMessage.setSubject("Task Status Updated");
        emailMessage.setText(message);

        mailSender.send(emailMessage);
    }
}
