package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.SampleTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("sampleType")
public class SampleTypeService extends CrudCatalogServiceProcessingInterceptor<SampleTypeEntity> {

    private final SampleTypeRepository sampleTypeRepository;
    private ResponseWrapper responseWrapper;

    public SampleTypeService(SampleTypeRepository sampleTypeRepository) {
        this.sampleTypeRepository = sampleTypeRepository;
    }


    @Override
    public ResponseWrapper executeCreation(SampleTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        sampleTypeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de muestra creada");
        responseWrapper.setData(Collections.singletonList("Tipo de muestra creada"));
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeUpdate(SampleTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<SampleTypeEntity> sampleTypeEntityFound = sampleTypeRepository.findById(entity.getId());

        if (sampleTypeEntityFound.isPresent()) {
            sampleTypeEntityFound.get().setName(entity.getName() != null ? entity.getName() : sampleTypeEntityFound.get().getName());
            sampleTypeEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : sampleTypeEntityFound.get().getDescription());
            sampleTypeEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
            sampleTypeRepository.save(sampleTypeEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Tipo de muestra actualizada");
            responseWrapper.setData(Collections.singletonList("Tipo de muestra actualizada"));
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Tipo de muestra no encontrada");
        responseWrapper.addError("id", "Tipo de muestra no encontrada");
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeDeleteById(SampleTypeEntity sampleTypeEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<SampleTypeEntity> sampleTypeEntityFound = sampleTypeRepository.findById(sampleTypeEntity.getId());

        sampleTypeEntityFound.ifPresent(
                sampleTypeEntity1 -> {
                    sampleTypeEntity1.setIsDeleted(true);
                    sampleTypeEntity1.setUpdatedBy(sampleTypeEntity.getUpdatedBy());
                    sampleTypeRepository.save(sampleTypeEntity1);
                }
        );

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de muestra eliminada");
        responseWrapper.setData(Collections.singletonList("Tipo de muestra eliminada"));
        return responseWrapper;

    }

    @Cacheable(value = "sampleTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipos de muestra encontrados");
        List<CatalogWrapper> sampleTypeEntities = sampleTypeRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(sampleTypeEntities);

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(SampleTypeEntity entity) {
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
    protected ResponseWrapper validateForUpdate(SampleTypeEntity entity) {
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
    protected ResponseWrapper validateForDelete(SampleTypeEntity entity) {
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
    protected ResponseWrapper validateForRead(SampleTypeEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "sampleType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SampleTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public SampleTypeEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        SampleTypeEntity sampleTypeEntity = new SampleTypeEntity(catalogDTO.getName(), catalogDTO.getDescription());
        sampleTypeEntity.setCreatedBy(userLogged);
        return sampleTypeEntity;
    }

    @Override
    public SampleTypeEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        SampleTypeEntity sampleTypeEntity = new SampleTypeEntity();
        sampleTypeEntity.setId(catalogDTO.getId());
        sampleTypeEntity.setName(catalogDTO.getName());
        sampleTypeEntity.setDescription(catalogDTO.getDescription());
        sampleTypeEntity.setUpdatedBy(userLogged);
        return sampleTypeEntity;
    }

    public SampleTypeEntity findById(Long id) {
        return executeReadAll().getData()
                .stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    SampleTypeEntity entity = new SampleTypeEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new SampleTypeEntity());
    }
}
