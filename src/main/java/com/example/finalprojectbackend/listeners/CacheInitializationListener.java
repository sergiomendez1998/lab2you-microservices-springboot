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
    private final StatusService statusService;
    private final SupportTypeService supportTypeService;
    private final ExamTypeService examTypeService;

    public CacheInitializationListener(AnalysisDocumentTypeService analysisDocumentTypeService,
                                       DepartmentService departmentService,
                                       ItemService itemService,
                                       MeasureUniteService measureUniteService,
                                       RoleService roleService,
                                       SampleTypeService sampleTypeService,
                                       StatusService statusService,
                                       SupportTypeService supportTypeService,
                                       ExamTypeService examTypeService) {
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
