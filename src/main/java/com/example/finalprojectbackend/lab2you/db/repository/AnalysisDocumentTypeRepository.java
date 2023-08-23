package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnalysisDocumentTypeRepository extends JpaRepository<AnalysisDocumentType, Long> {

    List<AnalysisDocumentType> findAllByIsActiveTrue();
}
