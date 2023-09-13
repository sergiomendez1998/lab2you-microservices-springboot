package com.example.finalprojectbackend.lab2you.db.model.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RoleDTO {
    private int id;
    private String name;

    public RoleDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
