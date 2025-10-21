package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.DepartmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("department")
public class DepartmentService extends CrudCatalogServiceProcessingInterceptor<DepartmentEntity> {

    private final DepartmentRepository departmentRepository;
    private ResponseWrapper responseWrapper;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }


    @Override
    public ResponseWrapper executeCreation(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        departmentRepository.save(entity);

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Departmento creado");
        responseWrapper.setData(Collections.singletonList("Departmento creado"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<DepartmentEntity> departmentEntityFound = departmentRepository.findById(entity.getId());

        if (departmentEntityFound.isPresent()) {
            departmentEntityFound.get().setName(entity.getName() != null ? entity.getName() : departmentEntityFound.get().getName());
            departmentEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : departmentEntityFound.get().getDescription());
            departmentEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
            departmentRepository.save(departmentEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Departmento actualizado");
            responseWrapper.setData(Collections.singletonList("Departmento actualizado"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Departmento no encontrado");
        responseWrapper.addError("id", "Departmento no encontrado");
        return responseWrapper;

    }


    @Override
    public ResponseWrapper executeDeleteById(DepartmentEntity departmentEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<DepartmentEntity> analysisDocumentTypeEntityFound = departmentRepository.findById(departmentEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeEntity.setUpdatedBy(departmentEntity.getUpdatedBy());
            departmentRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocumentType deleted");
        responseWrapper.setData(Collections.singletonList("AnalysisDocumentType deleted"));
        return responseWrapper;
    }

    @Cacheable(value = "departments")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Departmentos encontrados");

        List<CatalogWrapper> catalogWrapperList = departmentRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();

        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getName() == null || entity.getName().isEmpty()) {
            responseWrapper.addError("name", "el nombre no puedo ser nulo o vacio");
        }

        if (entity.getDescription() == null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("description", "la descripcion no puedo ser nulo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validando");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validando");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(DepartmentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validando");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(DepartmentEntity entity) {
        return null;
    }


    @Override
    public String getCatalogName() {
        return "department";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(DepartmentEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public DepartmentEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        DepartmentEntity departmentEntity = new DepartmentEntity(catalogDTO.getName(), catalogDTO.getDescription());
        departmentEntity.setCreatedBy(userLogged);
        return departmentEntity;
    }

    @Override
    public DepartmentEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setId(catalogDTO.getId());
        departmentEntity.setName(catalogDTO.getName());
        departmentEntity.setDescription(catalogDTO.getDescription());
        departmentEntity.setUpdatedBy(userLogged);
        return departmentEntity;
    }

    public DepartmentEntity getDepartmentByName(String name) {
            return executeReadAll().getData().stream()
                    .filter(item -> item instanceof CatalogWrapper)
                    .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                    .filter(catalogWrapper -> catalogWrapper.getName().equals(name))
                    .findFirst()
                    .map(catalogWrapper -> {
                        DepartmentEntity entity = new DepartmentEntity();
                        entity.setId(catalogWrapper.getId());
                        entity.setName(catalogWrapper.getName());
                        entity.setDescription(catalogWrapper.getDescription());
                        return entity;
                    })
                    .orElse(new DepartmentEntity());
        }
}
