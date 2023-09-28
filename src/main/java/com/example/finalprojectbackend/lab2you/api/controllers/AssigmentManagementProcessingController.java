package com.example.finalprojectbackend.lab2you.api.controllers;


import com.example.finalprojectbackend.lab2you.db.model.entities.AssignmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RequestEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.AssigmentService;
import com.example.finalprojectbackend.lab2you.service.EmployeeService;
import com.example.finalprojectbackend.lab2you.service.RequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/assigment")
public class AssigmentManagementProcessingController {

    private final AssigmentService assigmentService;
    private final RequestService requestService;

    private final EmployeeService employeeService;

    private final CurrentUserProvider currentUserProvider;

    public AssigmentManagementProcessingController(AssigmentService assigmentService, RequestService requestService, EmployeeService employeeService, CurrentUserProvider currentUserProvider) {
        this.assigmentService = assigmentService;
        this.requestService = requestService;
        this.employeeService = employeeService;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/{roleType}/{requestId}")
    public ResponseEntity<ResponseWrapper> create(@PathVariable String roleType, @PathVariable Long requestId) {
        ResponseWrapper responseWrapper = new ResponseWrapper();
        UserEntity userAssignedBy = new UserEntity();
        AssignmentEntity assignmentEntity = new AssignmentEntity();

        RequestEntity requestEntity = requestService.getRequestById(requestId);
        EmployeeEntity employeeReceiver = getRandomEmployeeByRoleType(roleType);
        userAssignedBy = currentUserProvider.getCurrentUser();
        EmployeeEntity employeeAssignedBy = employeeService.findEmployeeByUserId(userAssignedBy.getId());

        assignmentEntity.setAssignedByEmployee(employeeAssignedBy);
        assignmentEntity.setAssignedToEmployee(employeeReceiver);
        assignmentEntity.setRequest(requestEntity);

        responseWrapper = assigmentService.validate(assignmentEntity, CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        responseWrapper = assigmentService.execute(assignmentEntity, CREATE.getOperationType());
        return ResponseEntity.ok(responseWrapper);
    }

    private EmployeeEntity getRandomEmployeeByRoleType(String roleType) {
        String type = roleType.toLowerCase();
        return switch (type) {
            case "centralizer" -> employeeService.getRandomEmployeeWithRoleCentralizer();
            case "technician" -> employeeService.getRandomEmployeeWithRoleTechnician();
            case "analyst" -> employeeService.getRandomEmployeeWithRoleAnalyst();
            default -> null;
        };
    }
}
