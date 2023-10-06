package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.dto.RequestDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.*;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.*;
import com.example.finalprojectbackend.lab2you.db.repository.RequestRepository;
import com.example.finalprojectbackend.lab2you.service.catalogservice.ExamTypeService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.SupportTypeService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class RequestService extends CrudServiceProcessingController<RequestEntity> {

    private final RequestRepository requestRepository;
    private final SupportTypeService supportTypeService;
    private ResponseWrapper responseWrapper;

    private final ExamTypeService examTypeService;

    private final AssigmentService assigmentService;

    public RequestService(RequestRepository requestRepository, ExamTypeService examTypeService, SupportTypeService supportTypeService, AssigmentService assigmentService) {
        this.requestRepository = requestRepository;
        this.examTypeService = examTypeService;
        this.supportTypeService = supportTypeService;
        this.assigmentService = assigmentService;
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
            responseWrapper.addError("customer", "el cliente solicitante no debe de ser nulo");
        }

         if (entity.getRequestDetails().isEmpty()) {
            responseWrapper.addError("requestDetails", "el requestDetails solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getSupportType())) {
            responseWrapper.addError("supportType", "el tipo de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getSupportNumber())) {
            responseWrapper.addError("supportNumber", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getEmail())) {
            responseWrapper.addError("email", "el email no debe de ser nulo");
        }
        if (!Lab2YouUtils.validatePhoneNumber(entity.getSupportNumber())) {
            responseWrapper.addError("supportNumber", "el numero de soporte debe de tener 8 digitos");
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
            responseWrapper.addError("supportNumber", "el numero de soporte solicitado no debe de ser nulo");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getEmail())) {
            responseWrapper.addError("email", "el email no debe de ser nulo");
        }
        if (!Lab2YouUtils.validatePhoneNumber(entity.getSupportNumber())) {
            responseWrapper.addError("supportNumber", "el numero de soporte debe de tener 8 digitos");
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
        List<ExamTypeEntity> examTypes = examTypeService.findExamByNames(examNames);
        examTypes.forEach(requestEntity::addExamType);
        requestEntity.setSupportType(supportTypeEntity);
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
        requestWrapper.setSupportNumber(requestEntity.getSupportNumber());
        StatusEntity mostRecentStatus = getMostRecentStatusForRequest(requestEntity);
        if (mostRecentStatus != null) {
            requestWrapper.setStatus(mostRecentStatus.getName());
        }
        return requestWrapper;
    }

    public StatusEntity getMostRecentStatusForRequest(RequestEntity request) {

        List<RequestStatusEntity> filteredStatusEntities =
            request.getRequestStatuses().stream()
                .filter(requestStatusEntity -> requestStatusEntity.getRequest().getId().equals(request.getId()))
                .collect(Collectors.toList());

        if (filteredStatusEntities.isEmpty()) {
            return null;
        }

        filteredStatusEntities.sort(Comparator.comparing(RequestStatusEntity::getCreatedAt).reversed());

        return filteredStatusEntities.get(0).getStatus();
    }

    public RequestEntity getRequestById(Long id) {
        return requestRepository.findById(id).orElse(null);
    }
    public ResponseWrapper getStatusesByRequestId(Long id) {
         List<RequestStatusEntity> statusEntities = requestRepository.findStatusesByRequestId(id);

         List<StatusRequestWrapper> statusRequestWrappers = statusEntities.stream()
                 .map(this::mapToStatusRequestWrapper)
                 .collect(Collectors.toList());

            responseWrapper = new ResponseWrapper();
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Request detail");
            responseWrapper.setData(statusRequestWrappers);
            return responseWrapper;
    }

    public ResponseWrapper getAllExamsByRequestId(Long id){
        List<RequestDetailEntity> requestDetailEntities = requestRepository.findDetailsByRequestId(id);

        List<RequestDetailWrapper> requestDetailWrappers = requestDetailEntities.stream()
                .map(this::mapToRequestDetailWrapper)
                .collect(Collectors.toList());

        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Exams found");
        responseWrapper.setData(requestDetailWrappers);
        return responseWrapper;

    }

    public ResponseWrapperRequest<Map<String,String>> getGeneralInformationByRequestId(Long id){
        RequestDetailEntity requestDetailEntity = requestRepository.findDetailsByRequestId(id).get(0);
        Map<String, String> generalInformation = MapToGeneralInformationWrapper(requestDetailEntity);
        return new ResponseWrapperRequest<Map<String, String>>(generalInformation, "General information found",true);
    }

    private StatusRequestWrapper mapToStatusRequestWrapper(RequestStatusEntity requestStatusEntity) {
        StatusRequestWrapper statusRequestWrapper = new StatusRequestWrapper();
        statusRequestWrapper.setId(requestStatusEntity.getId());
        statusRequestWrapper.setStatusName(requestStatusEntity.getStatus().getName());
        statusRequestWrapper.setRequestCode(requestStatusEntity.getRequest().getRequestCode());
        statusRequestWrapper.setAssignedDate(requestStatusEntity.getCreatedAt());
        return statusRequestWrapper;
    }

    public RequestDetailWrapper mapToRequestDetailWrapper(RequestDetailEntity requestDetail){
        ExamTypeWrapper examTypeWrapper = new ExamTypeWrapper();
        examTypeWrapper.setId(requestDetail.getExamType().getId());
        examTypeWrapper.setName(requestDetail.getExamType().getName());
        examTypeWrapper.setDescription(requestDetail.getExamType().getDescription());

        RequestDetailWrapper requestDetailWrapper = new RequestDetailWrapper();
        requestDetailWrapper.setId(requestDetail.getId());
        requestDetailWrapper.setExamType(examTypeWrapper);
        requestDetailWrapper.setRequestId(requestDetail.getRequest().getId());
        return requestDetailWrapper;
    }

    public Map<String, String> MapToGeneralInformationWrapper(RequestDetailEntity requestDetail) {

        Map<String, String> generalInformation = new HashMap<>();
        generalInformation.put("Código solicitud", requestDetail.getRequest().getRequestCode());
        generalInformation.put("No. expediente", requestDetail.getRequest().getRequestCode());
        generalInformation.put("Nit", requestDetail.getRequest().getCustomer().getNit());
        generalInformation.put("No. soport", requestDetail.getRequest().getSupportNumber());
        generalInformation.put("Tipo soporte", requestDetail.getRequest().getSupportType().getName());
        generalInformation.put("Tipo solicitante", requestDetail.getRequest().getCustomer().getUser().getUserType());
        List<AssignmentEntity> assignmentEntities = assigmentService.findAllByRequestId(requestDetail.getRequest().getId());
         assignmentEntities.sort(Comparator.comparing(AssignmentEntity::getAssignationDate).reversed());

        if (assignmentEntities.isEmpty()) {
            generalInformation.put("Usuario asignación", "No asignado");
        } else {
            generalInformation.put("Usuario asignación", assignmentEntities.get(0).getAssignedToEmployee().getFirstName()+" "+assignmentEntities.get(0).getAssignedToEmployee().getLastName());
        }
        List<RequestStatusEntity> statusEntities = requestRepository.findStatusesByRequestId(requestDetail.getRequest().getId());
        statusEntities.sort(Comparator.comparing(RequestStatusEntity::getCreatedAt).reversed());
        if (statusEntities.isEmpty()) {
            generalInformation.put("Estado", "No asignado");
        } else {
            generalInformation.put("Estado", statusEntities.get(0).getStatus().getName());
        }
        generalInformation.put("Fecha de recepción", requestDetail.getRequest().getReceptionDate().toString());
        generalInformation.put("Descripción", requestDetail.getRequest().getRemark());
        generalInformation.put("Solicitante", requestDetail.getRequest().getCustomer().getFirstName()+" "+requestDetail.getRequest().getCustomer().getLastName());
        generalInformation.put("Email", requestDetail.getRequest().getEmail());
        generalInformation.put("Teléfono", requestDetail.getRequest().getSupportNumber());

        return generalInformation;
    }

    @Override
    protected ResponseWrapper validateForRead(RequestEntity entity) {
        return null;
    }

    public RequestEntity findRequestByRequestCode(String requestCode) {
        return requestRepository.findByRequestCode(requestCode);
    }
}
