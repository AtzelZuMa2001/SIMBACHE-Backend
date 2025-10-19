package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "PotholeStatus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class PotholeStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId")
    private short statusId;

    @Column(name = "StatusName", nullable = false)
    private String statusName;
}
