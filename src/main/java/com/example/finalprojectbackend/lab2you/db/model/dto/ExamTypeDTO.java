package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ExamTypeDTO {
    private int id;
    private String name;

    public ExamTypeDTO(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
