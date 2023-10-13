package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name = "requests")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String requestCode;
    private String supportNumber;
    private String email;
    private String remark;
    private Date receptionDate;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn(name = "support_type_id")
    private SupportTypeEntity supportType;

    @OneToMany(mappedBy = "request")
    private List<RequestDetailEntity> requestDetails = new ArrayList<>();

    @OneToMany(mappedBy = "request")
    private List<RequestStatusEntity> requestStatuses = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @PrePersist
    public void prePersist() {
        this.isDeleted = false;
        this.receptionDate = new Date(System.currentTimeMillis());
    }

    public void addExamType(ExamTypeEntity examTypeEntity) {
        RequestDetailEntity requestDetailEntity = new RequestDetailEntity();
        requestDetailEntity.setExamType(examTypeEntity);
        requestDetailEntity.setRequest(this);
        this.requestDetails.add(requestDetailEntity);
    }
}
