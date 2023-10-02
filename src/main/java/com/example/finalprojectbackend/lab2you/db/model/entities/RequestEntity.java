package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "request_detail",
            joinColumns =
            @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "exam_type_id", referencedColumnName = "id")
    )
    private List<ExamTypeEntity> examTypes = new ArrayList<>();
    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "request_status",
            joinColumns =
            @JoinColumn(name = "request_id", referencedColumnName = "id"),
            inverseJoinColumns =
            @JoinColumn(name = "status_id", referencedColumnName = "id")
    )
    private List<StatusEntity> statusEntities = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customer;

    @OneToMany(mappedBy = "requestEntity")
    private List<SampleEntity> samples = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        this.isDeleted = false;
        this.receptionDate = new Date(System.currentTimeMillis());
    }
}
