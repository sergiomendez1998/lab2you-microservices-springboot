package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RequestSampleItemWrapper {
    private Long id;
    private List<SampleWrapper> sampleWrapper;
}
