package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.RequestDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface  RequestDetailRepository extends JpaRepository<RequestDetailEntity, Long> {
}
