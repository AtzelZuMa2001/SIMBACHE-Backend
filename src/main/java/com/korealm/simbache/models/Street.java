package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Streets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Street {

    @Id
    @Column(name = "StreetId")
    private Long streetId; // not identity per schema

    @Column(name = "StreetName", nullable = false)
    private String streetName;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Colonia_FK")
    private Colonia colonia; // nullable

    @ManyToOne(optional = false)
    @JoinColumn(name = "Locality_FK", nullable = false)
    private Locality locality;
}
