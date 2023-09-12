package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.Item;
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
public class ItemService implements CatalogService<Item> {
    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository){
        this.itemRepository=itemRepository;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public Item executeCreation(Item entity) {
        return itemRepository.save(entity);
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public Item executeUpdate(Item entity) {
        Item itemFound= executeReadAll()
                .stream()
                .filter(item -> item.getId().equals(entity.getId())).findFirst()
                .orElse((null));

        if (itemFound != null){
            itemFound
                    .setName(entity.getName()!=null?entity.getName(): itemFound.getName());
            itemFound.setDescription(entity.getDescription()!= null? entity.getDescription()
                :itemFound.getDescription());
        }
        return itemFound;
    }
    @CacheEvict(value = "items",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        Item itemFound = executeReadAll()
                .stream()
                .filter(item -> item.getId().equals((id))).findFirst().orElse(null);

        if (itemFound !=null){
            itemFound.setIsDeleted(true);
            itemRepository.save(itemFound);
        }
    }
    @Cacheable(value = "items")
    @Override
    public List<Item> executeReadAll() {
        return itemRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "item";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(Item catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public Item mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new Item(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
