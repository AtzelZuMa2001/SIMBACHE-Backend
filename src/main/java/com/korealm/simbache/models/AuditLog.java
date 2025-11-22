package com.korealm.simbache.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "AuditLogs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LogId")
    private Long logId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_FK", nullable = false)
    private User user;

    @Column(name = "Action", nullable = false)
    private String action;

    @CreationTimestamp
    @Column(name = "Timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "Details", nullable = false, length = 2048)
    private String details;
}
