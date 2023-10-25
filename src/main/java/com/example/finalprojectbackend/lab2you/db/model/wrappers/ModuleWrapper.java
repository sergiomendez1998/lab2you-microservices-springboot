package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ModuleWrapper {
    private Long id;
    private String name;
    private String description;
    private String path;
    private String icon;

    public ModuleWrapper(Long id, String name, String description, String path, String icon) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.path = path;
        this.icon = icon;
    }
}
