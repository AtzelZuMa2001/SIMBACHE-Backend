package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Streets")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Street {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "StreetId")
    private Long streetId;

    @Column(name = "StreetName", nullable = false)
    private String streetName;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Locality_FK", nullable = false)
    private Locality locality;
}
