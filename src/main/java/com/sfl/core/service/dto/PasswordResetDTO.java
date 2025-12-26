package com.sfl.core.service.dto;

public class PasswordResetDTO {

    public PasswordResetDTO() {
        // For Jackson.
    }

    public PasswordResetDTO(String email) {
        this.email = email;
    }

    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
