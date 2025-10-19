package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Municipalities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MunicipalityId")
    private short municipalityId;

    @Column(name = "MunicipalityName", nullable = false, unique = true, length = 255)
    private String municipalityName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "State_FK", nullable = false)
    private State state;

    @OneToMany(mappedBy = "municipality")
    private List<Locality> localities;
}
