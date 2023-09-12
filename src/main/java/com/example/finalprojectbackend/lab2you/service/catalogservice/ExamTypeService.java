package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ExamTypeEntity;
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
public class ExamTypeService implements CatalogService<ExamTypeEntity> {
    private final ExamTypeRepository examTypeRepository;

    public ExamTypeService(ExamTypeRepository examTypeRepository){
        this.examTypeRepository = examTypeRepository;
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ExamTypeEntity executeCreation(ExamTypeEntity entity) {
        return examTypeRepository.save(entity);
    }

    @CacheEvict(value = "examTypes",allEntries = true)
    @Override
    public ExamTypeEntity executeUpdate(ExamTypeEntity entity) {
        ExamTypeEntity testTypeFound = executeReadAll()
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
        ExamTypeEntity examTypeEntityFound = executeReadAll()
                .stream()
                .filter(testType -> testType.getId().equals(id)).findFirst().orElse(null);

        if (examTypeEntityFound != null){
            examTypeEntityFound.setIsDeleted(true);
            examTypeRepository.save(examTypeEntityFound);
        }
    }

    @Cacheable (value = "examTypes")
    @Override
    public List<ExamTypeEntity> executeReadAll() {
        return examTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "examType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(ExamTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public ExamTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new ExamTypeEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
