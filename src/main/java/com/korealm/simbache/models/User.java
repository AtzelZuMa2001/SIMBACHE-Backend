package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, length = 500)
    private String passwordHash;

    @Column(nullable = false)
    private String firstName;

    @Column()
    private String middleName;

    @Column(nullable = false)
    private String lastName;

    @Column()
    private String secondLastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private Long phoneNumber;

    @Column(nullable = false)
    private boolean status = true; // maps bit(1)

    /*
    * Hibernate nos permite declarar las foreign keys como relaciones a otras clases de Java.
    Al hacerlo, ya podemos hacer unas queries bien chetadas involucrando muchas tablas como si fuera nada.
    */

    @Column(name = "Role_FK", nullable = false)
    private Integer roleFk;

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL, // Activar el ON DELETE CASCADE a nivel de Java (no a nivel de la BD, sino que Java internamente calcula las relaciones y las elimina en orden)
            orphanRemoval = true // Elmina cualquier token si el usuario se elimina de la BD (o sea, si se elimina el padre, ve y elimina a todos los hijos primero).
    )
    private SessionToken sessionToken;
}

