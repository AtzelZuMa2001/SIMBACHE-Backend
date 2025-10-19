package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "Workers")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Worker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WorkerId")
    private Long workerId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "Contractor_FK", nullable = false)
    private Contractor contractor;

    @Column(name = "FirstName", nullable = false)
    private String firstName;

    @Column(name = "MiddleName")
    private String middleName;

    @Column(name = "LastName", nullable = false)
    private String lastName;

    @Column(name = "SecondLastName")
    private String secondLastName;

    @Column(name = "Email", unique = true)
    private String email;

    @Column(name = "PhoneNumber", nullable = false, unique = true)
    private Long phoneNumber;

    @Column(name = "Status", nullable = false)
    private boolean status;

    @ManyToOne(optional = false)
    @JoinColumn(name = "WorkerPosition_FK", nullable = false)
    private WorkerPosition workerPosition;

    @OneToMany(mappedBy = "worker")
    private List<SquadWorker> squadMemberships;
}
