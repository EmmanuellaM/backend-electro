package com.polytechnique.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entité pour stocker l'historique des SMS envoyés aux infirmiers
 */
@Entity
@Table(name = "sms_messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "infirmier_local_id", nullable = false)
    private InfirmierLocal infirmierLocal;

    @Column(nullable = false)
    private String telephone;

    @Column(nullable = false, length = 1000)
    private String message;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private String sentBy;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SmsStatus status = SmsStatus.SIMULATED;

    @PrePersist
    protected void onCreate() {
        if (sentAt == null) {
            sentAt = LocalDateTime.now();
        }
    }
}
