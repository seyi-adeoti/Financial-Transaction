package com.oluwaseyi.in.Moneymanager.controller;

import com.oluwaseyi.in.Moneymanager.dto.ProfileRequest;
import com.oluwaseyi.in.Moneymanager.dto.ProfileResponse;
import com.oluwaseyi.in.Moneymanager.exception.ResourceNotFoundException;
import com.oluwaseyi.in.Moneymanager.interfaces.ProfileService;
import com.oluwaseyi.in.Moneymanager.mapper.ProfileMapper;
import com.oluwaseyi.in.Moneymanager.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/profiles")
@Tag(name = "Profiles", description = "API endpoints for managing user profiles")
public class ProfileController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private final ProfileService profileService;
    private final ProfileMapper profileMapper;

    public ProfileController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping
    @Operation(summary = "Create a new profile", description = "Creates a new profile that can be linked to transactions and expenses")
    public ResponseEntity<ApiResponse<ProfileResponse>> createProfile(@Valid @RequestBody ProfileRequest request) {
        logger.info("Creating profile with name: {}", request.getName());
        var profile = profileMapper.toEntity(request);
        var created = profileService.create(profile);
        var response = new ApiResponse<>(HttpStatus.CREATED.value(), "Profile created successfully", profileMapper.toResponse(created));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Get profiles", description = "Retrieves all profiles")
    public ResponseEntity<ApiResponse<List<ProfileResponse>>> getAllProfiles() {
        logger.info("Fetching all profiles");
        var profiles = profileService.findAll().stream()
                .map(profileMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profiles retrieved successfully", profiles));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get profile by ID", description = "Retrieves a profile by its ID")
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfileById(@PathVariable Long id) {
        logger.info("Fetching profile with id: {}", id);
        var profile = profileService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found with id: " + id));
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK.value(), "Profile retrieved successfully", profileMapper.toResponse(profile)));
    }
}
