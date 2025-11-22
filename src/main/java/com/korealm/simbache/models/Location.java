package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne()
    @JoinColumn(name = "Colonia_FK")
    private Colonia colonia; // nullable

    @Column(name = "PostalCode", nullable = false)
    private int postalCode;

    @ManyToOne(optional = false)
    @JoinColumn(name = "MainStreet_FK", nullable = false)
    private Street mainStreet;

    @ManyToOne()
    @JoinColumn(name = "Street_One_FK")
    private Street streetOne;

    @ManyToOne()
    @JoinColumn(name = "Street_Two_FK")
    private Street streetTwo;
}
