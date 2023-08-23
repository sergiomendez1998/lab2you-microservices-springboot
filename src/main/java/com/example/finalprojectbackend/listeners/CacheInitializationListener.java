package com.example.finalprojectbackend.listeners;

import com.example.finalprojectbackend.lab2you.service.catalogservice.AnalysisDocumentTypeService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class CacheInitializationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final AnalysisDocumentTypeService analysisDocumentTypeService;

    public CacheInitializationListener(AnalysisDocumentTypeService analysisDocumentTypeService) {
        this.analysisDocumentTypeService = analysisDocumentTypeService;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        analysisDocumentTypeService.executeReadAll();
    }
}
