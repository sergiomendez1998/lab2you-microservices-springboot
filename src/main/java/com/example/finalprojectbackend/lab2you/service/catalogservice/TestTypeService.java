package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.TestType;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.TestTypeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TestTypeService implements CatalogService<TestType> {
    private final TestTypeRepository testTypeRepository;

    public TestTypeService(TestTypeRepository testTypeRepository){
        this.testTypeRepository = testTypeRepository;
    }

    @CacheEvict(value = "testTypes",allEntries = true)
    @Override
    public TestType executeCreation(TestType entity) {
        return testTypeRepository.save(entity);
    }

    @CacheEvict(value = "testTypes",allEntries = true)
    @Override
    public TestType executeUpdate(TestType entity) {
        TestType testTypeFound = executeReadAll()
                .stream()
                .filter(testType -> testType.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (testTypeFound != null){
            testTypeFound
                    .setName(entity.getName() != null ? entity.getName() : testTypeFound.getName());
            testTypeFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : testTypeFound.getDescription());
            testTypeRepository.save(testTypeFound);
        }
        return testTypeFound;
    }

    @CacheEvict(value = "testTypes",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        TestType testTypeFound = executeReadAll()
                .stream()
                .filter(testType -> testType.getId().equals(id)).findFirst().orElse(null);

        if (testTypeFound != null){
            testTypeFound.setIsActive(false);
            testTypeRepository.save(testTypeFound);
        }
    }

    @Cacheable (value = "testTypes")
    @Override
    public List<TestType> executeReadAll() {
        return testTypeRepository.findAllByIsActiveTrue();
    }
}
