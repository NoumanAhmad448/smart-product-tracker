package com.smarttracker.product.service;

import com.smarttracker.product.dto.RegisterRequest;
import com.smarttracker.product.dto.UserResponse;

public interface UserService {

    UserResponse registerUser(RegisterRequest request);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}