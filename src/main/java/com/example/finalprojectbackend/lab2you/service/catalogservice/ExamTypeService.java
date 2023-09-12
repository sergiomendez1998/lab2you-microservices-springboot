package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamType;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.ExamTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("examType")
public class ExamTypeService implements CatalogService<ExamType> {
    private final ExamTypeRepository examTypeRepository;

    public ExamTypeService(ExamTypeRepository examTypeRepository){
        this.examTypeRepository = examTypeRepository;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ExamType executeCreation(ExamType entity) {
        return examTypeRepository.save(entity);
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ExamType executeUpdate(ExamType entity) {
        ExamType testTypeFound = executeReadAll()
                .stream()
                .filter(testType -> testType.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (testTypeFound != null){
            testTypeFound
                    .setName(entity.getName() != null ? entity.getName() : testTypeFound.getName());
            testTypeFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : testTypeFound.getDescription());
            examTypeRepository.save(testTypeFound);
        }
        return testTypeFound;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        ExamType examTypeFound = executeReadAll()
                .stream()
                .filter(testType -> testType.getId().equals(id)).findFirst().orElse(null);

        if (examTypeFound != null){
            examTypeFound.setIsDeleted(true);
            examTypeRepository.save(examTypeFound);
        }
    }

    @Cacheable (value = "examTypes")
    @Override
    public List<ExamType> executeReadAll() {
        return examTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "examType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(ExamType catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public ExamType mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new ExamType(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
