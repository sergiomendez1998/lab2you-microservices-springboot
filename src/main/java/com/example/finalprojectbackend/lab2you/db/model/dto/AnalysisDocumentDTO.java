package com.example.finalprojectbackend.lab2you.db.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalysisDocumentDTO {

    private Long sampleId;
    private String resolution;
    private Long analysisDocumentTypeId;

    public AnalysisDocumentDTO(Long sampleId, String resolution, Long analysisDocumentTypeId) {
        this.sampleId = sampleId;
        this.resolution = resolution;
        this.analysisDocumentTypeId = analysisDocumentTypeId;
    }
}
