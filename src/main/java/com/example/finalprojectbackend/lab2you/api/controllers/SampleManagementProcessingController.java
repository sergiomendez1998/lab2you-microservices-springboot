package com.example.finalprojectbackend.lab2you.api.controllers;


import com.example.finalprojectbackend.lab2you.db.model.dto.ItemDTO;
import com.example.finalprojectbackend.lab2you.db.model.dto.SampleDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RequestDetailRepository;
import com.example.finalprojectbackend.lab2you.db.repository.SampleItemRepository;
import com.example.finalprojectbackend.lab2you.service.SampleService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.ItemService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.MeasureUniteService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.SampleTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/sample")
public class SampleManagementProcessingController {
    private final SampleService sampleService;

    private final SampleTypeService sampleTypeService;
    private final MeasureUniteService measureUniteService;
    private final RequestDetailRepository requestDetailRepository;
    private final ItemService itemService;
    private final SampleItemRepository sampleItemRepository;

    private final Map<Long, RequestDetailEntity> requestDetailMap = new HashMap<>();
    private ResponseWrapper responseWrapper;

    public SampleManagementProcessingController(SampleService sampleService, SampleTypeService sampleTypeService,
                                                MeasureUniteService measureUniteService,
                                                RequestDetailRepository requestDetailRepository, SampleItemRepository sampleItemRepository, ItemService itemService) {
        this.sampleService = sampleService;
        this.sampleTypeService = sampleTypeService;
        this.measureUniteService = measureUniteService;
        this.requestDetailRepository = requestDetailRepository;
        this.sampleItemRepository = sampleItemRepository;
        this.itemService = itemService;
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseWrapper> register(@RequestBody List<SampleDTO> sampleDTO) {


        for (SampleDTO sampleDTO1 : sampleDTO) {
            Long requestDetailId = sampleDTO1.getRequestDetailId();
            RequestDetailEntity requestDetailEntity = getRequestDetail(requestDetailId);

            if (requestDetailEntity == null) {
                responseWrapper = new ResponseWrapper();
                responseWrapper.setSuccessful(false);
                responseWrapper.setMessage("Detalle de solicitud no encontrado para ID: " + requestDetailId);
                return ResponseEntity.badRequest().body(responseWrapper);
            }

            SampleTypeEntity sampleTypeEntity = sampleTypeService.findById(sampleDTO1.getSampleType().getId());
            MeasureUnitEntity measureUnitEntity = measureUniteService.findById(sampleDTO1.getMeasureUnit().getId());
            SampleEntity sampleEntity = sampleService.mapToEntitySample(sampleDTO1);
            sampleEntity.setSampleTypeEntity(sampleTypeEntity);
            sampleEntity.setMeasureUnitEntity(measureUnitEntity);
            sampleEntity.setRequestDetail(requestDetailEntity);

            responseWrapper = sampleService.validate(sampleEntity, CREATE.getOperationType());
            if (!responseWrapper.getErrors().isEmpty()) {
                return ResponseEntity.badRequest().body(responseWrapper);
            }

            responseWrapper = sampleService.execute(sampleEntity, CREATE.getOperationType());
            if (!responseWrapper.isSuccessful()) {
                return ResponseEntity.badRequest().body(responseWrapper);
            }
        }

        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping("/associate-item/{sampleId}")
    public ResponseEntity<ResponseWrapper> associateItem(@PathVariable Long sampleId, @RequestBody List<ItemDTO> items) {

        if (sampleId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "el id de la muestra es requerido", null));
        }

        SampleEntity sampleEntity = sampleService.findSampleById(sampleId);



        for (ItemDTO item : items) {
            ItemEntity itemEntity = itemService.findById(item.getId());
            SampleItemEntity sampleItemEntity = new SampleItemEntity();
            sampleItemEntity.setSample(sampleEntity);
            sampleItemEntity.setItem(itemEntity);
            sampleItemRepository.save(sampleItemEntity);
        }

        return ResponseEntity.ok(new ResponseWrapper(true, "Items asociados correctamente", null));
    }

    private RequestDetailEntity getRequestDetail(Long requestDetailId) {

        if (requestDetailMap.containsKey(requestDetailId)) {
            return requestDetailMap.get(requestDetailId);
        } else {
            Optional<RequestDetailEntity> requestDetailOptional = requestDetailRepository.findById(requestDetailId);
            if (requestDetailOptional.isPresent()) {
                RequestDetailEntity requestDetailEntity = requestDetailOptional.get();
                requestDetailMap.put(requestDetailId, requestDetailEntity);
                return requestDetailEntity;
            } else {
                return null;
            }
        }
    }
}