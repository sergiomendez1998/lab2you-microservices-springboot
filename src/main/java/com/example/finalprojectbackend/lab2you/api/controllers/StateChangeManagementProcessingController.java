package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.db.model.dto.RequestStatusDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.RequestEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RequestStatusEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.StatusEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RequestStatusRepository;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.RequestService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/v1/stateChange")
public class StateChangeManagementProcessingController {

    private final RequestStatusRepository requestStatusRepository;
    private final StatusService statusService;

    private final RequestService requestService;

    private final CurrentUserProvider currentUserProvider;

    public StateChangeManagementProcessingController(RequestStatusRepository requestStatusRepository,
                                                     StatusService statusService, RequestService requestService, CurrentUserProvider currentUserProvider) {
        this.requestStatusRepository = requestStatusRepository;
        this.statusService = statusService;
        this.requestService = requestService;
        this.currentUserProvider = currentUserProvider;
    }
    @PostMapping
    public ResponseEntity<ResponseWrapper> changeState(@RequestBody RequestStatusDTO requestStatus) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        StatusEntity statusEntity = statusService.findStatusById(requestStatus.getStatusId());
        RequestEntity requestEntity = requestService.getRequestById(requestStatus.getRequestId());
        UserEntity userLogin = currentUserProvider.getCurrentUser();
        if(requestEntity.getSamples().isEmpty()){
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Debe de haber al menos una muestra asociada a la solicitud para cambiar su estado.");
            return ResponseEntity.ok(responseWrapper);
        }

        if (statusEntity != null && requestEntity != null) {
            RequestStatusEntity requestStatusEntity = new RequestStatusEntity();
            requestStatusEntity.setCreatedBy(userLogin.getId());
            requestStatusEntity.setRequest(requestEntity);
            requestStatusEntity.setStatus(statusEntity);
            requestStatusRepository.save(requestStatusEntity);

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Estado cambiado");
            responseWrapper.setData(Collections.singletonList("El estado de la solicitud ha sido cambiado"));
           return ResponseEntity.ok(responseWrapper);
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Estado no cambiado");
        return ResponseEntity.ok(responseWrapper);
    }
}
