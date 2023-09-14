package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDEntity;
import com.example.finalprojectbackend.lab2you.db.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class EmployeeService implements CRUDEntity<EmployeeEntity> {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository){this.employeeRepository = employeeRepository;}

    @Override
    public EmployeeEntity executeCreation(EmployeeEntity entity) { return employeeRepository.save(entity);}

    @Override
    public EmployeeEntity executeUpdate(EmployeeEntity entity) { return employeeRepository.save(entity);}

    @Override
    public void executeDeleteById(Long id){throw new RuntimeException("Not supported");}

    @Override
    public List<EmployeeEntity> executeReadAll(){return null; }
}
