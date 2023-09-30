package com.example.finalprojectbackend.lab2you.db.repository;

import com.example.finalprojectbackend.lab2you.db.model.entities.RequestEntity;
import com.example.finalprojectbackend.lab2you.db.model.entities.StatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
       List<RequestEntity> findAllByIsDeletedFalse();
       RequestEntity findByRequestCode(String requestCode);
       @Query("SELECT r.statusEntities FROM RequestEntity r WHERE r.id = :requestId")
       List<StatusEntity> findStatusesByRequestId(@Param("requestId") Long requestId);
}
