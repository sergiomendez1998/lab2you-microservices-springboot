package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.CustomerDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.service.CustomerService;
import com.example.finalprojectbackend.lab2you.service.EmailService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.RoleProcessingControllerServiceCrud;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customer")
public class CustomerManagementProcessingController {

    private final CustomerService customerService;

    private final RoleProcessingControllerServiceCrud roleServiceCRUD;

    private final EmailService emailService;

    private ResponseWrapper responseWrapper;

    public CustomerManagementProcessingController(CustomerService customerService, RoleProcessingControllerServiceCrud roleServiceCRUD, EmailService emailService) {
        this.customerService = customerService;
        this.roleServiceCRUD = roleServiceCRUD;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseWrapper> register(@RequestBody CustomerDTO customerDTO) {
        UserEntity userEntity = new UserEntity();
        RoleEntity role = roleServiceCRUD.getRoleByName(Lab2YouConstants.lab2YouRoles.USER.getRole());

        userEntity.setNickName(customerDTO.getUser().getNickName());
        userEntity.setEmail(customerDTO.getUser().getEmail());
        userEntity.setUserType(Lab2YouConstants.lab2YouUserTypes.CUSTOMER.getUserType());
        userEntity.setEnabled(true);

        if (ObjectUtils.isEmpty(customerDTO.getUser().getPassword())) {
            String password = Lab2YouUtils.generateRandomPassword();
            customerDTO.getUser().setPassword(password);
        }
        userEntity.setPassword(Lab2YouUtils.encodePassword(customerDTO.getUser().getPassword()));
        userEntity.setRole(role);

        CustomerEntity customerEntity = customerService.mapToEntityCustomer(customerDTO);
        customerEntity.setUser(userEntity);

        responseWrapper = customerService.validate(customerEntity, Lab2YouConstants.operationTypes.CREATE.getOperationType());

        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        customerService.execute(customerEntity, Lab2YouConstants.operationTypes.CREATE.getOperationType());
        emailService.sendRegistrationEmail(customerDTO);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage(Lab2YouConstants.lab2YouSuccessCodes.EMAIL_SENT.getDescription());
        return ResponseEntity.ok(responseWrapper);
    }
}
