package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Squad")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Squad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SquadId")
    private Integer squadId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Contractor_FK", nullable = false)
    private Contractor contractor;

    @Column(name = "SquadName", nullable = false, unique = true, length = 128)
    private String squadName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Status_FK", nullable = false)
    private SquadStatus status;

    @OneToMany(mappedBy = "squad")
    private List<SquadWorker> workers;

    @OneToMany(mappedBy = "squad")
    private List<RepairSquad> repairSquads;
}
