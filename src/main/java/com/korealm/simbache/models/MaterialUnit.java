package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "MaterialUnits")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialUnit {

    @Id
    @Column(name = "UnitId")
    private Short unitId; // assigned (no identity)

    @Column(name = "UnitName", nullable = false, unique = true, length = 128)
    private String unitName;
}