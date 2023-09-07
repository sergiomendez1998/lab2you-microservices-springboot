package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.config.security.UserDetailsImpl;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailsService {


    private final UserService userService;

    public UserDetailServiceImpl(UserService userService) {
        this.userService = userService;
    }
    /*
    * This method is used to load the user by username.
    * It is used by the authentication manager to validate the user if exists in the record.
    *
    */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new UserDetailsImpl(userEntity);
    }
}
