package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.Role;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import com.example.finalprojectbackend.lab2you.db.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService implements CatalogService<Role> {

    private final RoleRepository roleRepository;

    @Autowired
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
}
