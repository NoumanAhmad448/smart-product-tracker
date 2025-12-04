package com.smarttracker.product.service.impl;

import com.smarttracker.product.dto.LoginRequestDTO;
import com.smarttracker.product.dto.LoginResponseDTO;
import com.smarttracker.product.dto.UserResponseDTO;
import com.smarttracker.product.exception.AuthenticationFailedException;
import com.smarttracker.product.mapper.UserMapper;
import com.smarttracker.product.model.User;
import com.smarttracker.product.repository.UserRepository;
import com.smarttracker.product.security.JwtUtil;
import com.smarttracker.product.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    
    @Override
    @Transactional
    public LoginResponseDTO authenticate(LoginRequestDTO request) {
        log.info("Authentication attempt for: {}", request.getUsernameOrEmail());
        
        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            // Load user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            
            // Generate tokens
            String accessToken = jwtUtil.generateToken(userDetails);
            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            // Update user's last login
            User user = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new AuthenticationFailedException("User not found after authentication"));
            
            user.setLastLogin(LocalDateTime.now());
            userRepository.save(user);
            
            log.info("User authenticated successfully: {}", user.getUsername());
            
            // Build response
            UserResponseDTO userResponse = userMapper.toDTO(user);
            
            return LoginResponseDTO.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getExpirationTime())
                    .user(userResponse)
                    .build();
                    
        } catch (BadCredentialsException e) {
            log.warn("Invalid credentials for: {}", request.getUsernameOrEmail());
            throw new AuthenticationFailedException("Invalid username or password");
        } catch (Exception e) {
            log.error("Authentication failed for {}: {}", 
                    request.getUsernameOrEmail(), e.getMessage());
            throw new AuthenticationFailedException("Authentication failed: " + e.getMessage());
        }
    }
    
    @Override
    public LoginResponseDTO.Tokens refreshToken(String refreshToken) {
        try {
            // Validate refresh token
            String username = jwtUtil.extractUsername(refreshToken);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (!jwtUtil.validateToken(refreshToken, userDetails)) {
                throw new AuthenticationFailedException("Invalid refresh token");
            }
            
            // Generate new tokens
            String newAccessToken = jwtUtil.generateToken(userDetails);
            String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);
            
            log.info("Token refreshed successfully for user: {}", username);
            
            return LoginResponseDTO.Tokens.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken)
                    .tokenType("Bearer")
                    .expiresIn(jwtUtil.getExpirationTime())
                    .build();
                    
        } catch (Exception e) {
            log.error("Token refresh failed: {}", e.getMessage());
            throw new AuthenticationFailedException("Token refresh failed: " + e.getMessage());
        }
    }
    
    @Override
    public void logout(String username) {
        // In a real application, you might:
        // 1. Add token to blacklist (Redis)
        // 2. Clear user session
        // 3. Update logout timestamp
        
        log.info("User logged out: {}", username);
        SecurityContextHolder.clearContext();
    }
}