package com.example.finalprojectbackend.listeners;

import com.example.finalprojectbackend.lab2you.service.catalogservice.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class CacheInitializationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final AnalysisDocumentTypeServiceCRUD analysisDocumentTypeService;
    private final DepartmentServiceCRUD departmentService;
    private final ItemServiceCRUD itemService;
    private final MeasureUniteServiceCRUD measureUnitService;
    private final RoleServiceCRUD roleService;
    private final SampleTypeServiceCRUD sampleTypeService;
    private final StatusServiceCRUD statusService;
    private final SupportTypeServiceCRUD supportTypeService;
    private final ExamTypeServiceCRUD examTypeService;

    public CacheInitializationListener(AnalysisDocumentTypeServiceCRUD analysisDocumentTypeService,
                                       DepartmentServiceCRUD departmentService,
                                       ItemServiceCRUD itemService,
                                       MeasureUniteServiceCRUD measureUniteService,
                                       RoleServiceCRUD roleService,
                                       SampleTypeServiceCRUD sampleTypeService,
                                       StatusServiceCRUD statusService,
                                       SupportTypeServiceCRUD supportTypeService,
                                       ExamTypeServiceCRUD examTypeService) {
        this.analysisDocumentTypeService = analysisDocumentTypeService;
        this.departmentService = departmentService;
        this.itemService = itemService;
        this.measureUnitService = measureUniteService;
        this.roleService = roleService;
        this.sampleTypeService =sampleTypeService;
        this.statusService = statusService;
        this.supportTypeService = supportTypeService;
        this.examTypeService = examTypeService;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        analysisDocumentTypeService.executeReadAll();
        departmentService.executeReadAll();
        itemService.executeReadAll();
        measureUnitService.executeReadAll();
        roleService.executeReadAll();
        sampleTypeService.executeReadAll();
        statusService.executeReadAll();
        supportTypeService.executeReadAll();
        examTypeService.executeReadAll();
    }
}
