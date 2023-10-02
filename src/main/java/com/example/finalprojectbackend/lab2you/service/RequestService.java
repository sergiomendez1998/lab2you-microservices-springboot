package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.RequestWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.StatusRequestWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RequestRepository;
import com.example.finalprojectbackend.lab2you.service.catalogservice.ExamTypeService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.StatusService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.SupportTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.statusTypes.*;

@Service
public class RequestService extends CrudServiceProcessingController<RequestEntity> {

    private final RequestRepository requestRepository;
    private final SupportTypeService supportTypeService;
    private final AssigmentService assigmentService;
    private ResponseWrapper responseWrapper;
    private final StatusService statusService;
    private final ExamTypeService examTypeService;

    public RequestService(RequestRepository requestRepository, AssigmentService assigmentService, StatusService statusService, ExamTypeService examTypeService, SupportTypeService supportTypeService) {
        this.requestRepository = requestRepository;
        this.assigmentService = assigmentService;
        this.statusService = statusService;
        this.examTypeService = examTypeService;
        this.supportTypeService = supportTypeService;
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

        if (entity.getExamTypes() == null || entity.getExamTypes().isEmpty()) {
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
        List<String> examNames = new ArrayList<>();
        requestDTO.getExamType().forEach(examTypeDTO -> examNames.add(examTypeDTO.getName()));
        requestEntity.setSupportNumber(requestDTO.getSupportNumber());
        requestEntity.setEmail(requestDTO.getEmail());
        requestEntity.setRemark(requestDTO.getRemark());
        LocalDateTime localDateTime = LocalDateTime.now();
        Date date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
        requestEntity.setRequestCode(Lab2YouUtils.generateRequestCode(date));
        SupportTypeEntity supportTypeEntity = supportTypeService.getSupportByName(requestDTO.getSupportType().getName());
        StatusEntity statusEntity = statusService.findStatusByName(CREATED.getStatusType());
        List<ExamTypeEntity> examTypes = examTypeService.findExamByNames(examNames);
        requestEntity.setSupportType(supportTypeEntity);
        requestEntity.getStatusEntities().add(statusEntity);
        requestEntity.getExamTypes().addAll(examTypes);
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
//        requestWrapper.setExamType(requestEntity.getExamType().getName());
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

    public RequestEntity getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }
    public ResponseWrapper getStatusesByRequestId(Long id) {
         List<StatusEntity> statusEntities = requestRepository.findStatusesByRequestId(id);
         StatusRequestWrapper statusRequestWrapper = new StatusRequestWrapper();

         List<StatusRequestWrapper> statusRequestWrappers = statusEntities.stream()
                 .map(this::mapToStatusRequestWrapper)
                 .collect(Collectors.toList());

            responseWrapper = new ResponseWrapper();
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Statuses found");
            responseWrapper.setData(statusRequestWrappers);
            return responseWrapper;
    }

    private StatusRequestWrapper mapToStatusRequestWrapper(StatusEntity statusEntity) {
        StatusRequestWrapper statusRequestWrapper = new StatusRequestWrapper();
        statusRequestWrapper.setStatusName(statusEntity.getName());
        statusRequestWrapper.setRequestCode(statusEntity.getRequests().get(0).getRequestCode());
        return statusRequestWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(RequestEntity entity) {
        return null;
    }

    public RequestEntity findRequestByRequestCode(String requestCode) {
        return requestRepository.findByRequestCode(requestCode);
    }
}
