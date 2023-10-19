package com.example.finalprojectbackend.lab2you.api.controllers;


import com.example.finalprojectbackend.lab2you.db.model.dto.ItemDTO;
import com.example.finalprojectbackend.lab2you.db.model.dto.SampleDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RequestDetailRepository;
import com.example.finalprojectbackend.lab2you.db.repository.RequestRepository;
import com.example.finalprojectbackend.lab2you.db.repository.SampleItemRepository;
import com.example.finalprojectbackend.lab2you.service.RequestService;
import com.example.finalprojectbackend.lab2you.service.SampleService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.ItemService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.MeasureUniteService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.SampleTypeService;
import jakarta.websocket.server.PathParam;
import org.apache.coyote.Request;
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

    private final SampleItemRepository sampleItemRepository;
    private final RequestRepository requestRepository;
    private final Map<Long, RequestEntity> requestMap = new HashMap<>();
    private final Map<Long, RequestDetailEntity> requestDetailMap = new HashMap<>();
    private ResponseWrapper responseWrapper;

    public SampleManagementProcessingController(SampleService sampleService, SampleTypeService sampleTypeService,
                                                MeasureUniteService measureUniteService,
                                                RequestDetailRepository requestDetailRepository, SampleItemRepository sampleItemRepository,
                                                RequestRepository requestRepository) {
        this.sampleService = sampleService;
        this.sampleTypeService = sampleTypeService;
        this.measureUniteService = measureUniteService;
        this.requestDetailRepository = requestDetailRepository;
        this.sampleItemRepository = sampleItemRepository;
        this.requestRepository = requestRepository;
    }

    @PostMapping("/create/{requestId}")
    public ResponseEntity<ResponseWrapper> register(@RequestBody List<SampleDTO> sampleDTO, @PathVariable Long requestId) {

        if (requestId == null) {
            responseWrapper = new ResponseWrapper();
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("solicitud No: encontrado para ID: " + requestId);
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        RequestEntity RequestEntity = requestRepository.findById(requestId).orElse(null);

        for (SampleDTO sampleDTO1 : sampleDTO) {
            SampleTypeEntity sampleTypeEntity = sampleTypeService.findById(sampleDTO1.getSampleType().getId());
            MeasureUnitEntity measureUnitEntity = measureUniteService.findById(sampleDTO1.getMeasureUnit().getId());
            SampleEntity sampleEntity = sampleService.mapToEntitySample(sampleDTO1);
            sampleEntity.setSampleTypeEntity(sampleTypeEntity);
            sampleEntity.setMeasureUnitEntity(measureUnitEntity);
            sampleEntity.setRequest(RequestEntity);

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
    public ResponseEntity<ResponseWrapper> associateItem(@PathVariable Long sampleId, @RequestBody List<Long> requestListIds) {

        if (sampleId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "el id de la muestra es requerido", null));
        }

        SampleEntity sampleEntity = sampleService.findSampleById(sampleId);

        for (Long requestDetail : requestListIds) {
            RequestDetailEntity requestDetailEntity = requestDetailRepository.findById(requestDetail).orElse(null);
            if (requestDetailEntity != null){
                SampleItemEntity sampleItemEntity = new SampleItemEntity();
                sampleItemEntity.setSample(sampleEntity);
                sampleItemEntity.setRequestDetail(requestDetailEntity);
                sampleItemRepository.save(sampleItemEntity);
                requestDetailEntity.setIsAssociated(true);
                requestDetailRepository.save(requestDetailEntity);
            }
        }

        return ResponseEntity.ok(new ResponseWrapper(true, "Items asociados correctamente", null));
    }

    @PutMapping("delete/{sampleId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long sampleId) {

        if (sampleId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El id de la muestra no puede ser nulo", null));
        }

        SampleEntity sampleEntity = sampleService.findSampleById(sampleId);

        responseWrapper = sampleService.execute(sampleEntity, DELETE.getOperationType());
        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping("disassociate-item/{sampleId}")
    public ResponseEntity<ResponseWrapper> deleteItem(@PathVariable Long sampleId, @PathParam("itemId") Long itemId) {

        if (sampleId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El id de la muestra es requerido", null));
        }
        if (itemId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El id del item es requerido", null));
        }

        SampleEntity sampleEntity = sampleService.findSampleById(sampleId);


        sampleService.disassociateItem(sampleEntity, itemId);
        return ResponseEntity.ok(new ResponseWrapper(true, "Item desasociado exitosamente", null));
    }
}