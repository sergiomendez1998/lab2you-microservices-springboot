package com.example.finalprojectbackend.lab2you.service;

import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.repository.UserRepository;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @ReadOnlyProperty
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    // public List<UserWrapper> executedReadAll() {
    // return userRepository.findAllByEnabled(true)
    // .stream()
    // .map(user -> new UserWrapper(user, user.getRoles()))
    // .toList();
    // }
    // public UserEntity findByCui(Long cui){
    // return userRepository.findByCui(cui);
    // }

}
