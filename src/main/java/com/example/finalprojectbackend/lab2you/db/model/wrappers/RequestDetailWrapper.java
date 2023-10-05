package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import com.example.finalprojectbackend.lab2you.db.model.dto.ExamTypeDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestDetailWrapper {
    Long id;
    Long requestId;
    ExamTypeWrapper examType;
}
