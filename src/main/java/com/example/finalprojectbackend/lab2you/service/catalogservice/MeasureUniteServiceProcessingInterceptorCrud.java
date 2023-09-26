package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnitEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.MeasureUnitRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("measureUnit")
public class MeasureUniteServiceProcessingInterceptorCrud extends CrudCatalogServiceProcessingInterceptor<MeasureUnitEntity> {

    private final MeasureUnitRepository measureUnitRepository;
    private  ResponseWrapper responseWrapper;

    public MeasureUniteServiceProcessingInterceptorCrud(MeasureUnitRepository measureUnitRepository){
        this.measureUnitRepository = measureUnitRepository;
    }
    @CacheEvict(value = "measure units", allEntries = true)
    @Override
    public ResponseWrapper executeCreation(MeasureUnitEntity entity) {
        responseWrapper = new ResponseWrapper();
        measureUnitRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Measure unit created");
        responseWrapper.setData(Collections.singletonList("Measure unit created"));
        return responseWrapper;
    }
    @CacheEvict(value = "measure units",allEntries = true)
    @Override
    public ResponseWrapper executeUpdate(MeasureUnitEntity entity) {
        responseWrapper = new ResponseWrapper();

        Optional<MeasureUnitEntity> measureUnitEntityFound = measureUnitRepository.findById(entity.getId());

        if (measureUnitEntityFound.isPresent()) {
            measureUnitEntityFound.get().setName(entity.getName() != null ? entity.getName() : measureUnitEntityFound.get().getName());
            measureUnitEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : measureUnitEntityFound.get().getDescription());
            measureUnitRepository.save(measureUnitEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Measure unit updated");
            responseWrapper.setData(Collections.singletonList("Measure unit updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Measure unit not found");
        responseWrapper.addError("id","Measure unit not found");
        return responseWrapper;
    }

    @CacheEvict(value = "measureUnits",allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(MeasureUnitEntity measureUnitEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<MeasureUnitEntity> measureUnitEntityFound = measureUnitRepository.findById(measureUnitEntity.getId());
        measureUnitEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            measureUnitRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Measure unit deleted");
        responseWrapper.setData(Collections.singletonList("Measure unit deleted"));
        return responseWrapper;
    }
    @Cacheable(value = "measureUnits")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Measure units found");
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
        if (entity.getName() ==null || entity.getName().isEmpty()) {
            responseWrapper.addError("nombre", "el nombre no puedo ser nullo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("descripcion", "la descripcion no puedo ser nullo o vacio");
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
    protected ResponseWrapper validateForUpdate(MeasureUnitEntity entity) {
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
    protected ResponseWrapper validateForDelete(MeasureUnitEntity entity) {
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
    protected ResponseWrapper validateForRead(MeasureUnitEntity entity) {
        return null;
    }


    @Override
    public String getCatalogName() {
        return "measureUnit";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(MeasureUnitEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public MeasureUnitEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new MeasureUnitEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
