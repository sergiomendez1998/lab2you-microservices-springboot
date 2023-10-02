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
@Table(name = "statuses")
public class StatusEntity extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToMany(mappedBy = "statusEntities", fetch = FetchType.LAZY)
    private List<RequestEntity> requests = new ArrayList<>();

    public StatusEntity(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
