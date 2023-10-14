package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AnalysisDocumentTypeRepository;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("analysisDocumentType")
public class AnalysisDocumentTypeService extends CrudCatalogServiceProcessingInterceptor<AnalysisDocumentTypeEntity> {

    private final AnalysisDocumentTypeRepository analysisDocumentTypeRepository;
    private  ResponseWrapper responseWrapper;

    public AnalysisDocumentTypeService(AnalysisDocumentTypeRepository analysisDocumentTypeRepository) {
        this.analysisDocumentTypeRepository = analysisDocumentTypeRepository;
    }


    @Override
    public ResponseWrapper executeCreation(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        analysisDocumentTypeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de documento de analisis creado");
        responseWrapper.setData(Collections.singletonList("Tipo de documento de analisis creado"));
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeUpdate(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<AnalysisDocumentTypeEntity> analysisDocumentTypeEntityFound = analysisDocumentTypeRepository.findById(entity.getId());

        if (analysisDocumentTypeEntityFound.isPresent()) {
            analysisDocumentTypeEntityFound.get().setName(entity.getName() != null ? entity.getName() : analysisDocumentTypeEntityFound.get().getName());
            analysisDocumentTypeEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : analysisDocumentTypeEntityFound.get().getDescription());
            analysisDocumentTypeEntityFound.get().setUpdatedBy(entity.getUpdatedBy());
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Tipo de documento de analisis actualizado");
            responseWrapper.setData(Collections.singletonList("Tipo de documento de analisis actualizado"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Tipo de documento de analisis no encontrado");
        responseWrapper.setData(new ArrayList<>());
        responseWrapper.addError("id", "Tipo de documento de analisis no encontrado");

        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeDeleteById(AnalysisDocumentTypeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<AnalysisDocumentTypeEntity> analysisDocumentTypeEntityFound = analysisDocumentTypeRepository.findById(entity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeEntity.setUpdatedBy(entity.getUpdatedBy());
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de documento de analisis eliminado");
        responseWrapper.setData(Collections.singletonList("Tipo de documento de analisis eliminado"));
        return responseWrapper;
    }

    @Cacheable(value = "analysisDocumentTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de documento de analisis encontrado");

        List<CatalogWrapper> catalogWrapperList = analysisDocumentTypeRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
        responseWrapper.setData(catalogWrapperList);

        if (catalogWrapperList.isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Tipo de documento de analisis no encontrado");
            responseWrapper.setData(new ArrayList<>());
            responseWrapper.addError("id", "Tipo de documento de analisis no encontrado");
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    public ResponseWrapper validateForCreation(AnalysisDocumentTypeEntity entity) {
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
    public ResponseWrapper validateForUpdate(AnalysisDocumentTypeEntity entity) {
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
    public ResponseWrapper validateForDelete(AnalysisDocumentTypeEntity entity) {
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
    public AnalysisDocumentTypeEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        AnalysisDocumentTypeEntity  analysisDocumentType = new AnalysisDocumentTypeEntity(catalogDTO.getName(), catalogDTO.getDescription());
        analysisDocumentType.setCreatedBy(userLogged);
        return analysisDocumentType;
    }

    @Override
    public AnalysisDocumentTypeEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        AnalysisDocumentTypeEntity  analysisDocumentType = new AnalysisDocumentTypeEntity();
        analysisDocumentType.setId(catalogDTO.getId());
        analysisDocumentType.setName(catalogDTO.getName());
        analysisDocumentType.setDescription(catalogDTO.getDescription());
        analysisDocumentType.setUpdatedBy(userLogged);
        return analysisDocumentType;
    }

    public AnalysisDocumentTypeEntity findById(Long id) {
        return executeReadAll().getData()
                .stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    AnalysisDocumentTypeEntity entity = new AnalysisDocumentTypeEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new AnalysisDocumentTypeEntity());
    }

}
