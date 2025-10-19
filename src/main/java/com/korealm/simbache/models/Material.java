package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Materials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MaterialId")
    private Long materialId;

    @Column(name = "MaterialName", nullable = false, unique = true)
    private String materialName;

    @Column(name = "Description", nullable = false, length = 2046)
    private String description;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Unit_FK")
    private MaterialUnit unit; // nullable
}