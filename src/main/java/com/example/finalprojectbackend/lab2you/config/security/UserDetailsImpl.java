package com.example.finalprojectbackend.lab2you.config.security;

import com.example.finalprojectbackend.lab2you.db.model.entities.*;

import com.example.finalprojectbackend.lab2you.db.model.wrappers.AuthorityWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ModuleWrapper;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
public class UserDetailsImpl implements UserDetails {
    private final UserEntity userEntity;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRole() != null ? userEntity.getRole().getAuthorities().stream()
                .map(authorityEntity -> new SimpleGrantedAuthority(authorityEntity.getName()))
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
    public String getUserType() {
        return userEntity.getUserType();
    }

    public String getNit() {
       if (userEntity.getCustomer() != null) {
           if(getUserType().equals("externo")){
               return userEntity.getCustomer().getNit();
           }else {
               return "";
           }
       } else {
           return "";
       }
    }
    public String getRole() {
        return userEntity.getRole().getName();
    }
    public Long getUserId() {
        return userEntity.getId();
    }
    public List<AuthorityWrapper> getAuthoritiesList() {
        return userEntity.getRole() != null ? userEntity.getRole().getAuthorities().stream()
                .map(authorityEntity -> new AuthorityWrapper(authorityEntity.getId(), authorityEntity.getName(), authorityEntity.getDescription()))
                .collect(Collectors.toList()) : new ArrayList<AuthorityWrapper>();
    }
    public List<ModuleWrapper> getModules() {
        List<ModuleEntity> modules = new ArrayList<>();
        if (userEntity.getRole() != null) {
            for (AuthorityEntity authority : userEntity.getRole().getAuthorities()) {
                for (ModuleEntity module : authority.getModuleAuthorities().stream().map(ModuleAuthority::getModuleEntity).toList()) {
                    if (!modules.contains(module)) {
                        modules.add(module);
                    }
                }
            }
        }
        return modules.stream()
                .map(module -> new ModuleWrapper(module.getId(), module.getName(), module.getDescription(), module.getPath(), module.getIcon()))
                .collect(Collectors.toList());
    }
}
