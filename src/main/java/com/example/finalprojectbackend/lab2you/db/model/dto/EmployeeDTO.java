package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String cui;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;
    private DepartmentDTO department;
    private UserDTO user;

    public EmployeeDTO(String cui, String firstName, String lastName, String address, String phoneNumber, String gender, DepartmentDTO department, UserDTO user) {
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.department = department;
        this.user = user;
    }
}
