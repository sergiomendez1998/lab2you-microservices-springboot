package com.example.finalprojectbackend.lab2you.service;


import com.example.finalprojectbackend.lab2you.db.model.entities.Role;
import com.example.finalprojectbackend.lab2you.db.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {

        this.roleRepository = roleRepository;
    }

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

}
