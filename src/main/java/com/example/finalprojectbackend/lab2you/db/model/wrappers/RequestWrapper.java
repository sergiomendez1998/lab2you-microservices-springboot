package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class RequestWrapper {
    private Long id;
    private String requestCode;
    private String customerFirstName;
    private String customerLastName;
    private String customerExpedientNumber;
    private String customerNit;
    private String supportNumber;
    private String status;
    private Date creationDate;
}
