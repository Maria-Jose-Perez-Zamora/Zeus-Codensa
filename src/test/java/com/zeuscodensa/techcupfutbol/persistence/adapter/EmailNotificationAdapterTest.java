package com.zeuscodensa.techcupfutbol.persistence.adapter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailNotificationAdapterTest {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailNotificationAdapter emailNotificationAdapter;

    @Test
    public void testSendAccountCreationEmail() {
        String testEmail = "newuser@escuelaing.edu.co";
        String testName = "Juan Perez";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailNotificationAdapter.sendAccountCreationEmail(testEmail, testName);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage capturedMessage = captor.getValue();
        assertEquals(testEmail, capturedMessage.getTo()[0]);
        assertTrue(capturedMessage.getSubject().contains(testName));
        assertTrue(capturedMessage.getText().contains("exitosamente"));
    }

    @Test
    public void testSendLoginAlertEmail() {
        String testEmail = "existinguser@escuelaing.edu.co";
        String testName = "Maria Gomez";

        doNothing().when(mailSender).send(any(SimpleMailMessage.class));

        emailNotificationAdapter.sendLoginAlertEmail(testEmail, testName);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(captor.capture());

        SimpleMailMessage capturedMessage = captor.getValue();
        assertEquals(testEmail, capturedMessage.getTo()[0]);
        assertEquals("Nuevo inicio de sesión en TechCup Futbol", capturedMessage.getSubject());
        assertTrue(capturedMessage.getText().contains(testName));
    }
}
