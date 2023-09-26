package com.example.finalprojectbackend.lab2you.db.model.dto;

import com.example.finalprojectbackend.lab2you.db.model.wrappers.CustomerWrapper;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDTO {
    private String supportNumber;
    private String email;
    private String remark;
    private ExamTypeDTO examType;
    private SupportTypeDTO supportType;
    private Long  customerID;
}
