package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnitEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.MeasureUnitRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("measureUnit")
public class MeasureUniteService implements CatalogService<MeasureUnitEntity> {

    private final MeasureUnitRepository measureUnitRepository;

    public MeasureUniteService(MeasureUnitRepository measureUnitRepository){
        this.measureUnitRepository = measureUnitRepository;
    }
    @CacheEvict(value = "measure units", allEntries = true)
    @Override
    public MeasureUnitEntity executeCreation(MeasureUnitEntity entity) {
        return measureUnitRepository.save(entity);
    }
    @CacheEvict(value = "measure units",allEntries = true)
    @Override
    public MeasureUnitEntity executeUpdate(MeasureUnitEntity entity) {
        MeasureUnitEntity measureUnitEntityFound = executeReadAll()
                .stream()
                .filter(measureUnitEntity -> measureUnitEntity.getId().equals(entity.getId())).findFirst()
                .orElse(null);
        if(measureUnitEntityFound != null){
            measureUnitEntityFound
                    .setName(entity.getName() != null ? entity.getName() : measureUnitEntityFound.getName());
            measureUnitEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : measureUnitEntityFound.getDescription());
            measureUnitRepository.save(measureUnitEntityFound);
        }
        return measureUnitEntityFound;
    }

    @CacheEvict(value = "measureUnits",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        MeasureUnitEntity measureUnitEntityFound = executeReadAll()
                .stream()
                .filter(measureUnitEntity -> measureUnitEntity.getId().equals(id)).findFirst().orElse(null);

        if (measureUnitEntityFound != null) {
              measureUnitEntityFound.setIsDeleted(true);
              measureUnitRepository.save(measureUnitEntityFound);
        }
    }
    @Cacheable(value = "measureUnits")
    @Override
    public List<MeasureUnitEntity> executeReadAll() {
        return measureUnitRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "measureUnit";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(MeasureUnitEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public MeasureUnitEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new MeasureUnitEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
