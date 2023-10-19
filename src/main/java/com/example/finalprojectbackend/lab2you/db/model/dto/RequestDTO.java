package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDTO {
    private String supportNumber;
    private String email;
    private String remark;
    private List<ItemDTO> items;
    private SupportTypeDTO supportType;
    private Long  userId;
    private String customerCui;
}
