package com.example.finalprojectbackend.lab2you.config.security;

import com.example.finalprojectbackend.lab2you.db.model.entities.Authority;
import com.example.finalprojectbackend.lab2you.db.model.entities.Module;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;

import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRole() != null ? userEntity.getRole().getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList()) : new ArrayList<SimpleGrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    @Override
    public String getUsername() {
        return userEntity.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.isEnabled();
    }

    public String getName() {
        return userEntity.getNickName();
    }

    public String getRole() {
        return userEntity.getRole().getName();
    }

    public List<Module> getModules() {
        return userEntity.getRole().getAuthorities().stream()
                .map(Authority::getModules)
                .flatMap(Collection::stream)
                .map(module -> {
                    Module newModule = new Module();
                    newModule.setId(module.getId());
                    newModule.setName(module.getName());
                    newModule.setDescription(module.getDescription());
                    newModule.setPath(module.getPath());
                    newModule.setIcon(module.getIcon());
                    return newModule;
                })
                .collect(Collectors.toList());
    }
}
