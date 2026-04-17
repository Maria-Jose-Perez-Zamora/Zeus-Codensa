package com.zeuscodensa.techcupfutbol.controller.dto;

import jakarta.validation.constraints.NotBlank;

public class RegistrationStatusUpdateRequestDTO {
    @NotBlank(message = "status is required")
    private String status;

    public RegistrationStatusUpdateRequestDTO() {
    }

    public RegistrationStatusUpdateRequestDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
