package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentType;
import com.example.finalprojectbackend.lab2you.db.repository.AnalysisDocumentTypeRepository;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class AnalysisDocumentTypeService implements CatalogService<AnalysisDocumentType> {

    private final AnalysisDocumentTypeRepository analysisDocumentTypeRepository;

    public AnalysisDocumentTypeService(AnalysisDocumentTypeRepository analysisDocumentTypeRepository) {
        this.analysisDocumentTypeRepository = analysisDocumentTypeRepository;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public AnalysisDocumentType executeCreation(AnalysisDocumentType entity) {
        return analysisDocumentTypeRepository.save(entity);
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public AnalysisDocumentType executeUpdate(AnalysisDocumentType entity) {
        AnalysisDocumentType analysisDocumentTypeFound = executeReadAll()
                .stream()
                .filter(analysisDocumentType -> analysisDocumentType.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (analysisDocumentTypeFound != null) {
            analysisDocumentTypeFound
                    .setName(entity.getName() != null ? entity.getName() : analysisDocumentTypeFound.getName());
            analysisDocumentTypeFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : analysisDocumentTypeFound.getDescription());
            analysisDocumentTypeRepository.save(analysisDocumentTypeFound);
        }
        return analysisDocumentTypeFound;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {

        AnalysisDocumentType analysisDocumentTypeFound = executeReadAll()
                .stream()
                .filter(analysisDocumentType -> analysisDocumentType.getId().equals(id)).findFirst().orElse(null);

        if (analysisDocumentTypeFound != null) {
            analysisDocumentTypeFound.setIsActive(false);
            analysisDocumentTypeRepository.save(analysisDocumentTypeFound);
        }
    }

    @Cacheable(value = "analysisDocumentTypes")
    @Override
    public List<AnalysisDocumentType> executeReadAll() {
        return analysisDocumentTypeRepository.findAllByIsActiveTrue();
    }
}
