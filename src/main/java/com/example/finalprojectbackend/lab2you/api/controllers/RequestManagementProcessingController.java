package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapperRequest;
import com.example.finalprojectbackend.lab2you.db.repository.RequestDetailRepository;
import com.example.finalprojectbackend.lab2you.db.repository.RequestStatusRepository;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.*;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/request")
public class RequestManagementProcessingController {

    private final RequestService requestService;
    private final CustomerService customerService;
    private final AssigmentService assigmentService;
    private final EmployeeService employeeService;
    private final UserService userService;
    private final RequestDetailRepository requestDetailRepository;
    private ResponseWrapper responseWrapper;

    private final StatusService statusService;
    private final RequestStatusRepository requestStatusRepository;

    private final CurrentUserProvider currentUserProvider;

    public RequestManagementProcessingController(RequestService requestService, CustomerService customerService,
                                                 AssigmentService assigmentService, UserService userService,
                                                 EmployeeService employeeService, CurrentUserProvider currentUserProvider,
                                                 RequestDetailRepository requestDetailRepository,
                                                 RequestStatusRepository requestStatusRepository, StatusService statusService) {
        this.requestService = requestService;
        this.customerService = customerService;
        this.assigmentService = assigmentService;
        this.userService = userService;
        this.employeeService = employeeService;
        this.currentUserProvider = currentUserProvider;
        this.requestDetailRepository = requestDetailRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.statusService = statusService;
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

    @GetMapping("/exams/{requestId}")
    public ResponseEntity<ResponseWrapper> getExamsByRequestId(@PathVariable Long requestId) {
        responseWrapper = new ResponseWrapper();
        responseWrapper = requestService.getAllExamsByRequestId(requestId);
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/requestGeneralInfo/{requestId}")
    public ResponseEntity<ResponseWrapperRequest<Map<String,String>>> getRequestGeneralInfo(@PathVariable Long requestId) {
        var request = requestService.getGeneralInformationByRequestId(requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody RequestDTO requestDTO) {
        AssignmentEntity assignmentEntity = new AssignmentEntity();
        UserEntity userAssignedBy = currentUserProvider.getCurrentUser();
        if (userAssignedBy==null || userAssignedBy.getUserType().equals(Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType())) {
            UserEntity userAssignedBySystem = userService.findByEmail("application@application.com");
            assignmentEntity.setAssignedByEmployee(userAssignedBySystem.getEmployee());
        }

        assignmentEntity.setAssignedToEmployee(employeeService.getRandomEmployeeWithRoleTechnician());
        CustomerEntity customerEntity = customerService.findCustomerByUserId(requestDTO.getUserId());
        RequestEntity requestEntity = requestService.mapToRequestEntity(requestDTO);
        requestEntity.setCustomer(customerEntity);

        responseWrapper = requestService.validate(requestEntity, CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        responseWrapper = requestService.execute(requestEntity, CREATE.getOperationType());
        if (!responseWrapper.isSuccessful()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        RequestEntity requestToAssign = requestService.findRequestByRequestCode(requestEntity.getRequestCode());

        requestEntity.getRequestDetails().forEach(requestDetailEntity -> {
            requestDetailEntity.setRequest(requestToAssign);
            requestDetailRepository.save(requestDetailEntity);
        });

        assignmentEntity.setRequest(requestToAssign);

        RequestStatusEntity requestStatusEntity = new RequestStatusEntity();
        requestStatusEntity.setRequest(requestToAssign);
        requestStatusEntity.setStatus(statusService.findStatusByName(Lab2YouConstants.statusTypes.CREATED.getStatusType()));

        requestStatusRepository.save(requestStatusEntity);
        assigmentService.executeCreation(assignmentEntity);
        responseWrapper.setMessage("Solicitud No. " + requestEntity.getRequestCode() + " creada exitosamente");
        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping("delete/{requestId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long requestId) {

        if(requestId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El id de la solicitud no puede ser nulo", null));
        }

        RequestEntity requestEntity = requestService.getRequestById(requestId);

        responseWrapper = requestService.execute(requestEntity, DELETE.getOperationType());
        return ResponseEntity.ok(responseWrapper);
    }
}
