package com.zeuscodensa.techcupfutbol.persistence.adapter;

import com.zeuscodensa.techcupfutbol.core.repository.IEmailNotificationPort;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class EmailNotificationAdapter implements IEmailNotificationPort {

    private static final Logger log = LoggerFactory.getLogger(EmailNotificationAdapter.class);

    private final JavaMailSender mailSender;
    private final String fromEmail = "noreply@zeus-codensa.com";

    public EmailNotificationAdapter(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    @Override
    public void sendAccountCreationEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("¡Bienvenido a TechCup Futbol, " + name + "!");
            message.setText("Tu cuenta ha sido creada exitosamente. Prepárate para la acción y la mejor gestión de torneos deportivos.");
            mailSender.send(message);
            log.info("Email de bienvenida enviado a {}", email);
        } catch (Exception e) {
            log.error("Fallo enviando email de bienvenida a {}: {}", email, e.getMessage());
        }
    }

    @Async
    @Override
    public void sendLoginAlertEmail(String email, String name) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(email);
            message.setSubject("Nuevo inicio de sesión en TechCup Futbol");
            message.setText("Hola " + name + ", hemos detectado un inicio de sesión reciente en tu cuenta.");
            mailSender.send(message);
            log.info("Email de alerta de login enviado a {}", email);
        } catch (Exception e) {
            log.error("Fallo enviando email de alerta a {}: {}", email, e.getMessage());
        }
    }
}
