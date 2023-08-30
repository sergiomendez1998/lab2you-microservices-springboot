package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentType;
import com.example.finalprojectbackend.lab2you.db.model.entities.MeasureUnit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasureUnitRepository extends JpaRepository<MeasureUnit, Long> {
    List<MeasureUnit> findAllByIsActiveTrue();
}
