package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDEntity;
import com.example.finalprojectbackend.lab2you.db.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService implements CRUDEntity<CustomerEntity> {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public CustomerEntity executeCreation(CustomerEntity entity) {
        return customerRepository.save(entity);
    }

    @Override
    public CustomerEntity executeUpdate(CustomerEntity entity) {
        return customerRepository.save(entity);
    }

    @Override
    public void executeDeleteById(Long id) {
        throw new RuntimeException("Not supported");
    }

    @Override
    public List<CustomerEntity> executeReadAll() {
        return null;
    }
}
