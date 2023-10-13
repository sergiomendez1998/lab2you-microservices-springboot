package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisDocumentTypeRepository extends JpaRepository<AnalysisDocumentTypeEntity, Long> {

    List<AnalysisDocumentTypeEntity> findAllByIsDeletedFalse();
}
