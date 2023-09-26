package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerWrapper {

    private Long id;
    private String expedientNumber;
    private String cui;
    private String firstName;
    private String lastName;
    private String address;
    private String nit;
    private String phoneNumber;
    private String gender;
    private String occupation;
    private  UserWrapper user;
}
