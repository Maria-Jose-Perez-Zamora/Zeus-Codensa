package com.zeuscodensa.techcupfutbol.controller.dto;

import java.time.LocalDateTime;

public class ApiErrorDTO {
    private int code;
    private String message;
    private LocalDateTime timestamp;

    public ApiErrorDTO(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}
