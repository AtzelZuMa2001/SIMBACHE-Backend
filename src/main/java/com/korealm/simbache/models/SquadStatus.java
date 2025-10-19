package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SquadStatus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SquadStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StatusId")
    private short statusId;

    @Column(name = "StatusName", nullable = false, unique = true)
    private String statusName;
}
