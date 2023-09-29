package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SampleWrapper {

    private String label;
    private String presentation;
    private Long quantity;
    private SampleTypeWrapper sampleType;
    private MeasureUnitWrapper measureUnit;
    private RequestWrapper request;
    private Date expirationDate;
    }
