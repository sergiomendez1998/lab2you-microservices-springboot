package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Qualifier("role")
public class RoleProcessingControllerServiceCrud extends CrudCatalogServiceProcessingInterceptor<RoleEntity> {

    private final RoleRepository roleRepository;
    private final ResponseWrapper responseWrapper;

    public RoleProcessingControllerServiceCrud(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
        this.responseWrapper = new ResponseWrapper();
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public ResponseWrapper executeCreation(RoleEntity entity) {
        roleRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Role created");
        responseWrapper.setData(Collections.singletonList("Role created"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(RoleEntity entity) {
        Optional<RoleEntity> roleEntityFound = roleRepository.findById(entity.getId());

        if (roleEntityFound.isPresent()) {
            roleEntityFound.get().setName(entity.getName() != null ? entity.getName() : roleEntityFound.get().getName());
            roleEntityFound.get().setDescription(entity.getDescription() != null ? entity.getDescription() : roleEntityFound.get().getDescription());
            roleRepository.save(roleEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Role updated");
            responseWrapper.setData(Collections.singletonList("Role updated"));
            return responseWrapper;
        }
        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Role not found");
        responseWrapper.addError("Id","Role not found");
        return responseWrapper;
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(RoleEntity roleEntity) {

        Optional<RoleEntity> roleEntityFound = roleRepository.findById(roleEntity.getId());

        roleEntityFound.ifPresent(roleEntity1 -> {
            roleEntity1.setIsDeleted(true);
            roleRepository.save(roleEntity1);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Role deleted");
        responseWrapper.setData(Collections.singletonList("Role deleted"));
        return responseWrapper;

    }
    @Cacheable (value = "roles")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Roles found");

        List<CatalogWrapper> catalogWrapperList = roleRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();
       responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(RoleEntity entity) {
        if (entity.getName() ==null || entity.getName().isEmpty()) {
            responseWrapper.addError("nombre", "el nombre no puedo ser nullo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("descripcion", "la descripcion no puedo ser nullo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(RoleEntity entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(RoleEntity entity) {
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(RoleEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "role";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(RoleEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public RoleEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new RoleEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }

    public RoleEntity getRoleByName(String name){
        return this.executeReadAll().getData()
                .stream()
                .map(RoleEntity.class::cast)
                .filter(roleEntity -> roleEntity.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
