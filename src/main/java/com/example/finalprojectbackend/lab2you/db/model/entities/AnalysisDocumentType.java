package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Table(name = "analysis_document_types")
@NoArgsConstructor
public class AnalysisDocumentType extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    public AnalysisDocumentType(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
