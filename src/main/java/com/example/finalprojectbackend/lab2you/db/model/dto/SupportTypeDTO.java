package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SupportTypeDTO {
    private int id;
    private String name;

    public SupportTypeDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
