package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AnalysisDocumentWrapper {
    private Long id;
    private String documentCode;
    private String customerNit;
    private String resolution;
    private String documentType;
    private String requestCode;
    private LocalDateTime createdAt;
}
