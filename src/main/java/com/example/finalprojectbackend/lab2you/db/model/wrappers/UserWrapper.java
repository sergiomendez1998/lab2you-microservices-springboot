package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserWrapper {
    private Long id;
    private  String email;
    private   RoleWrapper role;

    public UserWrapper(Long id ,String email, RoleWrapper role) {
        this.email = email;
        this.role = role;
        this.id = id;
    }
}
