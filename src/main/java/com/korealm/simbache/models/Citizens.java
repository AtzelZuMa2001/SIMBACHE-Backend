package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Citizens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Citizens {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CitizenId")
    private long citizenId;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "MiddleName")
    private String middleName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "SecondLastName")
    private String secondLastName;

    @Column(name = "Email", nullable = false)
    private String email;

    @Column(name = "PhoneNumber")
    private Long phoneNumber;

    @Column(name = "CreatedAt")
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    @JoinColumn(name = "RegisteredLocalion_FK", nullable = false)
    private Location registeredLocation;
}
