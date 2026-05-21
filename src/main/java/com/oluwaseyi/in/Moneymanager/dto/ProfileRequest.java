package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(name = "ProfileRequest", description = "Request object for creating or updating a profile")
public class ProfileRequest {

    @Schema(description = "Profile name", example = "Jane Doe")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "Profile email", example = "jane.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    public ProfileRequest() {
    }

    public ProfileRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
