package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AnalysisDocumentType;
import com.example.finalprojectbackend.lab2you.db.model.entities.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByName(String name);
    List<Authority> findAllByIsActiveTrue();
}
