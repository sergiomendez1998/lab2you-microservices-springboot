package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;

public interface Lab2YouProcessingController<T> {
    ResponseWrapper execute(T entity, String operation);
    ResponseWrapper validate(T entity, String operation);
}
