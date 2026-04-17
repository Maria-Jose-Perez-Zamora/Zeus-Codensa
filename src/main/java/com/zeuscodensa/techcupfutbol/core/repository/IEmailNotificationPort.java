package com.zeuscodensa.techcupfutbol.core.repository;

public interface IEmailNotificationPort {
    void sendAccountCreationEmail(String email, String name);
    void sendLoginAlertEmail(String email, String name);
}
