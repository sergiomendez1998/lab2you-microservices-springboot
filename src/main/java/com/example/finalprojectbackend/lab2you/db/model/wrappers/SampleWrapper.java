package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SampleWrapper {

    private Long id;
    private String label;
    private String presentation;
    private Long quantity;
    private SampleTypeWrapper sampleType;
    private MeasureUnitWrapper measureUnit;
    private Date expirationDate;
    private List<ItemWrapper> items;
    }
