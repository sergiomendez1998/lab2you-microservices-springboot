package com.example.finalprojectbackend.lab2you.api.controllers;


import com.example.finalprojectbackend.lab2you.db.model.dto.AssigmentDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AssignmentRepository;
import com.example.finalprojectbackend.lab2you.db.repository.RequestStatusRepository;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.AssigmentService;
import com.example.finalprojectbackend.lab2you.service.EmployeeService;
import com.example.finalprojectbackend.lab2you.service.RequestService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/assigment")
public class AssigmentAndStateChangeManagementProcessingController {
    private final AssigmentService assigmentService;
    private final RequestService requestService;

    private final AssignmentRepository assignmentRepository;
    private final EmployeeService employeeService;

    private final CurrentUserProvider currentUserProvider;

    private final RequestStatusRepository requestStatusRepository;
    private final StatusService statusService;


    public AssigmentAndStateChangeManagementProcessingController(AssigmentService assigmentService,
                                                                 RequestService requestService,
                                                                 EmployeeService employeeService,
                                                                 RequestStatusRepository requestStatusRepository,
                                                                 StatusService statusService,
                                                                 CurrentUserProvider currentUserProvider, AssignmentRepository assignmentRepository) {
        this.assigmentService = assigmentService;
        this.requestService = requestService;
        this.employeeService = employeeService;
        this.currentUserProvider = currentUserProvider;
        this.requestStatusRepository = requestStatusRepository;
        this.statusService = statusService;
        this.assignmentRepository = assignmentRepository;
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody AssigmentDTO assigmentDTO) {

        ResponseWrapper responseWrapper = new ResponseWrapper();
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        RequestStatusEntity requestStatusEntity = new RequestStatusEntity();

        StatusEntity statusEntity = statusService.findStatusById(assigmentDTO.getStatusId());
        RequestEntity requestEntity = requestService.getRequestById(assigmentDTO.getRequestId());
        EmployeeEntity employeeReceiver = getRandomEmployeeByRoleType(assigmentDTO.getTypeRoleToAssign());

        UserEntity userAssignedBy = currentUserProvider.getCurrentUser();

        EmployeeEntity employeeAssignedBy = employeeService.findEmployeeByUserId(userAssignedBy.getId());

        if(requestEntity.getSamples().isEmpty()){
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Debe de haber al menos una muestra asociada a la solicitud para cambiar su estado.");
            return ResponseEntity.ok(responseWrapper);
        }
        int counterSamples = 0;
        int counterIsDeleted = 0;

        for (SampleEntity sampleEntity : requestEntity.getSamples()) {
                counterSamples++;
            if (sampleEntity.isDeleted()) {
                counterIsDeleted++;
            }
        }

        if (counterSamples == counterIsDeleted) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Debe de haber al menos una muestra asociada a la solicitud para cambiar su estado.");
            return ResponseEntity.ok(responseWrapper);
        }

        requestStatusEntity.setRequest(requestEntity);
        requestStatusEntity.setStatus(statusEntity);
        AssignmentEntity currentAssignment = assignmentRepository.findCurrentAssignmentForRequest(requestEntity.getId());

        if(currentAssignment != null){
            currentAssignment.setCurrentAssignment(false);
            assignmentRepository.save(currentAssignment);
        }

        assignmentEntity.setAssignedByEmployee(employeeAssignedBy);
        assignmentEntity.setAssignedToEmployee(employeeReceiver);
        assignmentEntity.setRequest(requestEntity);

        responseWrapper = assigmentService.validate(assignmentEntity, CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        requestStatusRepository.save(requestStatusEntity);
        responseWrapper = assigmentService.execute(assignmentEntity, CREATE.getOperationType());
        responseWrapper.setMessage("Estado cambiado a: " + statusEntity.getName() + " y asignado a: " + employeeReceiver.getFirstName() + " " + employeeReceiver.getLastName());
        return ResponseEntity.ok(responseWrapper);
    }

    private EmployeeEntity getRandomEmployeeByRoleType(String roleType) {
        return switch (roleType) {
            case "Centralizador" -> employeeService.getRandomEmployeeWithRoleCentralizer();
            case "Tecnico" -> employeeService.getRandomEmployeeWithRoleTechnician();
            case "Analista" -> employeeService.getRandomEmployeeWithRoleAnalyst();
            default -> null;
        };
    }
}
