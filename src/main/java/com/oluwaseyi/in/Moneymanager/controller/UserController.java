package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.dto.UserResponse;
import com.oluwaseyi.in.Moneymanager.interfaces.UserService;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "API endpoints for managing users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(
        summary = "Get all users with pagination",
        description = "Retrieves all users with pagination support. Use page and size parameters to control pagination."
    )
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @PageableDefault(size = 10, page = 0) Pageable pageable) {
        logger.info("Fetching all users with pagination - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        
        Page<UserResponse> usersPage = userService.getAllUsers(pageable)
                .map(UserResponse::fromEntity);
        
        var response = new ApiResponse<>(
            HttpStatus.OK.value(),
            "Users retrieved successfully",
            usersPage
        );
        return ResponseEntity.ok(response);
    }
}
