package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.Department;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.DepartmentRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DepartmentService implements CatalogService<Department> {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository){
        this.departmentRepository = departmentRepository;
    }

    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public Department executeCreation(Department entity) {

        return departmentRepository.save(entity);
    }
    @CacheEvict(value = "departments", allEntries = true)
    @Override
    public Department executeUpdate(Department entity) {
        Department departmentFound = executeReadAll()
                .stream()
                .filter(department -> department.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (departmentFound != null ){
            departmentFound
                    .setName(entity.getName()!= null? entity.getName() : departmentFound.getName());
            departmentFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : departmentFound.getDescription());
            departmentRepository.save(departmentFound);
        }
        return departmentFound;

    }
    @CacheEvict(value = "departments",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        Department departmentFound = executeReadAll()
                .stream()
                .filter(department -> department.getId().equals(id)).findFirst().orElse(null);

        if (departmentFound != null) {
            departmentFound.setIsActive(false);
            departmentRepository.save(departmentFound);
        }

    }@Cacheable(value = "departments")
     @Override
    public List<Department> executeReadAll() {
        return departmentRepository.findAllByIsActiveTrue();
    }
}
