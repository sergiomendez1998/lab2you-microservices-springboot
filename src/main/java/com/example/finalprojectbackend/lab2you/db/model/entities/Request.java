package com.example.finalprojectbackend.lab2you.db.model.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import java.sql.Date;
import java.time.LocalDateTime;

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
    private String description;
    private Date receptionDate;
    private Long longitude;
    private Long latitude;
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn (name="support_type_id")
    private SupportType supportType;

    @ManyToOne
    @JoinColumn (name ="test_type_id")
    private TestType testType;

    @ManyToOne
    @JoinColumn (name="status_request_id")
    private StatusRequest statusRequest;

    @ManyToOne
    @JoinColumn (name="customer")
    private Customer customer;

    @PrePersist
    public void prePersist(){
        createdAt = LocalDateTime.now();
    }
}
