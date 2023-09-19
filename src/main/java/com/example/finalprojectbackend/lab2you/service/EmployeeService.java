package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class EmployeeService extends CrudServiceProcessingController<EmployeeEntity> {
    private final EmployeeRepository employeeRepository;
    private ResponseWrapper responseWrapper;

    public EmployeeService(EmployeeRepository employeeRepository){this.employeeRepository = employeeRepository;}


    @Override
    public ResponseWrapper executeCreation(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        employeeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("employee created successfully");
        responseWrapper.setData(Collections.singletonList("employee created successfully"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(EmployeeEntity entity) {
        return null;
    }

    @Override
    public ResponseWrapper executeDeleteById(EmployeeEntity entity) {
        return null;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        return null;
    }

    @Override
    protected ResponseWrapper validateForCreation(EmployeeEntity entity) {
        return null;
    }

    @Override
    protected ResponseWrapper validateForUpdate(EmployeeEntity entity) {
        return null;
    }

    @Override
    protected ResponseWrapper validateForDelete(EmployeeEntity entity) {
        return null;
    }

    @Override
    protected ResponseWrapper validateForRead(EmployeeEntity entity) {
        return null;
    }
}
