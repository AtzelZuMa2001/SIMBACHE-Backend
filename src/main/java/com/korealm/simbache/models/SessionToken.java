package com.korealm.simbache.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "SessionToken")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionToken {

    @Id
    @Column(name = "TokenId", length = 128)
    private String tokenId;   // the random UUID/session string

    @Column(name = "User_FK", nullable = false)
    private Long userFk;
}
