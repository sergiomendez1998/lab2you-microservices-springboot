package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.CustomerDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.CustomerEntity;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CustomerWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService extends CrudServiceProcessingController<CustomerEntity> {

    private final CustomerRepository customerRepository;
    private final UserService userService;
    private ResponseWrapper responseWrapper;

    public CustomerService(CustomerRepository customerRepository, UserService userService) {
        this.customerRepository = customerRepository;
        this.userService = userService;

    }

    @Override
    public ResponseWrapper executeCreation(CustomerEntity entity) {
        responseWrapper = new ResponseWrapper();
        userService.save(entity.getUser());
        customerRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("cliente creado exitosamente");
        responseWrapper.setData(Collections.singletonList("cliente creado exitosamente"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(CustomerEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<CustomerEntity> customerEntityFound = customerRepository.findById(entity.getId());
        if (customerEntityFound.isPresent()) {
            customerEntityFound.get().setNit(entity.getNit() != null ? entity.getNit() : customerEntityFound.get().getNit());
            customerEntityFound.get().setFirstName(entity.getFirstName() != null ? entity.getFirstName() : customerEntityFound.get().getFirstName());
            customerEntityFound.get().setLastName(entity.getLastName() != null ? entity.getLastName() : customerEntityFound.get().getLastName());
            customerRepository.save(customerEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("cliente actualizado exitosamente");
            responseWrapper.setData(Collections.singletonList("cliente actualizado exitosamente"));
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("customer not found");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeDeleteById(CustomerEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<CustomerEntity> customerEntityFound = customerRepository.findById(entity.getId());
        if (customerEntityFound.isPresent()) {
            customerEntityFound.get().getUser().setEnabled(false);
            customerRepository.save(customerEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("cliente eliminado exitosamente");
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("cliente no encontrado");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("clientes encontrados existosamente");

        List<CustomerWrapper> customerWrappers = customerRepository.findAll()
                .stream()
                .filter(customerEntity -> customerEntity.getUser().isEnabled())
                .map(this::mapToCustomerWrapper)
                .toList();

        responseWrapper.setData(customerWrappers);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(CustomerEntity entity) {

        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isNullOrEmpty(entity.getFirstName())) {
            responseWrapper.addError("firstName", "el nombre es requerido");
        }

        if (Lab2YouUtils.isNullOrEmpty(entity.getLastName())) {
            responseWrapper.addError("lastName", "el apellido es requerido");
        }

        if (!Lab2YouUtils.validateCui(entity.getCui())) {
            responseWrapper.addError("cui", "el cui debe de tener 13 digitos");
        }

        if (!Lab2YouUtils.validateNit(entity.getNit())) {
            responseWrapper.addError("nit", "el nit debe de tener 9 digitos");
        }

        if (Lab2YouUtils.isNullOrEmpty(entity.getPhoneNumber())) {
            responseWrapper.addError("phoneNumber", "el telefono es requerido");
        }

        if (!Lab2YouUtils.validatePhoneNumber(entity.getPhoneNumber())) {
            responseWrapper.addError("phoneNumber", "el telefono debe de tener 8 digitos");
        }

        if (Lab2YouUtils.isNullOrEmpty(entity.getAddress())) {
            responseWrapper.addError("address", "la direccion es requerida");
        }

        CustomerEntity nitExist = customerRepository.findByNit(entity.getNit());
        CustomerEntity phoneNumberExist = customerRepository.findByPhoneNumber(entity.getPhoneNumber());
        CustomerEntity cuiExist = customerRepository.findByCui(entity.getCui());

        if (nitExist != null) {
            responseWrapper.addError("nit", "el nit ya existe");
        }

        if (phoneNumberExist != null) {
            responseWrapper.addError("phoneNumber", "el telefono ya existe");
        }

        if (cuiExist != null) {
            responseWrapper.addError("cui", "el cui ya existe");
        }

        ResponseWrapper validationResponseForUser = userService.validateForCreation(entity.getUser());

        if(!validationResponseForUser.getErrors().isEmpty()){
            responseWrapper.getErrors().putAll(validationResponseForUser.getErrors());
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(CustomerEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isNullOrEmpty(entity.getFirstName())) {
            responseWrapper.addError("firstName", "el nombre es requerido");
        }

        if (Lab2YouUtils.isNullOrEmpty(entity.getLastName())) {
            responseWrapper.addError("lastName", "el apellido es requerido");
        }

        if (Lab2YouUtils.isNullOrEmpty(entity.getCui())) {
            responseWrapper.addError("cui", "el cui es requerido");
        }

        if (Lab2YouUtils.isObjectNullOrEmpty(entity.getNit())){
            responseWrapper.addError("nit", "el nit es requerido");
        }


        CustomerEntity nitExist = customerRepository.findByNit(entity.getNit());
        if (nitExist != null) {
            if (!nitExist.getId().equals(entity.getId())) {
                responseWrapper.addError("nit", "el nit ya existe");
            }
        }

        ResponseWrapper validationResponseForUser = userService.validateForUpdate(entity.getUser());

        if(!validationResponseForUser.getErrors().isEmpty()){
            responseWrapper.getErrors().putAll(validationResponseForUser.getErrors());
        }

        return responseWrapper;

    }

    @Override
    protected ResponseWrapper validateForDelete(CustomerEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null) {
            responseWrapper.addError("id", "el id es requerido");
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(CustomerEntity entity) {
        return null;
    }

    private CustomerWrapper mapToCustomerWrapper(CustomerEntity customerEntity) {
        CustomerWrapper customerWrapper = new CustomerWrapper();
        customerWrapper.setId(customerEntity.getId());
        customerWrapper.setExpedientNumber(customerEntity.getExpedientNumber());
        customerWrapper.setCui(customerEntity.getCui());
        customerWrapper.setFirstName(customerEntity.getFirstName());
        customerWrapper.setLastName(customerEntity.getLastName());
        customerWrapper.setNit(customerEntity.getNit());
        customerWrapper.setPhoneNumber(customerEntity.getPhoneNumber());
        customerWrapper.setAddress(customerEntity.getAddress());
        customerWrapper.getUser().setEmail(customerEntity.getUser().getEmail());
        customerWrapper.getUser().getRole().setName(customerEntity.getUser().getRole().getName());
        customerWrapper.getUser().getRole().setId(customerEntity.getUser().getRole().getId());
        customerWrapper.getUser().getRole().setDescription(customerEntity.getUser().getRole().getDescription());
        return customerWrapper;
    }

    public CustomerEntity mapToEntityCustomer(CustomerDTO customerDTO){
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setExpedientNumber(Lab2YouUtils.generateExpedientNumber());
        customerEntity.setCui(customerDTO.getCui());
        customerEntity.setNit(customerDTO.getNit());
        customerEntity.setFirstName(customerDTO.getFirstName());
        customerEntity.setLastName(customerDTO.getLastName());
        customerEntity.setAddress(customerDTO.getAddress());
        customerEntity.setPhoneNumber(customerDTO.getPhoneNumber());
        customerEntity.setOccupation(customerDTO.getOccupation());
        customerEntity.setGender(customerDTO.getGender());
        return customerEntity;
    }
    public CustomerEntity findCustomerByUserId(Long id){
        return customerRepository.findByUserId(id);
    }
}
