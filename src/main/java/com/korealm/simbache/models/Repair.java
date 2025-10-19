package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "Repairs")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Repair {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RepairId")
    private Long repairId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Pothole_FK", nullable = false)
    private Pothole pothole;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Contractor_FK", nullable = false)
    private Contractor contractor;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Status_FK", nullable = false)
    private RepairStatus status;

    @Column(name = "Budget", precision = 11, scale = 3, nullable = false)
    private BigDecimal budget;

    @ManyToOne(optional = true)
    @JoinColumn(name = "AssignedTransport_FK")
    private Transportation assignedTransport;

    @Column(name = "isActive", nullable = false)
    private boolean isActive = true;
}
