package com.example.finalprojectbackend.lab2you.db.model.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Entity
@Table(name="sample_item")
public class SampleItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="sample_id")
    private SampleEntity sample;

    @ManyToOne
    @JoinColumn(name="item_id")
    private ItemEntity item;

    private boolean isDeleted;
}
