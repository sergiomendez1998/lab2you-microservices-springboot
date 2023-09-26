package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.RequestEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.StatusEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.RequestWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RequestRepository;
import com.example.finalprojectbackend.lab2you.service.catalogservice.ExamTypeServiceProcessingInterceptorCrud;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusProcessingControllerServiceCrud;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.statusTypes.*;

@Service
public class RequestService extends CrudServiceProcessingController<RequestEntity> {

    private final RequestRepository requestRepository;
    private final AssigmentService assigmentService;
    private ResponseWrapper responseWrapper;
    private final StatusProcessingControllerServiceCrud statusProcessingControllerServiceCrud;
    private final ExamTypeServiceProcessingInterceptorCrud examTypeServiceProcessingInterceptorCrud;

    public RequestService(RequestRepository requestRepository, AssigmentService assigmentService, StatusProcessingControllerServiceCrud statusProcessingControllerServiceCrud, ExamTypeServiceProcessingInterceptorCrud examTypeServiceProcessingInterceptorCrud) {
        this.requestRepository = requestRepository;
        this.assigmentService = assigmentService;
        this.statusProcessingControllerServiceCrud = statusProcessingControllerServiceCrud;
        this.examTypeServiceProcessingInterceptorCrud = examTypeServiceProcessingInterceptorCrud;
    }

    @Override
    public ResponseWrapper executeCreation(RequestEntity entity) {
        responseWrapper = new ResponseWrapper();
        requestRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("request created successfully");
        responseWrapper.setData(Collections.singletonList("request created successfully"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(RequestEntity entity) {
        return null;
    }

    @Override
    public ResponseWrapper executeDeleteById(RequestEntity entity) {
        return null;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();

        List<RequestWrapper> requestWrappers = requestRepository.findAllByIsDeletedFalse().stream()
                .map(this::mapToRequestWrapper)
                .collect(Collectors.toList());

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("requests retrieved successfully");
        responseWrapper.setData(requestWrappers);

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(RequestEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getCustomer())) {
            responseWrapper.addError("cliente", "el cliente solicitante no debe de ser nulo");
        }
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getExamType())) {
            responseWrapper.addError("tipo de examen", "el tipo de examen solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getExamType())) {
            responseWrapper.addError("tipo de examen", "el tipo de examen solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getSupportType())) {
            responseWrapper.addError("tipo de soporte", "el tipo de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getEmail())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (!Lab2YouUtils.validatePhoneNumber(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte debe de tener 8 digitos");
        }
        if (!Lab2YouUtils.verifyEmailFormat(entity.getEmail())) {
            responseWrapper.addError("email", "el email no tiene un formato valido");
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(RequestEntity entity) {
        responseWrapper = new ResponseWrapper();

        if (entity.getId() == null) {
            responseWrapper.addError("id", "el id no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getEmail())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (!Lab2YouUtils.validatePhoneNumber(entity.getSupportNumber())) {
            responseWrapper.addError("numero de soporte", "el numero de soporte debe de tener 8 digitos");
        }
        if (!Lab2YouUtils.verifyEmailFormat(entity.getEmail())) {
            responseWrapper.addError("email", "el email no tiene un formato valido");
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(RequestEntity entity) {
        responseWrapper = new ResponseWrapper();

        if (entity.getId() == null) {
            responseWrapper.addError("id", "el id no debe de ser nulo");
        }
        return responseWrapper;
    }

    public RequestEntity mapToRequestEntity(RequestDTO requestDTO) {
        RequestEntity requestEntity = new RequestEntity();
        requestEntity.setSupportNumber(requestDTO.getSupportNumber());
        requestEntity.setEmail(requestDTO.getEmail());
        requestEntity.setRemark(requestDTO.getRemark());
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        requestEntity.setRequestCode(Lab2YouUtils.generateRequestCode(date));

        StatusEntity statusEntity = statusProcessingControllerServiceCrud.executeReadAll()
                .getData()
                .stream()
                .map(status -> (StatusEntity) status)
                .filter(status -> status.getName().equals(CREATED.getStatusType()))
                .findFirst()
                .orElse(null);
        requestEntity.setStatusEntities(Collections.singletonList(statusEntity));

        ExamTypeEntity examTypeEntity = examTypeServiceProcessingInterceptorCrud.executeReadAll()
                .getData()
                .stream()
                .map(exam -> (ExamTypeEntity) exam)
                .filter(exam -> exam.getName().equals(requestDTO.getExamType().getName()))
                .findFirst()
                .orElse(null);

        requestEntity.setExamType(examTypeEntity);
        return requestEntity;
    }

    private RequestWrapper mapToRequestWrapper(RequestEntity requestEntity) {
        RequestWrapper requestWrapper = new RequestWrapper();

        requestWrapper.setId(requestEntity.getId());
        requestWrapper.setRequestCode(requestEntity.getRequestCode());
        requestWrapper.setCustomerFirstName(requestEntity.getCustomer().getFirstName());
        requestWrapper.setCustomerLastName(requestEntity.getCustomer().getLastName());
        requestWrapper.setCustomerNit(requestEntity.getCustomer().getNit());
        requestWrapper.setCustomerExpedientNumber(requestEntity.getCustomer().getExpedientNumber());
        requestWrapper.setCreationDate(requestEntity.getReceptionDate());
        requestWrapper.setExamType(requestEntity.getExamType().getName());
        requestWrapper.setSupportNumber(requestEntity.getSupportNumber());

        StatusEntity mostRecentStatus = getMostRecentStatusForRequest(requestEntity);
        if (mostRecentStatus != null) {
            requestWrapper.setStatus(mostRecentStatus.getName());
        }

        requestWrapper.setSampleQuantity(Lab2YouUtils.calculateQuantityFromList(requestEntity.getSamples()));
        requestWrapper.setItemQuantity(Lab2YouUtils.calculateQuantityFromList(requestEntity.getSamples().stream()
                .map(SampleEntity::getItemEntities)
                .flatMap(List::stream)
                .collect(Collectors.toList())));
        requestWrapper.setUserAssigned(assigmentService.findAllByRequestId(requestEntity.getId()).get(0).getAssignedToEmployee().getFirstName());

        requestWrapper.setDocumentQuantity(Lab2YouUtils.calculateQuantityFromList(requestEntity.getSamples().stream()
                .map(SampleEntity::getAnalysisDocumentEntities)
                .flatMap(List::stream)
                .collect(Collectors.toList())));

        requestWrapper.setExpirationDays(Lab2YouUtils.calculateExpirationDays(requestEntity.getReceptionDate()));
        return requestWrapper;
    }

    public StatusEntity getMostRecentStatusForRequest(RequestEntity request) {

        List<StatusEntity> filteredStatusEntities = request.getStatusEntities().stream()
                .filter(status -> status.getRequests().contains(request))
                .collect(Collectors.toList());

        if (filteredStatusEntities.isEmpty()) {
            return null;
        }

        filteredStatusEntities.sort(Comparator.comparing(StatusEntity::getCreatedAt).reversed());
        return filteredStatusEntities.get(0);
    }

    @Override
    protected ResponseWrapper validateForRead(RequestEntity entity) {
        return null;
    }
}
