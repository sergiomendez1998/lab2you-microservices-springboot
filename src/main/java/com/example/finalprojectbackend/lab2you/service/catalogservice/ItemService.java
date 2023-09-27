package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.api.controllers.CrudCatalogServiceProcessingInterceptor;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("item")
public class ItemService extends CrudCatalogServiceProcessingInterceptor<ItemEntity> {
    private final ItemRepository itemRepository;
    private  ResponseWrapper responseWrapper;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ResponseWrapper executeCreation(ItemEntity entity) {
        itemRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Item created");
        responseWrapper.setData(Collections.singletonList("Item created"));
        return responseWrapper;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ResponseWrapper executeUpdate(ItemEntity itemEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ItemEntity> itemEntityFound = itemRepository.findById(itemEntity.getId());

        if (itemEntityFound.isPresent()) {
            itemEntityFound.get().setName(itemEntity.getName() != null ? itemEntity.getName() : itemEntityFound.get().getName());
            itemEntityFound.get().setDescription(itemEntity.getDescription() != null ? itemEntity.getDescription() : itemEntityFound.get().getDescription());
            itemRepository.save(itemEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Item updated");
            responseWrapper.setData(Collections.singletonList("Item updated"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Item not found");
        responseWrapper.addError("id", "Item not found");
        return responseWrapper;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(ItemEntity itemEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ItemEntity> analysisDocumentTypeEntityFound = itemRepository.findById(itemEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            itemRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Item deleted");
        responseWrapper.setData(Collections.singletonList("item deleted"));
        return responseWrapper;
    }
    @Cacheable(value = "items")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("items found");

        List<CatalogWrapper> catalogWrapperList = itemRepository.findAllByIsDeletedFalse()
                .stream()
                .map(this::mapToCatalogWrapper)
                .toList();

        responseWrapper.setData(catalogWrapperList);
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForCreation(ItemEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getName() ==null || entity.getName().isEmpty()) {
            responseWrapper.addError("nombre", "el nombre no puedo ser nullo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("descripcion", "la descripcion no puedo ser nullo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForUpdate(ItemEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }

        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForDelete(ItemEntity entity) {
        responseWrapper = new ResponseWrapper();
        if (entity.getId() == null || entity.getId() == 0) {
            responseWrapper.addError("id", "el id no puede ser nulo");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validating");
            responseWrapper.setData(new ArrayList<>());
            return responseWrapper;
        }
        return responseWrapper;
    }

    @Override
    protected ResponseWrapper validateForRead(ItemEntity entity) {
        return null;
    }

    @Override
    public String getCatalogName() {
        return "item";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(ItemEntity catalogItemEntity) {
        return new CatalogWrapper(catalogItemEntity.getId(), catalogItemEntity.getName(), catalogItemEntity.getDescription());
    }

    @Override
    public ItemEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new ItemEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
