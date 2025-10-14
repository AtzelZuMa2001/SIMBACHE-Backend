package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VehicleType")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short typeId;

    @Column(name = "TypeName", nullable = false)
    private String typeName;
}
