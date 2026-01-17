package com.polytechnique.backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NotificationSMSResponseDTO {
    private Integer id;
    private String contenuMessage;
    private String numeroDestinataire;
    private LocalDateTime dateEnvoi;
    private Boolean succes;
    private Integer infirmierId;
    private Integer diagnosticId;
}
