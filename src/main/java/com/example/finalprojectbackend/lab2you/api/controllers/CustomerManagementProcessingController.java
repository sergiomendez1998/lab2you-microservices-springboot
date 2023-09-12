package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.CustomerDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.service.CustomerService;
import com.example.finalprojectbackend.lab2you.service.EmailService;
import com.example.finalprojectbackend.lab2you.service.UserService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.RoleServiceCRUD;
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
    private final UserService userService;

    private final RoleServiceCRUD roleServiceCRUD;

    private final EmailService emailService;

    public CustomerManagementProcessingController(CustomerService customerService, UserService userService, RoleServiceCRUD roleServiceCRUD, EmailService emailService) {
        this.customerService = customerService;
        this.userService = userService;
        this.roleServiceCRUD = roleServiceCRUD;
        this.emailService = emailService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody CustomerDTO customerDTO) {
        UserEntity userEntity =  new UserEntity();
        RoleEntity role = roleServiceCRUD.getRoleByName(Lab2YouConstants.lab2YouRoles.USER.getRole());

        userEntity.setNickName(customerDTO.getUser().getNickName());
        userEntity.setEmail(customerDTO.getUser().getEmail());

        if (ObjectUtils.isEmpty(customerDTO.getUser().getPassword()))
        {
            String password = Lab2YouUtils.generateRandomPassword();
            customerDTO.getUser().setPassword(password);
        }
        userEntity.setPassword(Lab2YouUtils.encodePassword(customerDTO.getUser().getPassword()));
        userEntity.setRole(role);

        CustomerEntity customerEntity = new CustomerEntity();

        customerEntity.setFirstName(customerDTO.getFirstName());
        customerEntity.setLastName(customerDTO.getLastName());
        customerEntity.setPhoneNumber(customerDTO.getPhoneNumber());
        customerEntity.setAddress(customerDTO.getAddress());
        customerEntity.setNit(customerDTO.getNit());
        customerEntity.setCui(customerDTO.getCui());
        customerEntity.setGender(customerDTO.getGender());
        customerEntity.setOccupation(customerDTO.getOccupation());
        customerEntity.setUser(userEntity);

        userService.save(userEntity);
        customerService.executeCreation(customerEntity);
        emailService.sendRegistrationEmail(customerDTO);
        return ResponseEntity.ok(Lab2YouConstants.lab2YouSuccessCodes.EMAIL_SENT.getDescription());
    }
}
