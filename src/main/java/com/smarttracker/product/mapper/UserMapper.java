package com.smarttracker.product.mapper;

import com.smarttracker.product.dto.UserResponse;
import com.smarttracker.product.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    
    public UserResponse toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .createdAt(user.getCreatedAt())
                .lastLogin(user.getLastLogin())
                .build();
    }
}