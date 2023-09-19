package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWrapper {
    private  String email;
    private   RoleWrapper role;

    public UserWrapper(String email, RoleWrapper role) {
        this.email = email;
        this.role = role;
    }
}
