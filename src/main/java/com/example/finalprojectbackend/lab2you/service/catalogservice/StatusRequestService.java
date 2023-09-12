package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.StatusRequest;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.StatusRequestRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("statusRequest")
public class StatusRequestService implements CatalogService<StatusRequest> {

    private final StatusRequestRepository statusRequestRepository;

    public StatusRequestService(StatusRequestRepository statusRequestRepository){
        this.statusRequestRepository = statusRequestRepository;
    }

    @CacheEvict(value = "statusRequest", allEntries = true)
    @Override
    public StatusRequest executeCreation(StatusRequest entity) {
        return statusRequestRepository.save(entity);
    }

    @CacheEvict(value = "statusRequest",allEntries = true)
    @Override
    public StatusRequest executeUpdate(StatusRequest entity) {
        StatusRequest statusRequestFound = executeReadAll()
                .stream()
                .filter(statusRequest -> statusRequest.getId().equals(entity.getId())).findFirst()
                .orElse(null);

        if (statusRequestFound != null){
            statusRequestFound
                    .setName(entity.getName() != null ? entity.getName() : statusRequestFound.getName());
            statusRequestFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : statusRequestFound.getDescription());
            statusRequestRepository.save(statusRequestFound);
        }
        return statusRequestFound;
    }

    @Override
    public void executeDeleteById(Long id) {
        StatusRequest statusRequestFound = executeReadAll()
                .stream()
                .filter(statusRequest -> statusRequest.getId().equals(id)).findFirst().orElse(null);

        if(statusRequestFound != null){
            statusRequestFound.setIsActive(false);
            statusRequestRepository.save(statusRequestFound);
        }
    }

    @Cacheable(value = "statusRequests")
    @Override
    public List<StatusRequest> executeReadAll() {
        return statusRequestRepository.findAllByIsActiveTrue();
    }

    @Override
    public String getCatalogName() {
        return "statusRequest";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(StatusRequest catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public StatusRequest mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new StatusRequest(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
