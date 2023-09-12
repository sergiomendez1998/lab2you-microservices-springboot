package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.AnalysisDocumentTypeRepository;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDCatalogService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Qualifier("analysisDocumentType")
public class AnalysisDocumentTypeServiceCRUD implements CRUDCatalogService<AnalysisDocumentTypeEntity> {

    private final AnalysisDocumentTypeRepository analysisDocumentTypeRepository;

    public AnalysisDocumentTypeServiceCRUD(AnalysisDocumentTypeRepository analysisDocumentTypeRepository) {
        this.analysisDocumentTypeRepository = analysisDocumentTypeRepository;
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public AnalysisDocumentTypeEntity executeCreation(AnalysisDocumentTypeEntity entity) {
        return analysisDocumentTypeRepository.save(entity);
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public AnalysisDocumentTypeEntity executeUpdate(AnalysisDocumentTypeEntity entity) {
        AnalysisDocumentTypeEntity analysisDocumentTypeEntityFound = executeReadAll()
                .stream()
                .filter(analysisDocumentTypeEntity -> analysisDocumentTypeEntity.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (!isNull(analysisDocumentTypeEntityFound)) {
            analysisDocumentTypeEntityFound
                    .setName(entity.getName() != null ? entity.getName() : analysisDocumentTypeEntityFound.getName());
            analysisDocumentTypeEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : analysisDocumentTypeEntityFound.getDescription());
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntityFound);
            return analysisDocumentTypeEntityFound;
        }
        throw new RuntimeException("AnalysisDocumentType not found");
    }

    @CacheEvict(value = "analysisDocumentTypes", allEntries = true)
    @Override
    public void executeDeleteById(Long id) {

        AnalysisDocumentTypeEntity analysisDocumentTypeEntityFound = executeReadAll()
                .stream()
                .filter(analysisDocumentTypeEntity -> analysisDocumentTypeEntity.getId().equals(id)).findFirst().orElse(null);

        if (!isNull(analysisDocumentTypeEntityFound)) {
            analysisDocumentTypeEntityFound.setIsDeleted(true);
            analysisDocumentTypeRepository.save(analysisDocumentTypeEntityFound);
        }
        throw new RuntimeException("AnalysisDocumentType not found");
    }

    @Cacheable(value = "analysisDocumentTypes")
    @Override
    public List<AnalysisDocumentTypeEntity> executeReadAll() {
        return analysisDocumentTypeRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "analysisDocumentType";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(AnalysisDocumentTypeEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(), catalogItem.getName(), catalogItem.getDescription());
    }

    @Override
    public AnalysisDocumentTypeEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new AnalysisDocumentTypeEntity(catalogDTO.getName(), catalogDTO.getDescription());
    }
}
