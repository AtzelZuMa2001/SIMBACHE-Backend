package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Streets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Street {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "StreetId")
    private Long streetId;

    @Column(name = "StreetName", nullable = false)
    private String streetName;

    @ManyToOne()
    @JoinColumn(name = "Colonia_FK")
    private Colonia colonia; // nullable

    @ManyToOne(optional = false)
    @JoinColumn(name = "Locality_FK", nullable = false)
    private Locality locality;
}
