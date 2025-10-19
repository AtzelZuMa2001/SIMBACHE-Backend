package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SquadRoles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SquadRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private short roleId;

    @Column(name = "RoleName", nullable = false, unique = true)
    private String roleName;
}
