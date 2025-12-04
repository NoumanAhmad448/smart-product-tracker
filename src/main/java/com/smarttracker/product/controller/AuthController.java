package com.smarttracker.product.controller;

import com.smarttracker.product.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


import com.smarttracker.product.dto.*;
import com.smarttracker.product.service.AuthService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    private final AuthService authService;
    
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {
        
        log.info("Registration request received for username: {}", request.getUsername());
        
        UserResponse userResponse = userService.registerUser(request);
        
        ApiResponse<UserResponse> response = ApiResponse.success(
                "User registered successfully", 
                userResponse
        );
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/check-username/{username}")
    public ResponseEntity<ApiResponse<Boolean>> checkUsernameAvailability(
            @PathVariable String username) {
        
        boolean isAvailable = !userService.existsByUsername(username);
        
        ApiResponse<Boolean> response = ApiResponse.success(
                isAvailable ? "Username available" : "Username already taken",
                isAvailable
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/check-email/{email}")
    public ResponseEntity<ApiResponse<Boolean>> checkEmailAvailability(
            @PathVariable String email) {
        
        boolean isAvailable = !userService.existsByEmail(email);
        
        ApiResponse<Boolean> response = ApiResponse.success(
                isAvailable ? "Email available" : "Email already registered",
                isAvailable
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/validate-password")
    public ResponseEntity<ApiResponse<Map<String, Boolean>>> validatePassword(
            @RequestBody Map<String, String> request) {
        
        String password = request.get("password");
        boolean isValid = password != null && password.length() >= 8;
        
        Map<String, Boolean> result = Map.of("isValid", isValid);
        
        ApiResponse<Map<String, Boolean>> response = ApiResponse.success(
                isValid ? "Password is valid" : "Password is too weak",
                result
        );
        
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(
            @Valid @RequestBody LoginRequestDTO request) {
        
        log.info("Login request received for: {}", request.getUsernameOrEmail());
        
        LoginResponseDTO response = authService.authenticate(request);
        
        ApiResponseDTO<LoginResponseDTO> apiResponse = ApiResponseDTO.success(
                "Login successful",
                response
        );
        
        return ResponseEntity.ok(apiResponse);
    }
    
    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO.Tokens>> refreshToken(
            @Valid @RequestBody RefreshTokenRequestDTO request) {
        
        log.info("Refresh token request received");
        
        LoginResponseDTO.Tokens tokens = authService.refreshToken(request.getRefreshToken());
        
        ApiResponseDTO<LoginResponseDTO.Tokens> response = ApiResponseDTO.success(
                "Token refreshed successfully",
                tokens
        );
        
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            authService.logout(username);
            log.info("User logged out: {}", username);
        }
        
        ApiResponseDTO<Void> response = ApiResponseDTO.success(
                "Logout successful",
                null
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/me")
    public ResponseEntity<ApiResponseDTO<UserResponseDTO>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.error(
                    "User not authenticated",
                    "UNAUTHENTICATED"
            );
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        
        String username = authentication.getName();
        log.debug("Fetching current user: {}", username);
        
        // In a real app, you'd fetch from database
        // For now, return basic info
        UserResponseDTO userResponse = UserResponseDTO.builder()
                .username(username)
                .build();
        
        ApiResponseDTO<UserResponseDTO> response = ApiResponseDTO.success(
                "Current user fetched successfully",
                userResponse
        );
        
        return ResponseEntity.ok(response);
    }
}