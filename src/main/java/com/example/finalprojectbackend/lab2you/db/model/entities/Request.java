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

@Entity
@Table(name="requests")
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
public class Request {

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
    private SupportType supportType;

    @ManyToOne
    @JoinColumn (name ="exam_type_id")
    private ExamType examType;

    @ManyToMany(mappedBy = "requests")
    private List<Status> statuses = new ArrayList<>();

    @ManyToOne
    @JoinColumn (name="customer_id")
    private Customer customer;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
