package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.AssignmentEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface AssignmentRepository extends JpaRepository<AssignmentEntity, Long> {
    @Query("SELECT a FROM AssignmentEntity a WHERE a.request.id = :requestId ORDER BY a.AssignationDate DESC")
    List<AssignmentEntity> findAllByRequestId(@Param("requestId") Long requestId);
}
