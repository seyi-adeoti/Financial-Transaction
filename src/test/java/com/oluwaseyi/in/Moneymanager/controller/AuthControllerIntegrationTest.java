package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void signup_returnsCreatedAndSavesUser() throws Exception {
        String requestBody = "{\"username\":\"alice\",\"email\":\"alice@example.com\",\"password\":\"Password123!\"}";

        mockMvc.perform(post("/api/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(201))
                .andExpect(jsonPath("$.message").value("User registered successfully"));

        assertThat(userRepository.findByUsername("alice")).isPresent();
    }

    @Test
    void login_returnsJwtTokenForValidCredentials() throws Exception {
        userRepository.save(new com.oluwaseyi.in.Moneymanager.entity.User("loginuser", "login@example.com", passwordEncoder.encode("LoginPass123!"), java.util.Set.of(com.oluwaseyi.in.Moneymanager.dto.Role.ROLE_USER)));

        String requestBody = "{\"username\":\"loginuser\",\"password\":\"LoginPass123!\"}";

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.token").isNotEmpty());
    }

    @Test
    void forgotPassword_generatesTokenForExistingEmail() throws Exception {
        userRepository.save(new com.oluwaseyi.in.Moneymanager.entity.User("resetuser", "reset@example.com", passwordEncoder.encode("ResetPass123!"), java.util.Set.of(com.oluwaseyi.in.Moneymanager.dto.Role.ROLE_USER)));

        String requestBody = "{\"email\":\"reset@example.com\"}";

        mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").isNotEmpty());
    }
}
