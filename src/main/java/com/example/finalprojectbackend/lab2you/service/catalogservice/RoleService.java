package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.RoleEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Qualifier("role")
public class RoleService implements CatalogService<RoleEntity> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public RoleEntity executeCreation(RoleEntity entity) {
        return roleRepository.save(entity);
    }

    @Override
    public RoleEntity executeUpdate(RoleEntity entity) {
        RoleEntity roleEntityFound = executeReadAll()
                .stream()
                .filter(role -> role.getId().equals(entity.getId())).findFirst()
                .orElse(null);
        if (roleEntityFound != null){
            roleEntityFound
                    .setName(entity.getName() != null ? entity.getName() : roleEntityFound.getName());
            roleEntityFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    : roleEntityFound.getDescription());
            roleRepository.save(roleEntityFound);
        }
        return roleEntityFound;
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        RoleEntity roleEntityFound = executeReadAll()
                .stream()
                .filter(role -> role.getId().equals(id)).findFirst().orElse(null);

        if (roleEntityFound != null){
            roleEntityFound.setIsDeleted(true);
            roleRepository.save(roleEntityFound);
        }

    }
    @Cacheable (value = "roles")
    @Override
    public List<RoleEntity> executeReadAll() {
        return roleRepository.findAllByIsDeletedFalse();
    }

    @Override
    public String getCatalogName() {
        return "role";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(RoleEntity catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public RoleEntity mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new RoleEntity(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
