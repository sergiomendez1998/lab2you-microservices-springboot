package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/request")
public class RequestManagementProcessingController {

    private final RequestService requestService;
    private final CustomerService customerService;
    private final AssigmentService assigmentService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private ResponseWrapper responseWrapper;

    private final CurrentUserProvider currentUserProvider;

    public RequestManagementProcessingController(RequestService requestService, CustomerService customerService, AssigmentService assigmentService, UserService userService, EmployeeService employeeService, CurrentUserProvider currentUserProvider) {
        this.requestService = requestService;
        this.customerService = customerService;
        this.assigmentService = assigmentService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getAll() {
        return ResponseEntity.ok(requestService.executeReadAll());
    }

    @GetMapping("/requestStatuses/{requestId}")
    public ResponseEntity<ResponseWrapper> getById(@PathVariable Long requestId) {
        responseWrapper = new ResponseWrapper();
        responseWrapper = requestService.getStatusesByRequestId(requestId);
        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody RequestDTO requestDTO) {
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        UserEntity userAssignedBy = currentUserProvider.getCurrentUser();

        if (userAssignedBy==null || userAssignedBy.getUserType().equals(Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType())) {
            UserEntity userAssignedBySystem = userService.findByEmail("system@system.com");
            assignmentEntity.setAssignedByEmployee(userAssignedBySystem.getEmployee());
        }

        assignmentEntity.setAssignedByEmployee(employeeService.getRandomEmployeeWithRoleTechnician());
        CustomerEntity customerEntity = customerService.findCustomerByUserId(requestDTO.getUserId());
        RequestEntity requestEntity = requestService.mapToRequestEntity(requestDTO);
        requestEntity.setCustomer(customerEntity);

        responseWrapper = requestService.validate(requestEntity, CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        responseWrapper = requestService.execute(requestEntity, CREATE.getOperationType());
        assigmentService.executeCreation(assignmentEntity);
        return ResponseEntity.ok(responseWrapper);
    }

}
