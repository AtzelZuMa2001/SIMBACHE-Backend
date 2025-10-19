package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "InspectionStatus")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class InspectionStatus {

    @Id
    @Column(name = "StatusId")
    private short statusId;

    @Column(name = "StatusName", nullable = false, length = 64)
    private String statusName;
}