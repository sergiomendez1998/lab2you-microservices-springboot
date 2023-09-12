package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
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
    private List<RoleEntity> roleEntities;

    public CustomerWrapper(UserEntity userEntity, List<RoleEntity> roleEntities, CustomerEntity customerEntity) {
        this.cui = customerEntity.getCui();
        this.name = customerEntity.getName();
        this.lastName = customerEntity.getLastName();
        this.address = customerEntity.getAddress();
        this.email = userEntity.getEmail();
        this.phoneNumber = customerEntity.getPhoneNumber();
        this.gender = customerEntity.getGender();
        this.occupation = customerEntity.getOccupation();
        this.roleEntities = roleEntities;
    }
}
