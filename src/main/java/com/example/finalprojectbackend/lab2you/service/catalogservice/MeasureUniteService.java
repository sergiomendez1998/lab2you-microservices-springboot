package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnit;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.MeasureUnitRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeasureUniteService implements CatalogService<MeasureUnit> {

    private final MeasureUnitRepository measureUnitRepository;

    public MeasureUniteService(MeasureUnitRepository measureUnitRepository){
        this.measureUnitRepository = measureUnitRepository;
    }
    @CacheEvict(value = "measure units", allEntries = true)
    @Override
    public MeasureUnit executeCreation(MeasureUnit entity) {
        return measureUnitRepository.save(entity);
    }
    @CacheEvict(value = "measure units",allEntries = true)
    @Override
    public MeasureUnit executeUpdate(MeasureUnit entity) {
        MeasureUnit measureUnitFound = executeReadAll()
                .stream()
                .filter(measureUnit -> measureUnit.getId().equals(entity.getId())).findFirst()
                .orElse(null);
        if(measureUnitFound != null){
            measureUnitFound
                    .setName(entity.getName() != null ? entity.getName() : measureUnitFound.getName());
            measureUnitFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : measureUnitFound.getDescription());
            measureUnitRepository.save(measureUnitFound);
        }
        return measureUnitFound;
    }

    @CacheEvict(value = "measureUnits",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        MeasureUnit measureUnitFound = executeReadAll()
                .stream()
                .filter(measureUnit -> measureUnit.getId().equals(id)).findFirst().orElse(null);

        if (measureUnitFound != null) {
              measureUnitFound.setIsActive(false);
              measureUnitRepository.save(measureUnitFound);
        }
    }
    @Cacheable(value = "measureUnits")
    @Override
    public List<MeasureUnit> executeReadAll() {
        return measureUnitRepository.findAllByIsActiveTrue();
    }
}
