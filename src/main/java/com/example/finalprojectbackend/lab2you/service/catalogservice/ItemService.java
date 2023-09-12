package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.ItemEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("item")
public class ItemService implements CatalogService<ItemEntity> {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
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

        if (itemEntityFound != null){
            itemEntityFound
                    .setName(entity.getName()!=null?entity.getName(): itemEntityFound.getName());
            itemEntityFound.setDescription(entity.getDescription()!= null? entity.getDescription()
                : itemEntityFound.getDescription());
        }
        return itemEntityFound;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        ItemEntity itemEntityFound = executeReadAll()
                .stream()
                .filter(itemEntity -> itemEntity.getId().equals((id))).findFirst().orElse(null);

        if (itemEntityFound !=null){
            itemEntityFound.setIsDeleted(true);
            itemRepository.save(itemEntityFound);
        }
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
