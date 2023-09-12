package com.example.finalprojectbackend.listeners;

import com.example.finalprojectbackend.lab2you.service.catalogservice.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class CacheInitializationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final AnalysisDocumentTypeService analysisDocumentTypeService;
    private final DepartmentService departmentService;
    private final ItemService itemService;
    private final MeasureUniteService measureUnitService;
    private final RoleService roleService;
    private final SampleTypeService sampleTypeService;
    private final StatusRequestService statusRequestService;
    private final SupportTypeService supportTypeService;
    private final TestTypeService testTypeService;

    public CacheInitializationListener(AnalysisDocumentTypeService analysisDocumentTypeService,
                                       DepartmentService departmentService,
                                       ItemService itemService,
                                       MeasureUniteService measureUniteService,
                                       RoleService roleService,
                                       SampleTypeService sampleTypeService,
                                       StatusRequestService statusRequestService,
                                       SupportTypeService supportTypeService,
                                       TestTypeService testTypeService) {
        this.analysisDocumentTypeService = analysisDocumentTypeService;
        this.departmentService = departmentService;
        this.itemService = itemService;
        this.measureUnitService = measureUniteService;
        this.roleService = roleService;
        this.sampleTypeService =sampleTypeService;
        this.statusRequestService = statusRequestService;
        this.supportTypeService = supportTypeService;
        this.testTypeService = testTypeService;
    }
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        analysisDocumentTypeService.executeReadAll();
        departmentService.executeReadAll();
        itemService.executeReadAll();
        measureUnitService.executeReadAll();
        roleService.executeReadAll();
        sampleTypeService.executeReadAll();
        statusRequestService.executeReadAll();
        supportTypeService.executeReadAll();
        testTypeService.executeReadAll();
    }
}
