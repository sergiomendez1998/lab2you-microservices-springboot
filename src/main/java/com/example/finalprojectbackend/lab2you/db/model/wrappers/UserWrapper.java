package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWrapper {
    private  String email;
    private   RoleWrapper role;
    private String password;

    public UserWrapper(String email, RoleWrapper role,String password) {
        this.email = email;
        this.role = role;
        this.password = password;
    }
}
