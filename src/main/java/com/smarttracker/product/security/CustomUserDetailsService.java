package com.smarttracker.product.security;

import com.smarttracker.product.model.User;
import com.smarttracker.product.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        log.debug("Attempting to load user: {}", usernameOrEmail);
        
        // Try to find by username or email
        User user = userRepository.findByUsername(usernameOrEmail)
                .or(() -> userRepository.findByEmail(usernameOrEmail))
                .orElseThrow(() -> {
                    log.warn("User not found with username/email: {}", usernameOrEmail);
                    return new UsernameNotFoundException(
                            "User not found with username or email: " + usernameOrEmail);
                });
        
        if (!user.isEnabled()) {
            log.warn("User account is disabled: {}", usernameOrEmail);
            throw new UsernameNotFoundException("User account is disabled");
        }
        
        if (!user.isAccountNonLocked()) {
            log.warn("User account is locked: {}", usernameOrEmail);
            throw new UsernameNotFoundException("User account is locked");
        }
        
        log.debug("User loaded successfully: {}", usernameOrEmail);
        return user; // User implements UserDetails
    }
}