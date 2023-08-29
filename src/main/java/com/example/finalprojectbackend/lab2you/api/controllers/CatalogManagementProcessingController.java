package com.example.finalprojectbackend.lab2you.api.controllers;

import com.example.finalprojectbackend.lab2you.db.model.wrappers.CatalogWrapper;
import com.example.finalprojectbackend.lab2you.service.catalogservice.AnalysisDocumentTypeService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogManagementProcessingController {

    private final AnalysisDocumentTypeService analysisDocumentTypeService;

    @Autowired
    public CatalogManagementProcessingController(AnalysisDocumentTypeService analysisDocumentTypeService) {
        this.analysisDocumentTypeService = analysisDocumentTypeService;
    }

    @RequestMapping("/analysisDocumentTypes")
    @GetMapping()
    public ResponseEntity<List<CatalogWrapper>> getAnalysisDocumentTypes() {

        List<CatalogWrapper> catalogWrapperList = analysisDocumentTypeService.executeReadAll().stream()
                .map(analysisDocumentType -> new CatalogWrapper(
                        analysisDocumentType.getId(),
                        analysisDocumentType.getName(),
                        analysisDocumentType.getDescription()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(catalogWrapperList);
    }

}
