package com.example.finalprojectbackend.lab2you.db.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor
@Entity
@Table(name = "items")
public class ItemEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "item")
    private List<SampleItemEntity> sampleItemEntities = new ArrayList<>();

    public ItemEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }

}
