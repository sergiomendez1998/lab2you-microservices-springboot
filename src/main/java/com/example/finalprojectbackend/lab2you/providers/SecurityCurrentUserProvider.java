package com.example.finalprojectbackend.lab2you.providers;

import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityCurrentUserProvider implements CurrentUserProvider {
    private final UserService userService;


    public SecurityCurrentUserProvider(UserService userService) {
        this.userService = userService;
    }
    @Override
    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String userName = authentication.getName();
            if (userName.contains("@")) {
                return userService.findByEmail(userName);
            }
        }
        return userService.findByEmail("system@system.com");
    }
}
