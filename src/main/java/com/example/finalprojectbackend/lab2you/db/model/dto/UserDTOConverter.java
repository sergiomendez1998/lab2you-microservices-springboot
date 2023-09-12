package com.example.finalprojectbackend.lab2you.db.model.dto;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;

public class UserDTOConverter {

    public CustomerEntity convertToEntity(UserDTO userDTO) {
        CustomerEntity newCustomerEntity = new CustomerEntity();
        UserEntity newUser = new UserEntity();
        newCustomerEntity.setCui(userDTO.getCui());
        newCustomerEntity.setName(userDTO.getName());
        newCustomerEntity.setLastName(userDTO.getLastName());
        newCustomerEntity.setAddress(userDTO.getAddress());
        newCustomerEntity.setPhoneNumber(userDTO.getPhoneNumber());
        newCustomerEntity.setNit(userDTO.getNit());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(Lab2YouUtils.encodePassword(userDTO.getPassword()));
        newUser.setEnabled(true);
        newCustomerEntity.setGender(userDTO.getGender());
        newCustomerEntity.setOccupation(userDTO.getOccupation());
        newCustomerEntity.setUser(newUser);
        return newCustomerEntity;

    }
}
