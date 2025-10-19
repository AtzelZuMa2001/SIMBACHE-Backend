package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "RepairSquads")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepairSquad {

    @Id
    @Column(name = "RepairId")
    private Long repairId;

    @OneToOne(optional = false)
    @MapsId
    @JoinColumn(name = "RepairId", nullable = false)
    private Repair repair;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SquadId", nullable = false)
    private Squad squad;

    @Column(name = "AssignedDate", nullable = false)
    private LocalDate assignedDate;

    @Column(name = "ReleasedDate")
    private LocalDate releasedDate;
}
