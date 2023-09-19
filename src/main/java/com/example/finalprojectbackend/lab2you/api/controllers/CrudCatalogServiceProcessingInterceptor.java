package com.example.finalprojectbackend.lab2you.api.controllers;
import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;

public abstract class CrudCatalogServiceProcessingInterceptor<T> extends CrudServiceProcessingController<T>{

    public abstract String getCatalogName();
    public abstract CatalogWrapper mapToCatalogWrapper(T catalogItem);
    public abstract T mapToCatalogEntity(CatalogDTO catalogDTO);
}
