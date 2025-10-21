package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExamTypeItemWrapper {
    private Long id;
    private String name;
    private String description;
    List<ItemWrapper> items;
}
