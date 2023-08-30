package com.example.finalprojectbackend.listeners;

import com.example.finalprojectbackend.lab2you.service.catalogservice.AnalysisDocumentTypeService;
import com.example.finalprojectbackend.lab2you.service.catalogservice.DepartmentService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class CacheInitializationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final AnalysisDocumentTypeService analysisDocumentTypeService;
    private final DepartmentService departmentService;

    public CacheInitializationListener(AnalysisDocumentTypeService analysisDocumentTypeService, DepartmentService departmentService) {
        this.analysisDocumentTypeService = analysisDocumentTypeService;
        this.departmentService = departmentService;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        analysisDocumentTypeService.executeReadAll();
        departmentService.executeReadAll();
    }

}
