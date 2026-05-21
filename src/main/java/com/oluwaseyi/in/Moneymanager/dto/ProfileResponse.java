package com.oluwaseyi.in.Moneymanager.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ProfileResponse", description = "Response object containing profile details")
public class ProfileResponse {

    @Schema(description = "Profile unique identifier", example = "1")
    private Long id;

    @Schema(description = "Profile name", example = "Jane Doe")
    private String name;

    @Schema(description = "Profile email", example = "jane.doe@example.com")
    private String email;

    public ProfileResponse() {
    }

    public ProfileResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
