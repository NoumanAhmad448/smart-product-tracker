package com.smarttracker.product.service;

import com.smarttracker.product.dto.LoginRequestDTO;
import com.smarttracker.product.dto.LoginResponseDTO;

public interface AuthService {
    
    LoginResponseDTO authenticate(LoginRequestDTO request);
    
    LoginResponseDTO.Tokens refreshToken(String refreshToken);
    
    void logout(String username);
}