package com.example.finalprojectbackend.lab2you.db.model.dto;

import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import lombok.Getter;
import lombok.Setter;
import org.apache.catalina.User;


@Getter
@Setter
public class CustomerDTO{
    private String cui;
    private String firstName;
    private String lastName;
    private String address;
    private String nit;
    private String phoneNumber;
    private String gender;
    private String occupation;
    private UserDTO user;

    public CustomerDTO(String cui, String firstName, String lastName, String address, String nit, String phoneNumber, String gender, String occupation, UserDTO user) {
        this.cui = cui;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.nit = nit;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.occupation = occupation;
        this.user = user;
    }
}
