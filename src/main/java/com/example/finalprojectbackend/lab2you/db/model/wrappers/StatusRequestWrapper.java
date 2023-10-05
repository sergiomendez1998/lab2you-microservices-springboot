package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StatusRequestWrapper {

    private Long id;
    private String statusName;
    private String RequestCode;
    private LocalDate assignedDate;
}
