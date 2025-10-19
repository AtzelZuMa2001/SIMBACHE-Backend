package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "RepairUseTransportation")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairUseTransportation {

    @EmbeddedId
    private RepairUseTransportationId id;

    @ManyToOne(optional = false)
    @MapsId("repairId")
    @JoinColumn(name = "RepairId", nullable = false)
    private Repair repair;

    @ManyToOne(optional = false)
    @MapsId("transportationId")
    @JoinColumn(name = "TransportationId", nullable = false)
    private Transportation transportation;

    @Column(name = "AssignedDate", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "ReleasedDate", nullable = false)
    private LocalDate releasedDate;
}
