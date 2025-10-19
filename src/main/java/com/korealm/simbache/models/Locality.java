package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Localities")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Locality {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LocalityId")
    private int localityId;

    @Column(name = "LocalityName", nullable = false, unique = true)
    private String localityName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Municipality_FK", nullable = false)
    private Municipality municipality;

    @Column(name = "Type", nullable = false)
    private String type;

    @OneToMany(mappedBy = "locality")
    private List<Colonia> colonias;

    @OneToMany(mappedBy = "locality")
    private List<Street> streets;
}
