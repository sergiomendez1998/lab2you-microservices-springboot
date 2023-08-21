package com.example.finalprojectbackend.lab2you.db.model.dto;

import com.example.finalprojectbackend.lab2you.db.model.wrappers.CustomerWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO extends CustomerWrapper {
    private String password;
}
