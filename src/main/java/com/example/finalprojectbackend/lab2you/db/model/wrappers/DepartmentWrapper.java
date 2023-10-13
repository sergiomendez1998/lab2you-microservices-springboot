package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartmentWrapper {
    private  Long id;
    private String name;

    public DepartmentWrapper(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
