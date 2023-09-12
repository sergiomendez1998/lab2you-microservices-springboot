package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name="requests")
public class RequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String supportNumber;
    private String remark;
    private Date receptionDate;
    private Long longitude;
    private Long latitude;
    private LocalDateTime createdAt;
    private boolean isDeleted;
    @ManyToOne
    @JoinColumn (name="support_type_id")
    private SupportTypeEntity supportType;

    @ManyToOne
    @JoinColumn (name ="exam_type_id")
    private ExamTypeEntity examType;

    @ManyToMany(mappedBy = "requests")
    private List<StatusEntity> statusEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn (name="customer_id")
    private CustomerEntity customer;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
