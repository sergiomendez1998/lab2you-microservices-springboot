package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.EmployeeDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.DepartmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.service.EmployeeService;
import com.example.finalprojectbackend.lab2you.service.UserService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.DepartmentServiceCRUD;
import com.example.finalprojectbackend.lab2you.service.catalogservice.RoleServiceCRUD;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeManagementProcessingController {

    private final EmployeeService employeeService;

    private final UserService userService;

    private final RoleServiceCRUD roleServiceCRUD;

    private final DepartmentServiceCRUD departmentServiceCRUD;

    public EmployeeManagementProcessingController(EmployeeService employeeService, UserService userService, RoleServiceCRUD roleServiceCRUD, DepartmentServiceCRUD departmentServiceCRUD) {
        this.employeeService = employeeService;
        this.userService = userService;
        this.roleServiceCRUD = roleServiceCRUD;
        this.departmentServiceCRUD = departmentServiceCRUD;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody EmployeeDTO employeeDTO){
        UserEntity userEntity = new UserEntity();
        EmployeeEntity employeeEntity = new EmployeeEntity();

        RoleEntity role = roleServiceCRUD.getRoleByName(employeeDTO.getUser().getRole().getName());
        DepartmentEntity department = departmentServiceCRUD.getDepartmentByName(employeeDTO.getDepartment().getName());

        userEntity.setNickName(employeeDTO.getUser().getNickName());
        userEntity.setUserType(Lab2YouConstants.lab2YouUserTypes.EMPLOYEE.getUserType());
        userEntity.setEmail(employeeDTO.getUser().getEmail());
        userEntity.setPassword(Lab2YouUtils.encodePassword(employeeDTO.getUser().getPassword()));
        userEntity.setRole(role);

        employeeEntity.setFirstName(employeeDTO.getFirstName());
        employeeEntity.setLastName(employeeDTO.getLastName());
        employeeEntity.setPhoneNumber(employeeDTO.getPhoneNumber());
        employeeEntity.setAddress(employeeDTO.getAddress());
        employeeEntity.setCui(employeeDTO.getCui());
        employeeEntity.setGender(employeeDTO.getGender());
        employeeEntity.setDepartmentEntity(department);
        employeeEntity.setUser(userEntity);

        userService.save(userEntity);
        employeeService.executeCreation(employeeEntity);
        return ResponseEntity.ok(Lab2YouConstants.lab2YouSuccessCodes.REGISTRATION_SUCCESS.getDescription());
    }
}
