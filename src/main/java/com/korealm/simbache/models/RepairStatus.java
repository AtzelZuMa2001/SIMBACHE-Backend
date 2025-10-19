package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "RepairStatus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RepairStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId")
    private short statusId;

    @Column(name = "StatusName", nullable = false, length = 64)
    private String statusName;
}