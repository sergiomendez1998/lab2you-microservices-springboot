package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.DepartmentEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;

@Service
@Qualifier("department")
public class DepartmentService implements CatalogService<DepartmentEntity> {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public DepartmentEntity executeCreation(DepartmentEntity entity) {

        return departmentRepository.save(entity);
    }
    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public DepartmentEntity executeUpdate(DepartmentEntity entity) {
        DepartmentEntity departmentEntityFound = executeReadAll()
                .stream()
                .filter(departmentEntity -> departmentEntity.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (!isNull(departmentEntityFound)){
            departmentEntityFound
                    .setName(entity.getName()!= null? entity.getName() : departmentEntityFound.getName());
            departmentEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : departmentEntityFound.getDescription());
            departmentRepository.save(departmentEntityFound);
            return departmentEntityFound;
        }
        throw new RuntimeException("Department not found");

    }
    @CacheEvict(value = "departments",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        DepartmentEntity departmentEntityFound = executeReadAll()
                .stream()
                .filter(departmentEntity -> departmentEntity.getId().equals(id)).findFirst().orElse(null);

        if (!isNull(departmentEntityFound)) {
            departmentEntityFound.setIsDeleted(true);
            departmentRepository.save(departmentEntityFound);
        }

    }@Cacheable(value = "departments")
     @Override
    public List<DepartmentEntity> executeReadAll() {
        return departmentRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "department";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(DepartmentEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public DepartmentEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new DepartmentEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
