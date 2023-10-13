package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "log_header")
public class LogHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "record_id", nullable = false)
    private int recordId;

    @Column(name = "table_name", nullable = false)
    private String tableName;

    @Column(name = "operation_type", nullable = false)
    private String operationType;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "creation_date", nullable = false)
    private Date creationDate;

    @OneToMany(mappedBy = "header")
    private List<LogDetail> details;
}
