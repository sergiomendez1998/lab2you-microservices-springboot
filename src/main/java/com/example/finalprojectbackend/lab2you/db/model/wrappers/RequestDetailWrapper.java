package com.example.finalprojectbackend.lab2you.db.model.wrappers;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestDetailWrapper {
    Long id;
    Long requestId;
    ExamTypeWrapper examType;
}
