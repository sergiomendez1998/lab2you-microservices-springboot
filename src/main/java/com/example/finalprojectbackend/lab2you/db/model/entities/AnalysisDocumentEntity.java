package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "analysis_documents")
public class AnalysisDocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String documentCode;
    private String resolution;
    private String path;
    private Boolean isDeleted;

    @ManyToOne
    @JoinColumn (name = "sample_id")
    private SampleEntity sample;

    @ManyToOne
    @JoinColumn (name = "analysis_document_type_id")
    private AnalysisDocumentTypeEntity analysisDocumentType;
    private LocalDateTime createdAt;
}
