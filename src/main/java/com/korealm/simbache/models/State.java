package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "States")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class State {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StateId")
    private short stateId;

    @Column(name = "StateName", nullable = false, unique = true)
    private String stateName;

    @OneToMany(mappedBy = "state")
    private List<Municipality> municipalities;
}
