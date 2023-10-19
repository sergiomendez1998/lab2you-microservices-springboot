package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.RequestDetailWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.RequestSampleItemWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapperRequest;
import com.example.finalprojectbackend.lab2you.db.repository.RequestDetailRepository;
import com.example.finalprojectbackend.lab2you.db.repository.RequestStatusRepository;
import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import com.example.finalprojectbackend.lab2you.service.*;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.operationTypes.*;

@RestController
@RequestMapping("/api/v1/request")
public class RequestManagementProcessingController {

    private final RequestService requestService;
    private final CustomerService customerService;
    private final RequestDetailRepository requestDetailRepository;
    private ResponseWrapper responseWrapper;

    private final StatusService statusService;
    private final RequestStatusRepository requestStatusRepository;

    private final CurrentUserProvider currentUserProvider;

    public RequestManagementProcessingController(RequestService requestService, CustomerService customerService,
                                                 CurrentUserProvider currentUserProvider,
                                                 RequestDetailRepository requestDetailRepository,
                                                 RequestStatusRepository requestStatusRepository, StatusService statusService) {
        this.requestService = requestService;
        this.customerService = customerService;
        this.currentUserProvider = currentUserProvider;
        this.requestDetailRepository = requestDetailRepository;
        this.requestStatusRepository = requestStatusRepository;
        this.statusService = statusService;
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getAll() {
        return ResponseEntity.ok(requestService.executeReadAll());
    }

    @GetMapping("/samples/{requestId}")
    public ResponseEntity<RequestSampleItemWrapper> getSamplesByRequestId(@PathVariable Long requestId) {
       RequestEntity requestEntity = requestService.getRequestById(requestId);
       RequestSampleItemWrapper requestSampleItemWrapper =requestService.mapToSampleItems(requestEntity);
         return ResponseEntity.ok(requestSampleItemWrapper);
    }

    @GetMapping("/requestStatuses/{requestId}")
    public ResponseEntity<ResponseWrapper> getById(@PathVariable Long requestId) {
        responseWrapper = new ResponseWrapper();
        responseWrapper = requestService.getStatusesByRequestId(requestId);
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/items/{requestId}")
    public ResponseEntity<Map<String, List<RequestDetailWrapper>>> getExamsByRequestId(@PathVariable Long requestId) {
        var
        responseWrapper = requestService.getAllExamItemsByRequestId(requestId);
        return ResponseEntity.ok(responseWrapper);
    }

    @GetMapping("/requestGeneralInfo/{requestId}")
    public ResponseEntity<ResponseWrapperRequest<Map<String,String>>> getRequestGeneralInfo(@PathVariable Long requestId) {
        var request = requestService.getGeneralInformationByRequestId(requestId);
        return ResponseEntity.ok(request);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@RequestBody RequestDTO requestDTO) {

        CustomerEntity customerEntity;
        UserEntity currentUserLogin = currentUserProvider.getCurrentUser();
        String userType;
        userType = getUserTypeFromUserLogin(currentUserLogin);

        if(userType.equalsIgnoreCase(Lab2YouConstants.lab2YouUserTypes.EMPLOYEE.getUserType())){
            customerEntity = new CustomerEntity();
            customerEntity    = customerService.findCustomerByCui(requestDTO.getCustomerCui());
            if (customerEntity == null) {
                return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El cliente no existe", null));
            }
        }else{
            customerEntity = new CustomerEntity();
            customerEntity = customerService.findCustomerByUserId(requestDTO.getUserId());
        }

        RequestEntity requestEntity = requestService.mapToRequestEntity(requestDTO);
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        requestEntity.setRequestCode(Lab2YouUtils.generateRequestCode(date, userType));
        requestEntity.setCustomer(customerEntity);

        responseWrapper = requestService.validate(requestEntity, CREATE.getOperationType());

        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        responseWrapper = requestService.execute(requestEntity, CREATE.getOperationType());
        if (!responseWrapper.isSuccessful()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        RequestEntity newRequest = requestService.findRequestByRequestCode(requestEntity.getRequestCode());

        requestEntity.getRequestDetails().forEach(requestDetailEntity -> {
            requestDetailEntity.setRequest(newRequest);
            requestDetailRepository.save(requestDetailEntity);
        });

        RequestStatusEntity requestStatusEntity = new RequestStatusEntity();
        requestStatusEntity.setRequest(newRequest);
        requestStatusEntity.setStatus(statusService.findStatusByName(Lab2YouConstants.statusTypes.CREATED.getStatusType()));
        requestStatusRepository.save(requestStatusEntity);
        responseWrapper.setMessage("Solicitud No. " + requestEntity.getRequestCode() + " creada exitosamente");
        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping("delete/{requestId}")
    public ResponseEntity<ResponseWrapper> delete(@PathVariable Long requestId) {

        if(requestId == null) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "El id de la solicitud no puede ser nulo", null));
        }

        RequestEntity requestEntity = requestService.getRequestById(requestId);

        String recentStatus = requestEntity.getRequestStatuses()
                .stream()
                .min((o1, o2) -> o2.getCreatedAt().compareTo(o1.getCreatedAt()))
                .map(status -> status.getStatus().getName())
                .orElse(null);

        boolean statusCreated = Lab2YouConstants.statusTypes.CREATED.getStatusType().equalsIgnoreCase(recentStatus);

        if (!statusCreated) {
            return ResponseEntity.badRequest().body(new ResponseWrapper(false, "La solicitud con estado "+recentStatus+" no se puede eliminar", null));
        }

        responseWrapper = requestService.execute(requestEntity, DELETE.getOperationType());
        return ResponseEntity.ok(responseWrapper);
    }

    private String getUserTypeFromUserLogin(UserEntity userAssignedBy) {
        String userType;
        if((userAssignedBy != null ? userAssignedBy.getUserType() : null) !=null){
            if (userAssignedBy.getUserType().equalsIgnoreCase(Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType())) {
                userType = Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType();
            } else {
                userType = Lab2YouConstants.lab2YouUserTypes.EMPLOYEE.getUserType();
            }
        }else{
            userType = Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType();
        }
        return userType;
    }
}
