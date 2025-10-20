package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "UserRoles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RoleId")
    private short roleId;

    @Column(name = "RoleName", nullable = false, unique = true)
    private String roleName;

    @Column(name = "Description", nullable = false, length = 1024)
    private String description;
}
