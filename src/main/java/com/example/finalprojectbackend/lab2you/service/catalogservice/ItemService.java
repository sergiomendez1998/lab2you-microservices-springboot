package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
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

    @Override
    public ResponseWrapper executeCreation(ItemEntity entity) {
        itemRepository.save(entity);
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Item creado");
        responseWrapper.setData(Collections.singletonList("Item creado"));
        return responseWrapper;
    }

    @Override
    public ResponseWrapper executeUpdate(ItemEntity itemEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ItemEntity> itemEntityFound = itemRepository.findById(itemEntity.getId());

        if (itemEntityFound.isPresent()) {
            itemEntityFound.get().setName(itemEntity.getName() != null ? itemEntity.getName() : itemEntityFound.get().getName());
            itemEntityFound.get().setDescription(itemEntity.getDescription() != null ? itemEntity.getDescription() : itemEntityFound.get().getDescription());
            itemEntityFound.get().setUpdatedBy(itemEntity.getUpdatedBy());
            itemRepository.save(itemEntityFound.get());

            responseWrapper.setSuccessful(true);
            responseWrapper.setMessage("Item actualizado");
            responseWrapper.setData(Collections.singletonList("Item actualizado"));
            return responseWrapper;
        }

        responseWrapper.setSuccessful(false);
        responseWrapper.setMessage("Item no encontrado");
        responseWrapper.addError("id", "Item no encontrado");
        return responseWrapper;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ResponseWrapper executeDeleteById(ItemEntity itemEntity) {
        responseWrapper = new ResponseWrapper();
        Optional<ItemEntity> analysisDocumentTypeEntityFound = itemRepository.findById(itemEntity.getId());

        analysisDocumentTypeEntityFound.ifPresent(analysisDocumentTypeEntity -> {
            analysisDocumentTypeEntity.setIsDeleted(true);
            analysisDocumentTypeEntity.setUpdatedBy(itemEntity.getUpdatedBy());
            itemRepository.save(analysisDocumentTypeEntity);
        });

        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("Item eliminado");
        responseWrapper.setData(Collections.singletonList("item eliminado"));
        return responseWrapper;
    }
    @Cacheable(value = "items")
    @Override
    public ResponseWrapper executeReadAll() {
        responseWrapper = new ResponseWrapper();
        responseWrapper.setSuccessful(true);
        responseWrapper.setMessage("items encontrados");

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
            responseWrapper.addError("name", "el nombre no puedo ser nulo o vacio");
        }

        if (entity.getDescription() ==null || entity.getDescription().isEmpty()) {
            responseWrapper.addError("description", "la descripcion no puedo ser nulo o vacio");
        }

        if (responseWrapper.getErrors() != null && !responseWrapper.getErrors().isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Error validando");
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
            responseWrapper.setMessage("Error validando");
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
            responseWrapper.setMessage("Error validando");
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
    public ItemEntity mapToCatalogEntityForCreation(CatalogDTO catalogDTO, UserEntity userLogged) {
        ItemEntity itemEntity = new ItemEntity(catalogDTO.getName(),catalogDTO.getDescription());
        itemEntity.setCreatedBy(userLogged);
        return itemEntity;
    }

    @Override
    public ItemEntity mapToCatalogEntityForUpdate(CatalogDTO catalogDTO, UserEntity userLogged) {
        ItemEntity itemEntity = new ItemEntity();
        itemEntity.setId(catalogDTO.getId());
        itemEntity.setName(catalogDTO.getName());
        itemEntity.setDescription(catalogDTO.getDescription());
        itemEntity.setUpdatedBy(userLogged);
        return itemEntity;
    }

    public ItemEntity findById(Long id) {
        return executeReadAll().getData()
                .stream()
                .filter(item -> item instanceof CatalogWrapper)
                .map(catalogWrapper -> (CatalogWrapper) catalogWrapper)
                .filter(catalogWrapper -> catalogWrapper.getId().equals(id))
                .findFirst()
                .map(catalogWrapper -> {
                    ItemEntity entity = new ItemEntity();
                    entity.setId(catalogWrapper.getId());
                    entity.setName(catalogWrapper.getName());
                    entity.setDescription(catalogWrapper.getDescription());
                    return entity;
                })
                .orElse(new ItemEntity());
    }
}
