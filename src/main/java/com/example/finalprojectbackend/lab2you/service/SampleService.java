package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.dto.SampleDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.SampleWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.SampleRepository;
import org.springframework.stereotype.Service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SampleService extends CrudServiceProcessingController<SampleEntity> {

    private final SampleRepository sampleRepository;
    private ResponseWrapper responseWrapper;

    public SampleService(SampleRepository sampleRepository) {
        this.sampleRepository = sampleRepository;

    }

    @Override
    public ResponseWrapper executeCreation(SampleEntity entity) {
        responseWrapper = new ResponseWrapper();
        sampleRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("sample created successfully");
        responseWrapper.setData(Collections.singletonList("sample created successfully"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(SampleEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseWrapper executeDeleteById(SampleEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<SampleEntity> sampleEntityFound = sampleRepository.findById(entity.getId());
        if (sampleEntityFound.isPresent()) {
            sampleEntityFound.get().setDeleted(false);
            sampleRepository.save(sampleEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("sample deleted successfully");
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.addError("sample","sample not found");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("sample found successfully");

        List<SampleWrapper> sampleWrappers= sampleRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToSampleWrapper)
                .toList();

        responseWrapper.setData(sampleWrappers);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(SampleEntity entity) {
        responseWrapper = new ResponseWrapper();

        if(Lab2YouUtils.isNullOrEmpty(entity.getPresentation())){
            responseWrapper.addError("presentacion", "la presentacion es requerida");
        }

        if(entity.getQuantity() == null){
            responseWrapper.addError("cantidad", "la cantidad es requerida");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getRequestDetail())){
            responseWrapper.addError("RequestDetail", "el id del de la solicitud detalle es requerido");
        }

        if (entity.getExpirationDate() == null){
            responseWrapper.addError("fecha de expiracion", "la fecha de expiracion es requerida: ");
        }

        if (!Lab2YouUtils.validateQuantity(entity.getQuantity())){
            responseWrapper.addError("cantidad", "la cantidad debe tener entre 1 y 4 digitos");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getSampleTypeEntity())){
            responseWrapper.addError("tipo de muestra", "el tipo de muestra es requerido");
        }



        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(SampleEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected ResponseWrapper validateForDelete(SampleEntity entity) {
        responseWrapper = new ResponseWrapper();
        if(entity.getId() == null){
            responseWrapper.addError("id", "el id es requerido");
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(SampleEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private SampleWrapper mapToSampleWrapper(SampleEntity sampleEntity){
        SampleWrapper sampleWrapper = new SampleWrapper();
        sampleWrapper.setLabel(sampleEntity.getLabel());
        sampleWrapper.setPresentation(sampleEntity.getPresentation());
        sampleWrapper.setQuantity(sampleEntity.getQuantity());
        sampleWrapper.setExpirationDate(sampleEntity.getExpirationDate());
        sampleWrapper.getSampleType().setId(sampleEntity.getSampleTypeEntity().getId());
        sampleWrapper.getSampleType().setName(sampleEntity.getSampleTypeEntity().getName());
        sampleWrapper.getSampleType().setDescription(sampleEntity.getSampleTypeEntity().getDescription());
        sampleWrapper.getMeasureUnit().setId(sampleEntity.getMeasureUnitEntity().getId());
        sampleWrapper.getMeasureUnit().setName(sampleEntity.getMeasureUnitEntity().getName());
        sampleWrapper.getMeasureUnit().setDescription(sampleEntity.getMeasureUnitEntity().getDescription());
        return sampleWrapper;
    }

    public SampleEntity mapToEntitySample(SampleDTO sampleDTO){
        SampleEntity sampleEntity = new SampleEntity();
        sampleEntity.setLabel(sampleDTO.getLabel());
        sampleEntity.setPresentation(sampleDTO.getPresentation());
        sampleEntity.setQuantity(sampleDTO.getQuantity());
        sampleEntity.setExpirationDate(sampleDTO.getExpirationDate());
        return sampleEntity;
    }
}