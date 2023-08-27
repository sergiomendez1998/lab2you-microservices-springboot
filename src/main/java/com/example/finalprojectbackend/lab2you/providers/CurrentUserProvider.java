package com.example.finalprojectbackend.lab2you.providers;

import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;

public interface CurrentUserProvider {
    UserEntity getCurrentUser();
}
