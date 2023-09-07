package com.example.finalprojectbackend.lab2you.service.catalogservice;

import com.example.finalprojectbackend.lab2you.db.model.entities.Authority;
import com.example.finalprojectbackend.lab2you.db.repository.AuthorityRepository;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AuthorityService implements CatalogService<Authority> {
    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository){
        this.authorityRepository = authorityRepository;
    }
    @CacheEvict (value = "authorities",allEntries = true)
    @Override
    public Authority executeCreation(Authority entity) {
        return authorityRepository.save(entity);
    }
    @CacheEvict(value = "authorities",allEntries = true)
    @Override
    public Authority executeUpdate(Authority entity) {
        Authority authorityFound = executeReadAll()
                .stream()
                .filter(authority -> authority.getId().equals(entity.getId())).findFirst()
                .orElse(null);
        if (authorityFound != null){
            authorityFound
                    .setName(entity.getName()!=null? entity.getName() : authorityFound.getName());
            authorityFound.setDescription(entity.getDescription()!= null ? entity.getDescription()
                    :authorityFound.getDescription());
            authorityRepository.save(authorityFound);
        }
        return authorityFound;
    }
    @CacheEvict(value = "authorities",allEntries = true)
    @Override
    public void executeDeleteById(Long id) {
        Authority authorityFound =executeReadAll()
                .stream()
                .filter(authority -> authority.getId().equals(id)).findFirst().orElse(null);

        if (authorityFound !=null){
            authorityFound.setIsActive((false));
            authorityRepository.save(authorityFound);
        }


    }
    @Cacheable(value = "authorities")
    @Override
    public List<Authority> executeReadAll() {
        return authorityRepository.findAllByIsActiveTrue();
    }
}
