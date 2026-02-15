package com.polytechnique.backend.dto.response;

import com.polytechnique.backend.status.SmsStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO pour la réponse d'un SMS
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SmsMessageResponseDTO {

    private Integer id;
    private Integer infirmierId;
    private String infirmierNom;
    private String telephone;
    private String message;
    private LocalDateTime sentAt;
    private String sentBy;
    private Integer administrateurId;
    private String administrateurNom;
    private SmsStatus status;
}
