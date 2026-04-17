package com.zeuscodensa.techcupfutbol.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;





import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RefereeAssignmentRequestDTO {
    @NotBlank(message = "correoArbitro is required")
    @Email(message = "correoArbitro must be a valid email")
    @JsonProperty("correoArbitro")
    private String refereeEmail;

    public RefereeAssignmentRequestDTO() {
    }

    public RefereeAssignmentRequestDTO(String refereeEmail) {
        this.refereeEmail = refereeEmail;
    }

    public String getRefereeEmail() {
        return refereeEmail;
    }

    public void setRefereeEmail(String refereeEmail) {
        this.refereeEmail = refereeEmail;
    }
}
