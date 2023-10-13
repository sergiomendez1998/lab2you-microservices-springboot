package com.example.finalprojectbackend.lab2you.db.model.dto;


import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {
    Long id;
    private String nickName;
    private String password;
    private String email;
    private String userType;
    private RoleDTO role;
    public UserDTO(String nickName, String password, String email, String userType, RoleDTO role) {
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.role = role;
    }
}
