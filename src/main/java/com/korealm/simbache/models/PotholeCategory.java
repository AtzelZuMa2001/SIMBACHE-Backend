package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PotholeCategory")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PotholeCategory {

    @Id
    @Column(name = "CategoryId")
    private Short categoryId; // not identity per schema (assigned)

    @Column(name = "Name", nullable = false, unique = true)
    private String name;

    @Column(name = "Description", nullable = false, length = 512)
    private String description;

    @Column(name = "PriorityLevel", nullable = false)
    private int priorityLevel;
}
