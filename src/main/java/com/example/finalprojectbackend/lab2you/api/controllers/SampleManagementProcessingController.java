package com.example.finalprojectbackend.lab2you.api.controllers;


import com.example.finalprojectbackend.lab2you.db.model.dto.SampleDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnitEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.service.SampleService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.MeasureUniteService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.SampleTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleManagementProcessingController {
    private final SampleService sampleService;

    private final SampleTypeService sampleTypeService;
    private final MeasureUniteService measureUniteService;
    private ResponseWrapper responseWrapper;
    public SampleManagementProcessingController(SampleService sampleService, SampleTypeService sampleTypeService, MeasureUniteService measureUniteService) {
        this.sampleService = sampleService;
        this.sampleTypeService = sampleTypeService;
        this.measureUniteService = measureUniteService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> register(@RequestBody SampleDTO sampleDTO){
        SampleTypeEntity sampleTypeEntity = sampleTypeService.findById(sampleDTO.getSampleType().getId());
        MeasureUnitEntity measureUnitEntity = measureUniteService.findById(sampleDTO.getMeasureUnit().getId());
        SampleEntity sampleEntity = sampleService.mapToEntitySample(sampleDTO);
        sampleEntity.setSampleTypeEntity(sampleTypeEntity);
        sampleEntity.setMeasureUnitEntity(measureUnitEntity);

        responseWrapper = sampleService.validate(sampleEntity, CREATE.getOperationType());
    if (!responseWrapper.getErrors().isEmpty()){
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Error validating");
        return ResponseEntity.badRequest().body(responseWrapper);
    }
        responseWrapper = sampleService.execute(sampleEntity, CREATE.getOperationType());
        sampleService.executeCreation(sampleEntity);
        return ResponseEntity.ok(responseWrapper);
    }


}