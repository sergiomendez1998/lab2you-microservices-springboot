package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ExamTypeItemWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ItemWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.ExamTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("examType")
public class ExamTypeService extends CrudCatalogServiceProcessingInterceptor<ExamTypeEntity> {
    private final ExamTypeRepository examTypeRepository;
    private ResponseWrapper responseWrapper;

    public ExamTypeService(ExamTypeRepository examTypeRepository) {
        this.examTypeRepository = examTypeRepository;
    }


    @Override
    public ResponseWrapper executeCreation(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        examTypeRepository.save(examTypeEntity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de examen creado");
        responseWrapper.setData(Collections.singletonList("Tipo de examen creado"));
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeUpdate(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ExamTypeEntity> examTypeFound = examTypeRepository.findById(examTypeEntity.getId());

        if (examTypeFound.isPresent()) {
            examTypeFound.get().setName(examTypeEntity.getName() != null ? examTypeEntity.getName() : examTypeFound.get().getName());
            examTypeFound.get().setDescription(examTypeEntity.getDescription() != null ? examTypeEntity.getDescription() : examTypeFound.get().getDescription());
            examTypeFound.get().setUpdatedBy(examTypeEntity.getUpdatedBy());
            examTypeRepository.save(examTypeFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Tipo de examen actualizado");
            responseWrapper.setData(Collections.singletonList("Tipo de examen actualizado"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Tipo de examen no encontrado");
        responseWrapper.addError("id", "Tipo de examen no encontrado");
        return responseWrapper;
    }


    @Override
    public ResponseWrapper executeDeleteById(ExamTypeEntity examTypeEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ExamTypeEntity> analysisDocumentTypeEntityFound = examTypeRepository.findById(examTypeEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeEntity.setUpdatedBy(examTypeEntity.getUpdatedBy());
            examTypeRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipo de examen eliminado");
        responseWrapper.setData(Collections.singletonList("Tipo de examen eliminado"));
        return responseWrapper;
    }

    @Cacheable(value = "examTypes")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Tipos de examenes encontrados");
        List<ExamTypeItemWrapper> examTypeItemWrapperList = examTypeRepository
                .findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToExamTypeItemWrapper)
                .toList();
        responseWrapper.setData(examTypeItemWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForUpdate(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForDelete(ExamTypeEntity entity) {
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
    protected ResponseWrapper validateForRead(ExamTypeEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "examType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(ExamTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public ExamTypeEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        ExamTypeEntity examType = new ExamTypeEntity(catalogDTO.getName(), catalogDTO.getDescription());
        examType.setCreatedBy(userLogged);
        return examType;
    }

    private ExamTypeItemWrapper mapToExamTypeItemWrapper(ExamTypeEntity examTypeEntity) {
        ExamTypeItemWrapper examTypeItemWrapper = new ExamTypeItemWrapper();
        examTypeItemWrapper.setId(examTypeEntity.getId());
        examTypeItemWrapper.setName(examTypeEntity.getName());
        examTypeItemWrapper.setDescription(examTypeEntity.getDescription());

        examTypeItemWrapper.setItems(examTypeEntity.getItems().stream().map(itemEntity -> {
            ItemWrapper itemWrapper = new ItemWrapper();
            itemWrapper.setId(itemEntity.getId());
            itemWrapper.setName(itemEntity.getName());
            itemWrapper.setDescription(itemEntity.getDescription());
            itemWrapper.setPrice(itemEntity.getPrice());
            return itemWrapper;
        }).toList());

        return examTypeItemWrapper;
    }

    @Override
    public ExamTypeEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        ExamTypeEntity examType = new ExamTypeEntity();
        examType.setId(catalogDTO.getId());
        examType.setName(catalogDTO.getName());
        examType.setDescription(catalogDTO.getDescription());
        examType.setUpdatedBy(userLogged);
        return examType;
    }

    public ExamTypeEntity findById(Long id) {
        return executeReadAll().getData()
                .stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    ExamTypeEntity entity = new ExamTypeEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new ExamTypeEntity());

    }
}
