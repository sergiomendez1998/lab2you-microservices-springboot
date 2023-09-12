package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SampleTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDCatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.SampleTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.*;

@Service
@Qualifier("sampleType")
public class SampleTypeServiceCRUD implements CRUDCatalogService<SampleTypeEntity> {

    private final SampleTypeRepository sampleTypeRepository;

    public SampleTypeServiceCRUD(SampleTypeRepository sampleTypeRepository){
        this.sampleTypeRepository = sampleTypeRepository;
    }

    @CacheEvict(value = "sampleTypes",allEntries = true)
    @Override
    public SampleTypeEntity executeCreation(SampleTypeEntity entity) {
        return sampleTypeRepository.save(entity);
    }

    @CacheEvict(value = "sampleTypes",allEntries = true)
    @Override
    public SampleTypeEntity executeUpdate(SampleTypeEntity entity) {
        SampleTypeEntity sampleTypeEntityFound = executeReadAll()
                .stream()
                .filter(sampleTypeEntity -> sampleTypeEntity.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (!isNull(sampleTypeEntityFound)){
            sampleTypeEntityFound
                    .setName(entity.getName() != null ? entity.getName() : sampleTypeEntityFound.getName());
            sampleTypeEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : sampleTypeEntityFound.getDescription());
            sampleTypeRepository.save(sampleTypeEntityFound);
            return sampleTypeEntityFound;
        }
        throw new RuntimeException("SampleType not found");
    }

    @CacheEvict(value = "sampleTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        SampleTypeEntity sampleTypeEntityFound = executeReadAll()
                .stream()
                .filter(sampleTypeEntity -> sampleTypeEntity.getId().equals(id)).findFirst().orElse(null);

        if (!isNull(sampleTypeEntityFound)){
            sampleTypeEntityFound.setIsDeleted(true);
            sampleTypeRepository.save(sampleTypeEntityFound);
        }
        throw new RuntimeException("SampleType not found");
    }

    @Cacheable (value = "sampleTypes")
    @Override
    public List<SampleTypeEntity> executeReadAll() {
        return sampleTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "sampleType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SampleTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public SampleTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new SampleTypeEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
