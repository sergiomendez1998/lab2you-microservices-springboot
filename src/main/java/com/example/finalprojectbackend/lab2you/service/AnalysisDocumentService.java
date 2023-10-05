package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.dto.AnalysisDocumentDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.AnalysisDocumentWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AnalysisDocumentRepository;
import com.example.finalprojectbackend.lab2you.service.catalogservice.AnalysisDocumentTypeService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AnalysisDocumentService extends CrudServiceProcessingController<AnalysisDocumentEntity> {

    private final AnalysisDocumentRepository analysisDocumentRepository;
    private final AnalysisDocumentTypeService analysisDocumentTypeService;
    private ResponseWrapper responseWrapper;

    public AnalysisDocumentService(AnalysisDocumentRepository analysisDocumentRepository, AnalysisDocumentTypeService analysisDocumentTypeService) {
        this.analysisDocumentRepository = analysisDocumentRepository;
        this.analysisDocumentTypeService = analysisDocumentTypeService;
    }

    @Override
    public ResponseWrapper executeCreation(AnalysisDocumentEntity entity) {
        responseWrapper = new ResponseWrapper();
        analysisDocumentRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocument created");
        responseWrapper.setData(Collections.singletonList("AnalysisDocument created"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(AnalysisDocumentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    public ResponseWrapper executeDeleteById(AnalysisDocumentEntity entity) {
        Optional<AnalysisDocumentEntity> analysisDocumentEntityFound = analysisDocumentRepository.findById(entity.getId());
        responseWrapper = new ResponseWrapper();
        if (analysisDocumentEntityFound.isPresent()) {
            analysisDocumentEntityFound.get().setIsDeleted(true);
            analysisDocumentRepository.save(analysisDocumentEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("AnalysisDocument deleted");
            responseWrapper.setData(Collections.singletonList("AnalysisDocument deleted"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("AnalysisDocument not found");
        responseWrapper.addError("id", "AnalysisDocument not found");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();

        List<AnalysisDocumentWrapper> analysisDocumentWrapperList = analysisDocumentRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToAnalysisDocumentWrapper)
                .toList();

        if (analysisDocumentWrapperList.isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("AnalysisDocument not found");
            responseWrapper.addError("id", "AnalysisDocument not found");
            return responseWrapper;
        }

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("AnalysisDocument found");
        responseWrapper.setData(analysisDocumentWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(AnalysisDocumentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if(Lab2YouUtils.isNullOrEmpty(entity.getPath())){
            responseWrapper.addError("path","path no debe de ser nulo");
        }

        if(Lab2YouUtils.isNullOrEmpty(entity.getResolution())){
            responseWrapper.addError("conclusion","conclusion no debe de ser nulo");
        }

        if(Lab2YouUtils.isObjectNullOrEmpty(entity.getSample())){
            responseWrapper.addError("observation","observation no debe de ser nulo");
        }

        if(Lab2YouUtils.isObjectNullOrEmpty(entity.getAnalysisDocumentType())){
            responseWrapper.addError("analysisDocumentType","analysisDocumentType no debe de ser nulo");
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(AnalysisDocumentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    @Override
    protected ResponseWrapper validateForDelete(AnalysisDocumentEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(AnalysisDocumentEntity entity) {
        throw new UnsupportedOperationException("not implemented yet");
    }

    public AnalysisDocumentEntity findById(Long id){
        return analysisDocumentRepository.findById(id).orElse(null);
    }

    public AnalysisDocumentEntity mapToEntityAnalysisDocument(AnalysisDocumentDTO analysisDocumentDTO){
        AnalysisDocumentEntity analysisDocumentEntity = new AnalysisDocumentEntity();
        AnalysisDocumentTypeEntity analysisDocumentType = analysisDocumentTypeService.executeReadAll()
                .getData()
                .stream()
                .map(analysisDocumentTypeEntity -> (AnalysisDocumentTypeEntity) analysisDocumentTypeEntity)
                .filter(analysisDocumentTypeEntity -> analysisDocumentTypeEntity.getId().equals(analysisDocumentDTO.getAnalysisDocumentType().getId()))
                .findFirst()
                .orElse(null);

        analysisDocumentEntity.setAnalysisDocumentType(analysisDocumentType);
        analysisDocumentEntity.setResolution(analysisDocumentDTO.getResolution());
        analysisDocumentEntity.setDocumentCode(Lab2YouUtils.generateDocumentCode());
        return analysisDocumentEntity;
    }

    private AnalysisDocumentWrapper mapToAnalysisDocumentWrapper(AnalysisDocumentEntity analysisDocumentEntity){
        AnalysisDocumentWrapper analysisDocumentWrapper = new AnalysisDocumentWrapper();
        analysisDocumentWrapper.setId(analysisDocumentEntity.getId());
        analysisDocumentWrapper.setDocumentCode(analysisDocumentEntity.getDocumentCode());
        analysisDocumentWrapper.setResolution(analysisDocumentEntity.getResolution());
        analysisDocumentWrapper.setDocumentType(analysisDocumentEntity.getAnalysisDocumentType().getName());
        analysisDocumentWrapper.setCustomerNit(analysisDocumentEntity.getSample().getRequestDetail().getRequest().getCustomer().getNit());
        analysisDocumentWrapper.setRequestCode(analysisDocumentEntity.getSample().getRequestDetail().getRequest().getRequestCode());
        return  analysisDocumentWrapper;
    }
}
