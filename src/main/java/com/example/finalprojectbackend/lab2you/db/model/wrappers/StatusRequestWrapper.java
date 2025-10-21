package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@Getter
@Setter
public class StatusRequestWrapper {

    private Long id;
    private String statusName;
    private String RequestCode;
    private LocalDateTime assignedDate;
}
