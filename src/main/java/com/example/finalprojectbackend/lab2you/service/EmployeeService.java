package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.EmployeeDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.EmployeeEntity;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudServiceProcessingController;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.*;
import com.example.finalprojectbackend.lab2you.db.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


import static com.example.finalprojectbackend.lab2you.Lab2YouConstants.lab2YouRoles.*;

@Service
public class EmployeeService extends CrudServiceProcessingController<EmployeeEntity> {
    private final EmployeeRepository employeeRepository;
    private final UserService userService;
    private ResponseWrapper responseWrapper;

    public EmployeeService(EmployeeRepository employeeRepository, UserService userService){
        this.employeeRepository = employeeRepository;
        this.userService = userService;
    }


    @Override
    public ResponseWrapper executeCreation(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        employeeRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("empleado creado exitosamente");
        responseWrapper.setData(Collections.singletonList("empleado creado exitosamente"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<EmployeeEntity> employeeEntityFound = employeeRepository.findById(entity.getId());
        if(employeeEntityFound.isPresent()){
            employeeEntityFound.get().getUser().setEmail(entity.getUser().getEmail() != null ? entity.getUser().getEmail() : employeeEntityFound.get().getUser().getEmail());
            employeeEntityFound.get().getUser().setRole(entity.getUser().getRole() != null ? entity.getUser().getRole() : employeeEntityFound.get().getUser().getRole());
            employeeEntityFound.get().setFirstName(entity.getFirstName() != null ? entity.getFirstName() : employeeEntityFound.get().getFirstName());
            employeeEntityFound.get().setLastName(entity.getLastName() != null ? entity.getLastName() : employeeEntityFound.get().getLastName());
            employeeRepository.save(employeeEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("empleado actualizado exitosamente");
            responseWrapper.setData(Collections.singletonList("empleado actualizado exitosamente"));
        return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("empleado no encontrado");
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeDeleteById(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        Optional<EmployeeEntity> employeeEntityFound = employeeRepository.findById(entity.getId());
        if(employeeEntityFound.isPresent()){
            employeeEntityFound.get().getUser().setEnabled(false);
            employeeRepository.save(employeeEntityFound.get());
            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("empleado eliminado exitosamente");
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("empleado no encontrado");
        return responseWrapper;

    }

    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("empleados encontrados exitosamente");

        List<EmployeeWrapper> employeeWrappers = employeeRepository.findAll()
                .stream()
                .filter(employeeEntity -> employeeEntity.getUser().isEnabled())
                .map(this::mapToEmployeeWrapper)
                .toList();

        responseWrapper.setData(employeeWrappers);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(EmployeeEntity entity) {

        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isNullOrEmpty(entity.getFirstName())) {
            responseWrapper.addError("firstName", "el nombre es requerido");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getLastName())) {
            responseWrapper.addError("lastName", "el apellido es requerido");
        }
        if (!Lab2YouUtils.validateCui(entity.getCui())) {
            responseWrapper.addError("cui", "el cui debe tener 13 digitos");
        }
        if(Lab2YouUtils.isNullOrEmpty(entity.getPhoneNumber())){
            responseWrapper.addError("phoneNumber", "el telefono es requerido");
        }
        if(!Lab2YouUtils.validatePhoneNumber(entity.getPhoneNumber())){
            responseWrapper.addError("phoneNumber", "el telefono debe tener 8 digitos");
        }
        if(Lab2YouUtils.isNullOrEmpty(entity.getAddress())){
            responseWrapper.addError("address", "la direccion es requerida");
        }
        EmployeeEntity cuiExist = employeeRepository.findByCui(entity.getCui());
        EmployeeEntity phoneNumberExist = employeeRepository.findByPhoneNumber(entity.getPhoneNumber());
        if(cuiExist != null){
            responseWrapper.addError("cui", "el cui ya existe");
        }
        if(phoneNumberExist != null){
            responseWrapper.addError("phoneNumber", "el telefono ya existe");
        }
        ResponseWrapper validationResponseForUser = userService.validateForCreation(entity.getUser());
        if(!validationResponseForUser.getErrors().isEmpty()){
            responseWrapper.getErrors().putAll(validationResponseForUser.getErrors());
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (Lab2YouUtils.isNullOrEmpty(entity.getFirstName())) {
            responseWrapper.addError("firstName", "el nombre es requerido");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getLastName())) {
            responseWrapper.addError("LastName", "el apellido es requerido");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getUser().getEmail())) {
            responseWrapper.addError("email", "el email es requerido");
        }
        if (Lab2YouUtils.isNullOrEmpty(entity.getUser().getRole().getName())) {
            responseWrapper.addError("role", "el rol es requerido");
        }

        ResponseWrapper validationResponseForUser = userService.validateForUpdate(entity.getUser());
        if(!validationResponseForUser.getErrors().isEmpty()){
            responseWrapper.getErrors().putAll(validationResponseForUser.getErrors());
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(EmployeeEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null) {
            responseWrapper.addError("id", "el id es requerido");
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(EmployeeEntity entity) {
        return null;
    }
    private EmployeeWrapper mapToEmployeeWrapper(EmployeeEntity employeeEntity){
        EmployeeWrapper employeeWrapper = new EmployeeWrapper();
        employeeWrapper.setId(employeeEntity.getId());
        employeeWrapper.setFirstName(employeeEntity.getFirstName());
        employeeWrapper.setLastName(employeeEntity.getLastName());
        employeeWrapper.setCui(employeeEntity.getCui());
        employeeWrapper.setPhoneNumber(employeeEntity.getPhoneNumber());
        employeeWrapper.setAddress(employeeEntity.getAddress());
        RoleWrapper roleWrapper = new RoleWrapper(employeeEntity.getUser().getRole().getId(), employeeEntity.getUser().getRole().getName(), employeeEntity.getUser().getRole().getDescription());
        UserWrapper userWrapper = new UserWrapper(employeeEntity.getUser().getId(),employeeEntity.getUser().getEmail(), roleWrapper);
        employeeWrapper.setUser(userWrapper);
        DepartmentWrapper departmentWrapper = new DepartmentWrapper(employeeEntity.getDepartmentEntity().getId(), employeeEntity.getDepartmentEntity().getName());
        employeeWrapper.setDepartment(departmentWrapper);
        return employeeWrapper;
    }

    public EmployeeEntity mapToEntityEmployee(EmployeeDTO employeeDTO){
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setId(employeeDTO.getId());
        employeeEntity.setFirstName(employeeDTO.getFirstName());
        employeeEntity.setLastName(employeeDTO.getLastName());
        employeeEntity.setCui(employeeDTO.getCui());
        employeeEntity.setPhoneNumber(employeeDTO.getPhoneNumber());
        employeeEntity.setAddress(employeeDTO.getAddress());
        employeeEntity.setGender(employeeDTO.getGender());
        return employeeEntity;
    }

    public EmployeeEntity getRandomEmployeeWithRoleTechnician(){
        List<EmployeeEntity> employeeEntities = new ArrayList<>(employeeRepository.findAll()
                .stream()
                .filter(employeeEntity -> employeeEntity.getUser().getRole().getName().equals(TECHNICIAN.getRole()))
                .toList());

        Collections.shuffle(employeeEntities);
        return employeeEntities.get(0);
    }

    public EmployeeEntity getRandomEmployeeWithRoleAnalyst(){
        List<EmployeeEntity> employeeEntities = new ArrayList<>(employeeRepository.findAll()
                .stream()
                .filter(employeeEntity -> employeeEntity.getUser().getRole().getName().equals(ANALYST.getRole()))
                .toList());

        Collections.shuffle(employeeEntities);
        return employeeEntities.get(0);
    }

    public EmployeeEntity getRandomEmployeeWithRoleCentralizer(){
        List<EmployeeEntity> employeeEntities = new ArrayList<>(employeeRepository.findAll()
                .stream()
                .filter(employeeEntity -> employeeEntity.getUser().getRole().getName().equals(CENTRALIZER.getRole()))
                .toList());

        Collections.shuffle(employeeEntities);
        return employeeEntities.get(0);
    }

    public EmployeeEntity findEmployeeByUserId(Long userId){
        return employeeRepository.findByUserId(userId);
    }
}
