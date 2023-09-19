package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AnalysisDocumentTypeRepository;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("analysisDocumentType")
public class AnalysisDocumentTypeServiceProcessingInterceptorCrud extends CrudCatalogServiceProcessingInterceptor<AnalysisDocumentTypeEntity> {

    private final AnalysisDocumentTypeRepository analysisDocumentTypeRepository;
    private  ResponseWrapper responseWrapper;

    public AnalysisDocumentTypeServiceProcessingInterceptorCrud(AnalysisDocumentTypeRepository analysisDocumentTypeRepository) {
        this.analysisDocumentTypeRepository = analysisDocumentTypeRepository;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public ResponseWrapper executeCreation(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        analysisDocumentTypeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocumentType created");
        responseWrapper.setData(Collections.singletonList("AnalysisDocumentType created"));
        return responseWrapper;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public ResponseWrapper executeUpdate(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<AnalysisDocumentTypeEntity> analysisDocumentTypeEntityFound = analysisDocumentTypeRepository.findById(entity.getId());

        if (analysisDocumentTypeEntityFound.isPresent()) {
            analysisDocumentTypeEntityFound.get().setName(entity.getName() != null ? entity.getName() : analysisDocumentTypeEntityFound.get().getName());
            analysisDocumentTypeEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : analysisDocumentTypeEntityFound.get().getDescription());
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("AnalysisDocumentType updated");
            responseWrapper.setData(Collections.singletonList("AnalysisDocumentType updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("AnalysisDocumentType not found");
        responseWrapper.setData(new ArrayList<>());
        responseWrapper.addError("id", "AnalysisDocumentType not found");

        return responseWrapper;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<AnalysisDocumentTypeEntity> analysisDocumentTypeEntityFound = analysisDocumentTypeRepository.findById(entity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocumentType deleted");
        responseWrapper.setData(Collections.singletonList("AnalysisDocumentType deleted"));
        return responseWrapper;
    }

    @Cacheable(value = "analysisDocumentTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocumentTypes found");

        List<CatalogWrapper> catalogWrapperList = analysisDocumentTypeRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(catalogWrapperList);

        return responseWrapper;
    }

    @Override
    public ResponseWrapper validateForCreation(AnalysisDocumentTypeEntity entity) {
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
    public ResponseWrapper validateForUpdate(AnalysisDocumentTypeEntity entity) {
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
    public ResponseWrapper validateForDelete(AnalysisDocumentTypeEntity entity) {
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
    public ResponseWrapper validateForRead(AnalysisDocumentTypeEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "analysisDocumentType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(AnalysisDocumentTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public AnalysisDocumentTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new AnalysisDocumentTypeEntity(catalogDTO.getName(), catalogDTO.getDescription());
    }


}
