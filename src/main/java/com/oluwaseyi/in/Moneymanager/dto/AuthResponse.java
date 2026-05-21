package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "AuthResponse", description = "Authentication response containing JWT token")
public class AuthResponse {

    @Schema(description = "JWT access token")
    private String token;

    public AuthResponse() {
    }

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
