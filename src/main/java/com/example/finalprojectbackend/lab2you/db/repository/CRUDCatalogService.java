package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;

import java.util.List;

public interface CRUDCatalogService<T> extends CRUDEntity<T>{

    String getCatalogName();

    CatalogWrapper mapToCatalogWrapper(T catalogItem);
    T mapToCatalogEntity(CatalogDTO catalogDTO);
}
