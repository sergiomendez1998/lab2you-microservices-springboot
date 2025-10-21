package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.CacheUtils;
import com.example.finalprojectbackend.lab2you.Lab2YouConstants;
import com.example.finalprojectbackend.lab2you.Lab2YouUtils;
import com.example.finalprojectbackend.lab2you.db.model.dto.CatalogDTO;
import com.example.finalprojectbackend.lab2you.db.model.entities.UserEntity;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.db.model.wrappers.ResponseWrapper;

import com.example.finalprojectbackend.lab2you.providers.CurrentUserProvider;
import jakarta.websocket.server.PathParam;
import org.springframework.cache.CacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogManagementProcessingController<T> {

    private final Map<String, CrudCatalogServiceProcessingInterceptor<T>> catalogServiceMap;
    private ResponseWrapper responseWrapper;

    private final CurrentUserProvider currentUserProvider;

    private final CacheManager cacheManager;


    public CatalogManagementProcessingController(List<CrudCatalogServiceProcessingInterceptor<T>> catalogServices,
            CurrentUserProvider currentUserProvider, CacheManager cacheManager) {
        catalogServiceMap = catalogServices.stream()
                .collect(Collectors.toMap(service -> service.getCatalogName().toLowerCase(), service -> service));
        this.currentUserProvider = currentUserProvider;
        this.cacheManager = cacheManager;
    }

    @GetMapping("/supportType/{userType}")
    public ResponseEntity<List<CatalogWrapper>> getSupportType(@PathVariable("userType") String userType) {
        CrudCatalogServiceProcessingInterceptor<T> catalogService = getCatalogService("supportType");
        List<CatalogWrapper> supportType = catalogService.executeReadAll().getData()
                .stream()
                .map(supportTypeEntity -> (CatalogWrapper) supportTypeEntity)
                .collect(Collectors.toList());

        supportType = supportType.stream()
                .filter(supportTypeEntity -> supportTypeEntity.getUserType().equalsIgnoreCase(userType))
                .collect(Collectors.toList());

        return ResponseEntity.ok(supportType);
    }

    @GetMapping()
    public ResponseEntity<ResponseWrapper> getAll(@PathParam("catalogType") String catalogType) {
        responseWrapper = new ResponseWrapper();
        if (catalogType.isEmpty()) {
            responseWrapper.setSuccessful(false);
            responseWrapper.setMessage("Catalogo no encontrado");
            responseWrapper.addError("Tipo de Catalogo", "el nombre del catalogo es requerido");
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        CrudCatalogServiceProcessingInterceptor<T> catalogService = getCatalogService(catalogType);
        responseWrapper = catalogService.executeReadAll();

        return ResponseEntity.ok(responseWrapper);
    }

    @PostMapping
    public ResponseEntity<ResponseWrapper> create(@PathParam("catalogType") String catalogType,
            @RequestBody CatalogDTO CatalogDTO) {

        if(catalogType.equalsIgnoreCase("item")){
             if(CatalogDTO.getExamTypeId() == null){
                 responseWrapper = new ResponseWrapper();
                 responseWrapper.setSuccessful(false);
                 responseWrapper.setMessage("Tipo de examen no esta presente");
                 responseWrapper.addError("Tipo de examen", "el tipo de examen es requerido para asociar el item");
                 return ResponseEntity.badRequest().body(responseWrapper);
             }
        }

        responseWrapper = new ResponseWrapper();
        CrudCatalogServiceProcessingInterceptor<T> catalogService = getCatalogService(catalogType);
        UserEntity userLogged = currentUserProvider.getCurrentUser();
        T catalogEntity = catalogService.mapToCatalogEntityForCreation(CatalogDTO, userLogged);
        responseWrapper = catalogService.validate(catalogEntity,
                Lab2YouConstants.operationTypes.CREATE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        responseWrapper = catalogService.execute(catalogEntity,
                Lab2YouConstants.operationTypes.CREATE.getOperationType());
        clearCache(catalogType);
        return ResponseEntity.ok(responseWrapper);
    }

    @PutMapping
    public ResponseEntity<ResponseWrapper> update(@PathParam("catalogType") String catalogType,
            @RequestBody CatalogDTO catalogDTO) {
        responseWrapper = new ResponseWrapper();
        CrudCatalogServiceProcessingInterceptor<T> catalogService = getCatalogService(catalogType);
        UserEntity userLogged = currentUserProvider.getCurrentUser();
        T catalogEntity = catalogService.mapToCatalogEntityForUpdate(catalogDTO, userLogged);
        responseWrapper = catalogService.validate(catalogEntity,
                Lab2YouConstants.operationTypes.UPDATE.getOperationType());

        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }

        ResponseWrapper updatedCatalogEntity = catalogService.execute(catalogEntity,
                Lab2YouConstants.operationTypes.UPDATE.getOperationType());
        clearCache(catalogType);
        return ResponseEntity.ok(updatedCatalogEntity);
    }

    @DeleteMapping
    public ResponseEntity<ResponseWrapper> delete(@PathParam("catalogType") String catalogType, String id) {
        responseWrapper = new ResponseWrapper();
        UserEntity userLogged = currentUserProvider.getCurrentUser();
        CrudCatalogServiceProcessingInterceptor<T> catalogService = getCatalogService(catalogType);
        CatalogDTO catalogDTO = new CatalogDTO();
        catalogDTO.setId(Long.parseLong(id));
        T catalogEntity = catalogService.mapToCatalogEntityForUpdate(catalogDTO, userLogged);
        responseWrapper = catalogService.validate(catalogEntity,
                Lab2YouConstants.operationTypes.DELETE.getOperationType());
        if (!responseWrapper.getErrors().isEmpty()) {
            return ResponseEntity.badRequest().body(responseWrapper);
        }
        responseWrapper = catalogService.execute(catalogEntity,
                Lab2YouConstants.operationTypes.DELETE.getOperationType());
        clearCache(catalogType);
        return ResponseEntity.ok(responseWrapper);
    }

    private CrudCatalogServiceProcessingInterceptor<T> getCatalogService(String catalogType) {
        return catalogServiceMap.get(catalogType.toLowerCase());
    }

    public void clearCache(String catalogType){
        CacheUtils cacheUtils = new CacheUtils(cacheManager);
        cacheUtils.clearCacheByName(Lab2YouUtils.pluralize(catalogType));
    }
}
