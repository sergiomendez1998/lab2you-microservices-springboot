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
        responseWrapper.setMessage("muestra creada exitosamente");
        responseWrapper.setData(Collections.singletonList("muestra creada exitosamente"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(SampleEntity entity) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ResponseWrapper executeDeleteById(SampleEntity entity) {
        responseWrapper = new ResponseWrapper();

        entity.setDeleted(true);
        entity.getSampleItemEntities().forEach(sampleItemEntity -> {
            sampleItemEntity.setDeleted(true);
        });

        sampleRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("muestra eliminada exitosamente");
        return responseWrapper;
    }

    public void disassociateItem(SampleEntity sampleEntity, Long itemId){
        sampleEntity.getSampleItemEntities().forEach(sampleItemEntity -> {
            if(sampleItemEntity.getItem().getId().equals(itemId)){
                sampleItemEntity.setDeleted(true);
            }
        });
        sampleRepository.save(sampleEntity);
    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("muestras encontradas exitosamente");

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
            responseWrapper.addError("presentation", "la presentacion es requerida");
        }

        if(entity.getQuantity() == null){
            responseWrapper.addError("quantity", "la cantidad es requerida");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getRequestDetail())){
            responseWrapper.addError("RequestDetail", "el id del de la solicitud detalle es requerido");
        }

        if (entity.getExpirationDate() == null){
            responseWrapper.addError("expirationDate", "la fecha de expiracion es requerida: ");
        }

        if (!Lab2YouUtils.validateQuantity(entity.getQuantity())){
            responseWrapper.addError("quantity", "la cantidad debe tener entre 1 y 4 digitos");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getSampleTypeEntity())){
            responseWrapper.addError("sampleType", "el tipo de muestra es requerido");
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

    public SampleEntity findSampleById(Long id){
        return sampleRepository.findById(id).orElse(null);
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