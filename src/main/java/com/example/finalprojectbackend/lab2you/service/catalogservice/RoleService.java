package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.Role;
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
public class RoleService implements CatalogService<Role> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository){
        this.roleRepository = roleRepository;
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public Role executeCreation(Role entity) {
        return roleRepository.save(entity);
    }

    @Override
    public Role executeUpdate(Role entity) {
        Role roleFound = executeReadAll()
                .stream()
                .filter(role -> role.getId().equals(entity.getId())).findFirst()
                .orElse(null);
        if (roleFound != null){
            roleFound
                    .setName(entity.getName() != null ? entity.getName() : roleFound.getName());
            roleFound.setDescription(entity.getDescription() != null ? entity.getDescription()
                    :roleFound.getDescription());
            roleRepository.save(roleFound);
        }
        return roleFound;
    }

    @CacheEvict(value = "roles",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        Role roleFound = executeReadAll()
                .stream()
                .filter(role -> role.getId().equals(id)).findFirst().orElse(null);

        if (roleFound != null){
            roleFound.setIsActive(false);
            roleRepository.save(roleFound);
        }

    }
    @Cacheable (value = "roles")
    @Override
    public List<Role> executeReadAll() {
        return roleRepository.findAllByIsActiveTrue();
    }

    @Override
    public String getCatalogName() {
        return "role";
    }

    @Override
    public CatalogWrapper mapToCatalogWrapper(Role catalogItem) {
        return new CatalogWrapper(catalogItem.getId(),catalogItem.getName(),catalogItem.getDescription());
    }

    @Override
    public Role mapToCatalogEntity(CatalogDTO catalogDTO) {
        return new Role(catalogDTO.getName(),catalogDTO.getDescription());
    }
}
