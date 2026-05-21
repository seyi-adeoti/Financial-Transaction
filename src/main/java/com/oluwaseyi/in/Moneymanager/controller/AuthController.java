package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.dto.AuthRequest;
import com.oluwaseyi.in.Moneymanager.dto.AuthResponse;
import com.oluwaseyi.in.Moneymanager.dto.ForgotPasswordRequest;
import com.oluwaseyi.in.Moneymanager.dto.ResetPasswordRequest;
import com.oluwaseyi.in.Moneymanager.dto.Role;
import com.oluwaseyi.in.Moneymanager.dto.SignupRequest;
import com.oluwaseyi.in.Moneymanager.entity.User;
import com.oluwaseyi.in.Moneymanager.interfaces.UserService;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import com.oluwaseyi.in.Moneymanager.service.JwtService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<String>> signup(@Valid @RequestBody SignupRequest request) {
        logger.info("Signing up user: {}", request.getUsername());
        if (userService.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), "Username already exists"));
        }
        if (userService.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new ApiResponse<>(HttpStatus.CONFLICT.value(), "Email already exists"));
        }

        User user = new User(request.getUsername(), request.getEmail(), request.getPassword(), Set.of(Role.ROLE_USER));
        userService.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(HttpStatus.CREATED.value(), "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody AuthRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
            return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Login successful", new AuthResponse(token)));
        } catch (AuthenticationException ex) {
            logger.warn("Authentication failed for user: {}", request.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse<>(HttpStatus.UNAUTHORIZED.value(), "Invalid username or password"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return userService.findByEmail(request.getEmail())
                .map(user -> {
                    String token = userService.createPasswordResetToken(user);
                    logger.info("Generated password reset token for user: {}", user.getUsername());
                    return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Password reset token generated", token));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<String>(HttpStatus.NOT_FOUND.value(), "Email address not found", null)));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<String>> resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        return userService.findByResetToken(request.getToken())
                .map(user -> {
                    if (user.getResetTokenExpiration() == null || user.getResetTokenExpiration() < System.currentTimeMillis()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body(new ApiResponse<String>(HttpStatus.BAD_REQUEST.value(), "Reset token has expired", null));
                    }
                    userService.updatePassword(user, request.getNewPassword());
                    return ResponseEntity.ok(new ApiResponse<String>(HttpStatus.OK.value(), "Password reset successfully", null));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<String>(HttpStatus.NOT_FOUND.value(), "Invalid reset token", null)));
    }
}
