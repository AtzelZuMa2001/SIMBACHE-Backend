package com.korealm.simbache.models;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @EqualsAndHashCode
public class SquadWorkerId implements Serializable {

    @Column(name = "SquadId")
    private Integer squadId;

    @Column(name = "WorkerId")
    private Long workerId;
}
