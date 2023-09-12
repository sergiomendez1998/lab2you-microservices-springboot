package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.repository.CatalogService;

import jakarta.websocket.server.PathParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogManagementProcessingController<T> {

    private final Map<String, CatalogService<T>> catalogServiceMap;


    public CatalogManagementProcessingController(List<CatalogService<T>> catalogServices) {
        catalogServiceMap = catalogServices.stream()
                .collect(Collectors.toMap(service -> service.getCatalogName().toLowerCase(), service -> service));
    }

    @GetMapping()
    public ResponseEntity<List<CatalogWrapper>> getAll(@PathParam("catalogType") String catalogType) {
        CatalogService<T> catalogService = getCatalogService(catalogType);
        List<T> catalogItems = catalogService.executeReadAll();
        List<CatalogWrapper> catalogWrapperList = catalogItems.stream()
                .map(catalogService::mapToCatalogWrapper)
                .collect(Collectors.toList());
        return ResponseEntity.ok(catalogWrapperList);
    }

    @PostMapping()
    public ResponseEntity<CatalogWrapper> create(@PathParam("catalogType") String catalogType,
                                                 @RequestBody CatalogDTO CatalogDTO) {
        CatalogService<T> catalogService = getCatalogService(catalogType);
        T catalogEntity = catalogService.mapToCatalogEntity(CatalogDTO);
        T createdCatalogEntity = catalogService.executeCreation(catalogEntity);
        CatalogWrapper createdCatalogWrapper = catalogService.mapToCatalogWrapper(createdCatalogEntity);
        return ResponseEntity.ok(createdCatalogWrapper);
    }

    @PutMapping()
    public ResponseEntity<CatalogWrapper> update(@PathParam("catalogType") String catalogType,
                                                 @RequestBody CatalogDTO CatalogDTO) {
        CatalogService<T> catalogService = getCatalogService(catalogType);
        T catalogEntity = catalogService.mapToCatalogEntity(CatalogDTO);
        T updatedCatalogEntity = catalogService.executeUpdate(catalogEntity);
        CatalogWrapper updatedCatalogWrapper = catalogService.mapToCatalogWrapper(updatedCatalogEntity);
        return ResponseEntity.ok(updatedCatalogWrapper);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathParam("catalogType") String catalogType,
                                       @PathVariable("id") Long id) {
        CatalogService<T> catalogService = getCatalogService(catalogType);
        catalogService.executeDeleteById(id);
        return ResponseEntity.ok("Catalog item with id " + id + " was deleted");
    }

    private CatalogService<T> getCatalogService(String catalogType) {
        return catalogServiceMap.get(catalogType.toLowerCase());
    }

}
