package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssigmentDTO {
    private Long requestId;

    private Long statusId;
    private String typeRoleToAssign;

}
