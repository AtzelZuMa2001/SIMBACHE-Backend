package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "VehicleStatus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class VehicleStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId")
    private short statusId;

    @Column(name = "StatusName", nullable = false, unique = true, length = 64)
    private String statusName;
}