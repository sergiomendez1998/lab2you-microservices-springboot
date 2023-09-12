package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;

import java.util.List;

public interface CatalogService <T> {

    T executeCreation(T entity);

    T executeUpdate(T entity);

    void executeDeleteById(Long id);

    List<T> executeReadAll();

    String getCatalogName();

    CatalogWrapper mapToCatalogWrapper(T catalogItem);
    T mapToCatalogEntity(CatalogDTO catalogDTO);
}
