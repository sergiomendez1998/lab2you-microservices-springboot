package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class EmployeeWrapper {
    private Long id;
    private String cui;
    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String gender;
    private DepartmentWrapper department;
    private UserWrapper user;
}
