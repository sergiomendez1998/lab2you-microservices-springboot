package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SupportTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDCatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.SupportTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.*;

@Service
@Qualifier("supportType")
public class SupportTypeServiceCRUD implements CRUDCatalogService<SupportTypeEntity> {

    private final SupportTypeRepository supportTypeRepository;

    public SupportTypeServiceCRUD(SupportTypeRepository supportTypeRepository){
        this.supportTypeRepository = supportTypeRepository;
    }

    @CacheEvict(value = "SupportTypes", allEntries = true)
    @Override
    public SupportTypeEntity executeCreation(SupportTypeEntity entity) {
        return supportTypeRepository.save(entity);
    }

    @CacheEvict(value = "supportTypes",allEntries = true)
    @Override
    public SupportTypeEntity executeUpdate(SupportTypeEntity entity) {
        SupportTypeEntity supportTypeEntityFound = executeReadAll()
                .stream()
                .filter(supportTypeEntity -> supportTypeEntity.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if(!isNull(supportTypeEntityFound)){
            supportTypeEntityFound
                    .setName(entity.getName() != null ? entity.getName() : supportTypeEntityFound.getName());
            supportTypeEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : supportTypeEntityFound.getDescription());
            supportTypeRepository.save(supportTypeEntityFound);
            return supportTypeEntityFound;
        }
        throw new RuntimeException("SupportType not found");
    }

    @CacheEvict(value = "supportTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        SupportTypeEntity supportTypeEntityFound = executeReadAll()
                .stream()
                .filter(supportTypeEntity -> supportTypeEntity.getId().equals(id)).findFirst().orElse(null);

        if (!isNull(supportTypeEntityFound)){
            supportTypeEntityFound.setIsDeleted(true);
            supportTypeRepository.save(supportTypeEntityFound);
        }

        throw new RuntimeException("SupportType not found");
    }

    @Cacheable (value = "supportTypes")
    @Override
    public List<SupportTypeEntity> executeReadAll() {
        return supportTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "supportType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SupportTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public SupportTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new SupportTypeEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
