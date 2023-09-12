package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleType;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.SampleTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("sampleType")
public class SampleTypeService implements CatalogService<SampleType> {

    private final SampleTypeRepository sampleTypeRepository;

    public SampleTypeService(SampleTypeRepository sampleTypeRepository){
        this.sampleTypeRepository = sampleTypeRepository;
    }

    @CacheEvict(value = "sampleTypes",allEntries = true)
    @Override
    public SampleType executeCreation(SampleType entity) {
        return sampleTypeRepository.save(entity);
    }

    @CacheEvict(value = "sampleTypes",allEntries = true)
    @Override
    public SampleType executeUpdate(SampleType entity) {
        SampleType sampleTypeFound = executeReadAll()
                .stream()
                .filter(sampleType -> sampleType.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (sampleTypeFound != null){
            sampleTypeFound
                    .setName(entity.getName() != null ? entity.getName() : sampleTypeFound.getName());
            sampleTypeFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : sampleTypeFound.getDescription());
            sampleTypeRepository.save(sampleTypeFound);
        }
        return sampleTypeFound;
    }

    @CacheEvict(value = "sampleTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        SampleType sampleTypeFound = executeReadAll()
                .stream()
                .filter(sampleType -> sampleType.getId().equals(id)).findFirst().orElse(null);

        if (sampleTypeFound != null){
            sampleTypeFound.setIsDeleted(true);
            sampleTypeRepository.save(sampleTypeFound);
        }
    }

    @Cacheable (value = "sampleTypes")
    @Override
    public List<SampleType> executeReadAll() {
        return sampleTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "sampleType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SampleType catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public SampleType mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new SampleType(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
