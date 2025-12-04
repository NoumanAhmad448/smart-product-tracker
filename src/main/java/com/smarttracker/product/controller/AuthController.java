package com.smarttracker.product.controller;

import com.smarttracker.product.dto.ApiResponse;
import com.smarttracker.product.dto.RegisterRequest;
import com.smarttracker.product.dto.UserResponse;
import com.smarttracker.product.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    
    private final UserService userService;
    
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
}