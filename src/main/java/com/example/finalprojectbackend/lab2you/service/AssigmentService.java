package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.entities.AssignmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AssignmentRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AssigmentService extends CrudServiceProcessingController<AssignmentEntity> {

    private final AssignmentRepository assignmentRepository;
    private ResponseWrapper responseWrapper;

    public AssigmentService(AssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }
    @Override
    public ResponseWrapper executeCreation(AssignmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        assignmentRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("request created successfully");
        responseWrapper.setData(Collections.singletonList("request created successfully"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(AssignmentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public ResponseWrapper executeDeleteById(AssignmentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public ResponseWrapper executeReadAll() {
       throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected ResponseWrapper validateForCreation(AssignmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getRequest())) {
            responseWrapper.addError("request", "la solicitud no debe de ser nula");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getAssignedByEmployee())) {
            responseWrapper.addError("employee", "el empleado que asigna no debe de ser nulo");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getAssignedToEmployee())) {
            responseWrapper.addError("employee", "el empleado que recibe no debe de ser nulo");
        }

        return responseWrapper;

    }

    @Override
    protected ResponseWrapper validateForUpdate(AssignmentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected ResponseWrapper validateForDelete(AssignmentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected ResponseWrapper validateForRead(AssignmentEntity entity) {
        return null;
    }
    public List<AssignmentEntity> findAllByRequestId(Long requestId) {
        return assignmentRepository.findAllByRequestId(requestId);
    }
}
