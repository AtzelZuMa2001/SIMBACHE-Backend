package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Contractors")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Contractor {

    @Id
    @Column(name = "ContractorId")
    private Integer contractorId; // assigned (not identity)

    @Column(name = "ContractorName", nullable = false, unique = true, length = 1024)
    private String contractorName;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "PhoneNumber", nullable = false)
    private Long phoneNumber; // bigint

    @ManyToOne(optional = false)
    @JoinColumn(name = "HeadquaterLocation_FK", nullable = false)
    private Location headquaterLocation;

    @Column(name = "Type", nullable = false, length = 64)
    private String type;
}
