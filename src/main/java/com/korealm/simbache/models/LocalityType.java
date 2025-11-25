package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "LocalityType")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class LocalityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private short id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "localityType")
    private List<Locality> localities;
}
