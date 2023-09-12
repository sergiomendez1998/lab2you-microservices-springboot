package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CustomerWrapper {
    private Long cui;
    private String name;
    private String lastName;
    private String address;
    private String nit;
    private String phoneNumber;
    private String gender;
    private String occupation;
    private UserEntity user;
    public CustomerWrapper(UserEntity user, CustomerEntity customerEntity) {
        this.cui = customerEntity.getCui();
        this.name = customerEntity.getFirstName();
        this.lastName = customerEntity.getLastName();
        this.address = customerEntity.getAddress();
        this.phoneNumber = customerEntity.getPhoneNumber();
        this.gender = customerEntity.getGender();
        this.occupation = customerEntity.getOccupation();
        this.user = user;
    }
}
