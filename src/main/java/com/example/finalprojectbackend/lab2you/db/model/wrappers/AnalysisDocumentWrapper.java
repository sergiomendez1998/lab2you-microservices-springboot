package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AnalysisDocumentWrapper {
    private Long id;
    private String documentCode;
    private String customerNit;
    private Date creationDate;
    private String resolution;
    private String documentType;
    private String requestCode;
}
