package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@Entity
@Table(name = "request_detail")
public class RequestDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "request_id")
    private RequestEntity request;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id")
    private ItemEntity item;
    @OneToMany(mappedBy = "requestDetail")
    private List<SampleItemEntity>  sampleItemEntities = new ArrayList<>();
    private LocalDate createdAt;
    private Boolean isDeleted;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDate.now();
        isDeleted = false;
    }
}
