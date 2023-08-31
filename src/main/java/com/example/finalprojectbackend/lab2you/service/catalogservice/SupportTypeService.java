package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.SupportType;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.SupportTypeRepository;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupportTypeService implements CatalogService<SupportType> {

    private final SupportTypeRepository supportTypeRepository;

    @Autowired
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
}
