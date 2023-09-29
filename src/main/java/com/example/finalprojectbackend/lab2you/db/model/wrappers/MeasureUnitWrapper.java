package com.example.finalprojectbackend.lab2you.db.model.wrappers;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MeasureUnitWrapper {

        private Long id;
        private String name;
        private String description;

        public MeasureUnitWrapper(Long id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

}
