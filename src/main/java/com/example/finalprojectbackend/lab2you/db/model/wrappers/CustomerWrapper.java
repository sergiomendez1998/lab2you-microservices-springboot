package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import com.example.finalprojectbackend.lab2you.db.model.entities.Customer;
import com.example.finalprojectbackend.lab2you.db.model.entities.Role;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerWrapper {
    private Long cui;
    private String name;
    private String lastName;
    private String address;
    private String email;

    private String nit;
    private String phoneNumber;
    private String gender;

    private String occupation;
    private List<Role> roles;

    public CustomerWrapper(UserEntity userEntity, List<Role> roles, Customer customer) {
        this.cui = customer.getCui();
        this.name = customer.getName();
        this.lastName = customer.getLastName();
        this.address = customer.getAddress();
        this.email = userEntity.getEmail();
        this.phoneNumber = customer.getPhoneNumber();
        this.gender = customer.getGender();
        this.occupation = customer.getOccupation();
        this.roles = roles;
    }
}
