package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnitEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.MeasureUnitRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("measureUnit")
public class MeasureUniteService extends CrudCatalogServiceProcessingInterceptor<MeasureUnitEntity> {

    private final MeasureUnitRepository measureUnitRepository;
    private ResponseWrapper responseWrapper;

    public MeasureUniteService(MeasureUnitRepository measureUnitRepository) {
        this.measureUnitRepository = measureUnitRepository;
    }

    @Override
    public ResponseWrapper executeCreation(MeasureUnitEntity entity) {
        responseWrapper = new ResponseWrapper();
        measureUnitRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Unidad de medida creada");
        responseWrapper.setData(Collections.singletonList("Unidad de medida creada"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(MeasureUnitEntity entity) {
        responseWrapper = new ResponseWrapper();

        Optional<MeasureUnitEntity> measureUnitEntityFound = measureUnitRepository.findById(entity.getId());

        if (measureUnitEntityFound.isPresent()) {
            measureUnitEntityFound.get().setName(entity.getName() != null ? entity.getName() : measureUnitEntityFound.get().getName());
            measureUnitEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : measureUnitEntityFound.get().getDescription());
            measureUnitEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
            measureUnitRepository.save(measureUnitEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Unidad de medida actualizada");
            responseWrapper.setData(Collections.singletonList("Unidad de medida actualizada"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Unidad de medida no encontrada");
        responseWrapper.addError("id", "Unidad de medida no encontrada");
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeDeleteById(MeasureUnitEntity measureUnitEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<MeasureUnitEntity> measureUnitEntityFound = measureUnitRepository.findById(measureUnitEntity.getId());
        measureUnitEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeEntity.setUpdatedBy(measureUnitEntity.getUpdatedBy());
            measureUnitRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Unidad de medida eliminada");
        responseWrapper.setData(Collections.singletonList("Unidad de medida eliminada"));
        return responseWrapper;
    }

    @Cacheable(value = "measureUnits")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Unidades de medidas encontradas");
        List<CatalogWrapper> catalogWrapperList = measureUnitRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(MeasureUnitEntity entity) {
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
    protected ResponseWrapper validateForUpdate(MeasureUnitEntity entity) {
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
    protected ResponseWrapper validateForDelete(MeasureUnitEntity entity) {
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
    protected ResponseWrapper validateForRead(MeasureUnitEntity entity) {
        return null;
    }


    @Override
    public String getCatalogName() {
        return "measureUnit";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(MeasureUnitEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public MeasureUnitEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        MeasureUnitEntity measureUnitEntity = new MeasureUnitEntity(catalogDTO.getName(), catalogDTO.getDescription());
        measureUnitEntity.setCreatedBy(userLogged);
        return measureUnitEntity;
    }

    @Override
    public MeasureUnitEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        MeasureUnitEntity measureUnitEntity = new MeasureUnitEntity();
        measureUnitEntity.setId(catalogDTO.getId());
        measureUnitEntity.setName(catalogDTO.getName());
        measureUnitEntity.setDescription(catalogDTO.getDescription());
        measureUnitEntity.setUpdatedBy(userLogged);
        return measureUnitEntity;
    }

    public MeasureUnitEntity findById(Long id) {
        return executeReadAll().getData()
                .stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    MeasureUnitEntity entity = new MeasureUnitEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new MeasureUnitEntity());
    }

}
