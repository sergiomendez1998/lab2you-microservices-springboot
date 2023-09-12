package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnalysisDocumentRepository extends JpaRepository<AnalysisDocumentTypeEntity, Long> {

}
