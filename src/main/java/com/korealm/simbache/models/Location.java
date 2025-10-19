package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "Locations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LocationId")
    private Long locationId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "State_FK", nullable = false)
    private State state;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Municipality_FK", nullable = false)
    private Municipality municipality;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Locality_FK", nullable = false)
    private Locality locality;

    @ManyToOne(optional = true)
    @JoinColumn(name = "Colonia_FK")
    private Colonia colonia; // nullable

    @ManyToOne(optional = true)
    @JoinColumn(name = "Street_FK")
    private Street street; // nullable

    @Column(name = "PostalCode", nullable = false)
    private int postalCode;

    @Column(name = "Latitude", precision = 11, scale = 8)
    private BigDecimal latitude;

    @Column(name = "Longitude", precision = 11, scale = 8)
    private BigDecimal longitude;
}
