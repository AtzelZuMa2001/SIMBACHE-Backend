package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Transportation")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Transportation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TransportationId")
    private Long transportationId;

    @Column(name = "PlateNumber", length = 32)
    private String plateNumber;

    @ManyToOne(optional = true)
    @JoinColumn(name = "VehicleType_FK")
    private VehicleType vehicleType;

    @ManyToOne(optional = true)
    @JoinColumn(name = "VehicleStatus_FK")
    private VehicleStatus vehicleStatus;
}