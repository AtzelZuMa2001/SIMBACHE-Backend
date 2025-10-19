package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "SquadHasWorkers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SquadWorker {

    @EmbeddedId
    private SquadWorkerId id;

    @ManyToOne(optional = false)
    @MapsId("squadId")
    @JoinColumn(name = "SquadId", nullable = false)
    private Squad squad;

    @ManyToOne(optional = false)
    @MapsId("workerId")
    @JoinColumn(name = "WorkerId", nullable = false)
    private Worker worker;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Role_FK", nullable = false)
    private SquadRole role;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;
}
