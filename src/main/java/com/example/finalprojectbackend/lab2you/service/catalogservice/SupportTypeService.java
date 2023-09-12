package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.SupportType;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.SupportTypeRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("supportType")
public class SupportTypeService implements CatalogService<SupportType> {

    private final SupportTypeRepository supportTypeRepository;

    public SupportTypeService(SupportTypeRepository supportTypeRepository){
        this.supportTypeRepository = supportTypeRepository;
    }

    @CacheEvict(value = "SupportTypes", allEntries = true)
    @Override
    public SupportType executeCreation(SupportType entity) {
        return supportTypeRepository.save(entity);
    }

    @CacheEvict(value = "supportTypes",allEntries = true)
    @Override
    public SupportType executeUpdate(SupportType entity) {
        SupportType supportTypeFound = executeReadAll()
                .stream()
                .filter(supportType -> supportType.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if(supportTypeFound != null){
            supportTypeFound
                    .setName(entity.getName() != null ? entity.getName() : supportTypeFound.getName());
            supportTypeFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : supportTypeFound.getDescription());
            supportTypeRepository.save(supportTypeFound);
        }
        return supportTypeFound;
    }

    @CacheEvict(value = "supportTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        SupportType supportTypeFound = executeReadAll()
                .stream()
                .filter(supportType -> supportType.getId().equals(id)).findFirst().orElse(null);

        if (supportTypeFound != null){
            supportTypeFound.setIsActive(false);
            supportTypeRepository.save(supportTypeFound);
        }
    }

    @Cacheable (value = "supportTypes")
    @Override
    public List<SupportType> executeReadAll() {
        return supportTypeRepository.findAllByIsActiveTrue();
    }

    @Override
    public String getCatalogName() {
        return "supportType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(SupportType catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public SupportType mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new SupportType(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
