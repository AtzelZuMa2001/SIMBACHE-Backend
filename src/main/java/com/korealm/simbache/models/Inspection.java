package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "InspectionId")
    private Long inspectionId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Pothole_FK", nullable = false)
    private Pothole pothole;

    @ManyToOne(optional = true)
    @JoinColumn(name = "RepairId_FK")
    private Repair repair; // nullable per schema

    @ManyToOne(optional = false)
    @JoinColumn(name = "Inspector_FK", nullable = false)
    private User inspector;

    @Column(name = "InspectionDate", nullable = false)
    private LocalDateTime inspectionDate;

    @Column(name = "Notes", nullable = false, length = 4096)
    private String notes;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Status_FK", nullable = false)
    private InspectionStatus status;

    @Column(name = "PhotoUrl", nullable = false, length = 4096)
    private String photoUrl;

    @Column(name = "IsActive", nullable = false)
    private boolean isActive = true;
}
