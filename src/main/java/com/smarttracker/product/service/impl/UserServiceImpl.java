package com.smarttracker.product.service.impl;

import com.smarttracker.product.dto.RegisterRequest;
import com.smarttracker.product.dto.UserResponse;
import com.smarttracker.product.exception.DuplicateResourceException;
import com.smarttracker.product.mapper.UserMapper;
import com.smarttracker.product.model.User;
import com.smarttracker.product.repository.UserRepository;
import com.smarttracker.product.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    
    @Override
    @Transactional
    public UserResponse registerUser(RegisterRequest request) {
        log.info("Attempting to register user: {}", request.getUsername());
        
        // Check if username already exists (case-insensitive)
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername())) {
            throw new DuplicateResourceException(
                "USERNAME_EXISTS", 
                String.format("Username '%s' is already taken", request.getUsername())
            );
        }
        
        // Check if email already exists (case-insensitive)
        if (userRepository.existsByEmailIgnoreCase(request.getEmail())) {
            throw new DuplicateResourceException(
                "EMAIL_EXISTS",
                String.format("Email '%s' is already registered", request.getEmail())
            );
        }
        
        // Validate password strength
        validatePasswordStrength(request.getPassword());
        
        // Create user entity
        User user = User.builder()
                .username(request.getUsername().toLowerCase())
                .email(request.getEmail().toLowerCase())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .build();
        
        // Save user
        User savedUser = userRepository.save(user);
        log.info("User registered successfully with ID: {}", savedUser.getId());
        
        return userMapper.toDTO(savedUser);
    }
    
    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }
    
    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }
    
    private void validatePasswordStrength(String password) {
        // Additional password validation if needed
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
        
        // Check for common weak passwords
        if (isWeakPassword(password)) {
            throw new IllegalArgumentException("Password is too weak. Please choose a stronger password");
        }
    }
    
    private boolean isWeakPassword(String password) {
        // Add common weak passwords check
        String[] weakPasswords = {
            "password", "12345678", "qwerty123", "admin123", "letmein"
        };
        
        for (String weak : weakPasswords) {
            if (password.equalsIgnoreCase(weak)) {
                return true;
            }
        }
        
        return false;
    }
}