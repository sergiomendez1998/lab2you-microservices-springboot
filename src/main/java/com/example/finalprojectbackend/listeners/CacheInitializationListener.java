package com.example.finalprojectbackend.listeners;

import com.example.finalprojectbackend.lab2you.service.catalogservice.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;


@Component
public class CacheInitializationListener implements ApplicationListener<ApplicationReadyEvent> {
    private final AnalysisDocumentTypeServiceProcessingInterceptorCrud analysisDocumentTypeService;
    private final DepartmentProcessingControllerServiceCrud departmentService;
    private final ItemProcessingControllerServiceCrud itemService;
    private final MeasureUniteServiceProcessingInterceptorCrud measureUnitService;
    private final RoleProcessingControllerServiceCrud roleService;
    private final SampleTypeServiceProcessingInterceptorCrud sampleTypeService;
    private final StatusProcessingControllerServiceCrud statusService;
    private final SupportTypeServiceProcessingInterceptorCrud supportTypeService;
    private final ExamTypeServiceProcessingInterceptorCrud examTypeService;

    public CacheInitializationListener(AnalysisDocumentTypeServiceProcessingInterceptorCrud analysisDocumentTypeService,
                                       DepartmentProcessingControllerServiceCrud departmentService,
                                       ItemProcessingControllerServiceCrud itemService,
                                       MeasureUniteServiceProcessingInterceptorCrud measureUniteService,
                                       RoleProcessingControllerServiceCrud roleService,
                                       SampleTypeServiceProcessingInterceptorCrud sampleTypeService,
                                       StatusProcessingControllerServiceCrud statusService,
                                       SupportTypeServiceProcessingInterceptorCrud supportTypeService,
                                       ExamTypeServiceProcessingInterceptorCrud examTypeService) {
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
