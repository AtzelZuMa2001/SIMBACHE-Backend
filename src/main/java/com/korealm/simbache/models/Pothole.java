package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Potholes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Pothole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PotholeId")
    private Long potholeId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "ReportedBy_FK", nullable = false)
    private User reportedBy;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Location_FK", nullable = false)
    private Location location;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Category_FK", nullable = false)
    private PotholeCategory category;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Status_FK", nullable = false)
    private PotholeStatus status;

    @Column(name = "PhotoUrl", length = 4096)
    private String photoUrl;

    @Column(name = "DateReported", nullable = false)
    private LocalDateTime dateReported;

    @Column(name = "DateValidated")
    private LocalDateTime dateValidated;

    @Column(name = "DateClosed")
    private LocalDateTime dateClosed;

    @Column(name = "isActive", nullable = false)
    private boolean isActive = true;
}
