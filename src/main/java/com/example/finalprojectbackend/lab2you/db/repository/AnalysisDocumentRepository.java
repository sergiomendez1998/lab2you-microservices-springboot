package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisDocumentRepository extends JpaRepository<AnalysisDocumentEntity, Long> {
       List<AnalysisDocumentEntity> findAllByIsDeletedFalse();
       List<AnalysisDocumentEntity> findAllBySampleIdAndIsDeletedFalse(Long sampleId);
}
