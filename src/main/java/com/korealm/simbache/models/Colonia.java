package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Colonias")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Colonia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ColoniaId")
    private Long coloniaId;

    @Column(name = "ColoniaName", nullable = false)
    private String coloniaName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Locality_FK", nullable = false)
    private Locality locality;

    @OneToMany(mappedBy = "colonia")
    private List<Street> streets;
}
