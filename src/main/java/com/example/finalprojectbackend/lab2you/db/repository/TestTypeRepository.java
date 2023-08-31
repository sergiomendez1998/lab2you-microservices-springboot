package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.TestType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestTypeRepository extends JpaRepository<TestType, Long> {
    List<TestType> findAllByIsActiveTrue();
}
