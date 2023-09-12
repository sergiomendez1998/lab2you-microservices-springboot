package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CRUDCatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@Qualifier("item")
public class ItemServiceCRUD implements CRUDCatalogService<ItemEntity> {
    private final ItemRepository itemRepository;

    public ItemServiceCRUD(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ItemEntity executeCreation(ItemEntity entity) {
        return itemRepository.save(entity);
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public ItemEntity executeUpdate(ItemEntity entity) {
        ItemEntity itemEntityFound = executeReadAll()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals(entity.getId())).findFirst()
                .orElse((null));

        if (!isNull(itemEntityFound)){
            itemEntityFound
                    .setName(entity.getName()!=null?entity.getName(): itemEntityFound.getName());
            itemEntityFound.setDescription(entity.getDescription()!= null? entity.getDescription()
                : itemEntityFound.getDescription());
            return itemEntityFound;
        }
        throw new RuntimeException("Item not found");
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        ItemEntity itemEntityFound = executeReadAll()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals((id))).findFirst().orElse(null);

        if (!isNull(itemEntityFound)){
            itemEntityFound.setIsDeleted(true);
            itemRepository.save(itemEntityFound);
        }
        throw new RuntimeException("Item not found");
    }
    @Cacheable(value = "items")
    @Override
    public List<ItemEntity> executeReadAll() {
        return itemRepository.findAllByIsDeletedFalse();
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
