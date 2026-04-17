package com.zeuscodensa.techcupfutbol.core.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import jakarta.mail.internet.MimeMessage;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        emailService = new EmailService(mailSender, "noreply@techcup.com");
    }

    @Test
    void sendWelcomeEmail_ShouldSendWithoutException() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() -> emailService.sendWelcomeEmail("user@escuelaing.edu.co", "Test User"));
        verify(mailSender, times(1)).send(mimeMessage);
    }

    @Test
    void sendWelcomeEmail_WhenMailFails_ShouldNotThrow() {
        when(mailSender.createMimeMessage()).thenThrow(new RuntimeException("SMTP error"));

        // Should swallow exception — registration must not fail because of email
        assertDoesNotThrow(() -> emailService.sendWelcomeEmail("user@escuelaing.edu.co", "Test User"));
    }

    @Test
    void sendPasswordResetEmail_ShouldSendWithoutException() {
        MimeMessage mimeMessage = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        assertDoesNotThrow(() -> emailService.sendPasswordResetEmail("user@escuelaing.edu.co", "Test User", "ABC123"));
        verify(mailSender, times(1)).send(mimeMessage);
    }
}
