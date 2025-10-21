package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SupportTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.SupportTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("supportType")
public class SupportTypeService extends CrudCatalogServiceProcessingInterceptor<SupportTypeEntity> {

    private final SupportTypeRepository supportTypeRepository;
    private  ResponseWrapper responseWrapper;
    public SupportTypeService(SupportTypeRepository supportTypeRepository){
        this.supportTypeRepository = supportTypeRepository;
    }


    @Override
    public ResponseWrapper executeCreation(SupportTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        supportTypeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de soporte creado");
        responseWrapper.setData(Collections.singletonList("Tipo de soporte creado"));
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeUpdate(SupportTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
       Optional<SupportTypeEntity> supportTypeEntityFound = supportTypeRepository.findById(entity.getId());

         if (supportTypeEntityFound.isPresent()) {
              supportTypeEntityFound.get().setName(entity.getName() != null ? entity.getName() : supportTypeEntityFound.get().getName());
              supportTypeEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : supportTypeEntityFound.get().getDescription());
              supportTypeEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
              supportTypeRepository.save(supportTypeEntityFound.get());

              responseWrapper.setSuccessful(true);
              responseWrapper.setMessage("Tipo de soporte actualizado");
              responseWrapper.setData(Collections.singletonList("Tipo de soporte actualizado"));
              return responseWrapper;
         }

            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Tipo de soporte no encontrado");
            responseWrapper.addError("id","Tipo de soporte no encontrado");
            return responseWrapper;
    }


    @Override
    public ResponseWrapper executeDeleteById(SupportTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<SupportTypeEntity> supportTypeEntityFound = supportTypeRepository.findById(entity.getId());

        supportTypeEntityFound.ifPresent(supportTypeEntity -> {
            supportTypeEntity.setIsDeleted(true);
            supportTypeEntity.setUpdatedBy(entity.getUpdatedBy());
            supportTypeRepository.save(supportTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de soporte eliminado");
        responseWrapper.setData(Collections.singletonList("Tipo de soporte eliminado"));
        return responseWrapper;
    }

    @Cacheable (value = "supportTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipos de soporte encontrados");
        List<CatalogWrapper> catalogWrapperList = supportTypeRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(SupportTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getName() ==null || entity.getName().isEmpty()) {
            responseWrapper.addError("name", "el nombre no puedo ser nulo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
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
    protected ResponseWrapper validateForUpdate(SupportTypeEntity entity) {
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
    protected ResponseWrapper validateForDelete(SupportTypeEntity entity) {
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
    protected ResponseWrapper validateForRead(SupportTypeEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "supportType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SupportTypeEntity catalogItem) {
        CatalogWrapper catalogWrapper = new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
        catalogWrapper.setUserType(catalogItem.getUserType());
        return catalogWrapper;
    }

    @Override
    public SupportTypeEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        SupportTypeEntity supportTypeEntity = new SupportTypeEntity(catalogDTO.getName(),catalogDTO.getDescription());
        supportTypeEntity.setCreatedBy(userLogged);
        return supportTypeEntity;
    }

    @Override
    public SupportTypeEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        SupportTypeEntity supportTypeEntity = new SupportTypeEntity();
        supportTypeEntity.setId(catalogDTO.getId());
        supportTypeEntity.setName(catalogDTO.getName());
        supportTypeEntity.setDescription(catalogDTO.getDescription());
        supportTypeEntity.setUpdatedBy(userLogged);
        return supportTypeEntity;
    }
    public SupportTypeEntity getSupportByName(String name){
        return executeReadAll().getData().stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getName().equals(name))
                .findFirst()
                .map(catalogWrapper -> {
                    SupportTypeEntity entity = new SupportTypeEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new SupportTypeEntity());
    }
}
