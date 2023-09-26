package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AssignmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RequestEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.service.AssigmentService;
import com.example.finalprojectbackend.lab2you.service.CustomerService;
import com.example.finalprojectbackend.lab2you.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.*;
import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/request")
public class RequestManagementProcessingController {

    private final RequestService requestService;
    private final CustomerService customerService;

    private final AssigmentService assigmentService;

    private ResponseWrapper responseWrapper;

    public RequestManagementProcessingController(RequestService requestService, CustomerService customerService, AssigmentService assigmentService) {
        this.requestService = requestService;
        this.customerService = customerService;
        this.assigmentService = assigmentService;
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getAll() {
        return ResponseEntity.ok(requestService.executeReadAll());
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody RequestDTO requestDTO) {
        CustomerEntity customerEntity = customerService.findCustomerById(requestDTO.getCustomerID());
        RequestEntity requestEntity = requestService.mapToRequestEntity(requestDTO);
        requestEntity.setCustomer(customerEntity);

        responseWrapper = requestService.validate(requestEntity, CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        responseWrapper = requestService.execute(requestEntity, CREATE.getOperationType());

        return ResponseEntity.ok(responseWrapper);
    }


}
