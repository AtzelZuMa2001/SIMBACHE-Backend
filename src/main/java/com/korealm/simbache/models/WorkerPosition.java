package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "WorkerPosition")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class WorkerPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PositionId")
    private short positionId;

    @Column(name = "PositionName", nullable = false, unique = true, length = 128)
    private String positionName;
}
