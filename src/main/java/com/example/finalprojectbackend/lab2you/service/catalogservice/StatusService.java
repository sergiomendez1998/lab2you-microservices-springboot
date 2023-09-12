package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.Status;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("status")
public class StatusService implements CatalogService<Status> {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository){
        this.statusRepository = statusRepository;
    }

    @CacheEvict(value = "statuses", allEntries = true)
    @Override
    public Status executeCreation(Status entity) {
        return statusRepository.save(entity);
    }

    @CacheEvict(value = "statuses",allEntries = true)
    @Override
    public Status executeUpdate(Status entity) {
        Status statusFound = executeReadAll()
                .stream()
                .filter(status -> status.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (statusFound != null){
            statusFound
                    .setName(entity.getName() != null ? entity.getName() : statusFound.getName());
            statusFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : statusFound.getDescription());
            statusRepository.save(statusFound);
        }
        return statusFound;
    }

    @Override
    public void executeDeleteById(Long id) {
        Status statusFound = executeReadAll()
                .stream()
                .filter(status -> status.getId().equals(id)).findFirst().orElse(null);

        if(statusFound != null){
            statusFound.setIsDeleted(true);
            statusRepository.save(statusFound);
        }
    }

    @Cacheable(value = "statuses")
    @Override
    public List<Status> executeReadAll() {
        return statusRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "status";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(Status catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public Status mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new Status(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
