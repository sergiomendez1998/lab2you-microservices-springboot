package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthWrapper {
    private String token;
    private Long userId;
    private String role;
    private String name;
    private String email;

    public AuthWrapper(Long userId,String token, String role, String name, String email) {
        this.userId = userId;
        this.token = token;
        this.role = role;
        this.name = name;
        this.email = email;
    }
}
