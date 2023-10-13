package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name="samples")
public class SampleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String presentation;
    private Long quantity;
    private String label;
    private LocalDateTime createdAt;
    private Date expirationDate;
    private boolean isDeleted;

    @ManyToOne
    @JoinColumn (name="measure_unit_id")
    private MeasureUnitEntity measureUnitEntity;
    @ManyToOne
    @JoinColumn (name="sample_type_id")
    private SampleTypeEntity sampleTypeEntity;

    @ManyToOne
    @JoinColumn (name = "request_detail_id")
    private RequestDetailEntity requestDetail;

    @OneToMany(mappedBy = "sample")
    private List<AnalysisDocumentEntity> analysisDocumentEntities = new ArrayList<>();
    @OneToMany(mappedBy = "sample")
    private List<SampleItemEntity> sampleItemEntities = new ArrayList<>();
}
