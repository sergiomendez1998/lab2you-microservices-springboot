package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.StatusEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("status")
public class StatusService extends CrudCatalogServiceProcessingInterceptor<StatusEntity> {

    private final StatusRepository statusRepository;
    private ResponseWrapper responseWrapper;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    
    @Override
    public ResponseWrapper executeCreation(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        statusRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Status created");
        responseWrapper.setData(Collections.singletonList("Status created"));
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeUpdate(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<StatusEntity> statusEntityFound = statusRepository.findById(entity.getId());

        if (statusEntityFound.isPresent()) {
            statusEntityFound.get()
                    .setName(entity.getName() != null ? entity.getName() : statusEntityFound.get().getName());
            statusEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription()
                    : statusEntityFound.get().getDescription());
            statusEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
            statusRepository.save(statusEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Status updated");
            responseWrapper.setData(Collections.singletonList("Status updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Status not found");
        responseWrapper.addError("id", "Status not found");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeDeleteById(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<StatusEntity> statusEntityFound = statusRepository.findById(entity.getId());

        statusEntityFound.ifPresent(statusEntity1 -> {
            statusEntity1.setIsDeleted(true);
            statusEntity1.setUpdatedBy(entity.getUpdatedBy());
            statusRepository.save(statusEntity1);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Status deleted");
        responseWrapper.setData(Collections.singletonList("Status deleted"));
        return responseWrapper;
    }

    @Cacheable(value = "statuses")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Statuses found");

        List<CatalogWrapper> catalogWrapperList = statusRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();

        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getName() == null || entity.getName().isEmpty()) {
            responseWrapper.addError("name", "el nombre no puedo ser nulo o vacio");
        }

        if (entity.getDescription() == null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("description", "la descripcion no puedo ser nulo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(StatusEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(StatusEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "status";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(StatusEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public StatusEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        StatusEntity statusEntity = new StatusEntity(catalogDTO.getName(), catalogDTO.getDescription());
        statusEntity.setCreatedBy(userLogged);
        return statusEntity;
    }

    @Override
    public StatusEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(catalogDTO.getId());
        statusEntity.setDescription(catalogDTO.getDescription());
        statusEntity.setName(catalogDTO.getName());
        statusEntity.setUpdatedBy(userLogged);
        return statusEntity;
    }

    public StatusEntity findStatusByName(String name){
        return executeReadAll().getData().stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getName().equals(name))
                .findFirst()
                .map(catalogWrapper -> {
                    StatusEntity entity = new StatusEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new StatusEntity());
    }

    public StatusEntity findStatusById(Long id){
        return executeReadAll().getData().stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    StatusEntity entity = new StatusEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new StatusEntity());
    }
}
