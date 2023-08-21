package com.example.finalprojectbackend.lab2you.config.security;

import lombok.Data;

@Data
public class AuthCredentials {
    private String password;
    private String email;
}
