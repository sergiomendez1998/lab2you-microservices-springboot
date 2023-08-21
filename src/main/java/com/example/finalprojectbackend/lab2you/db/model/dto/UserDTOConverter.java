package com.example.finalprojectbackend.lab2you.db.model.dto;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.entities.Customer;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;

public class UserDTOConverter {

    public Customer convertToEntity(UserDTO userDTO) {
        Customer newCustomer = new Customer();
        UserEntity newUser = new UserEntity();
        newCustomer.setCui(userDTO.getCui());
        newCustomer.setName(userDTO.getName());
        newCustomer.setLastName(userDTO.getLastName());
        newCustomer.setAddress(userDTO.getAddress());
        newCustomer.setPhoneNumber(userDTO.getPhoneNumber());
        newCustomer.setNit(userDTO.getNit());
        newUser.setEmail(userDTO.getEmail());
        newUser.setPassword(Lab2YouUtils.encodePassword(userDTO.getPassword()));
        newUser.setEnabled(true);
        newCustomer.setGender(userDTO.getGender());
        newCustomer.setOccupation(userDTO.getOccupation());
        newCustomer.setUser(newUser);
        return newCustomer;
    }
}
