package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
public class SampleDTO {

    private String label;
    private String presentation;
    private Long quantity;
    private SampleTypeDTO sampleType;
    private MeasureUnitDTO measureUnit;
    private Date expirationDate;
}
