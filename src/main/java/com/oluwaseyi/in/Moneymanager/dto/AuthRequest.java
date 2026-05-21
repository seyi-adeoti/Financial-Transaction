package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "AuthRequest", description = "Login request payload")
public class AuthRequest {

    @Schema(description = "Username", example = "john.doe")
    @NotBlank(message = "Username is required")
    private String username;

    @Schema(description = "Password", example = "Password123!")
    @NotBlank(message = "Password is required")
    private String password;

    public AuthRequest() {
    }

    public AuthRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
