package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "SessionToken")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SessionToken {

    @Id
    @Column(name = "TokenId", length = 64)
    private String tokenId;   // the random UUID/session string

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "User_FK", nullable = false, unique = true)
    private User user;
}
