package com.polytechnique.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO pour envoyer un SMS à un infirmier
 */
@Data
public class SmsMessageRequestDTO {

    @NotNull(message = "L'ID de l'infirmier est obligatoire")
    private Integer infirmierId;

    @NotBlank(message = "Le message est obligatoire")
    private String message;

    private String sentBy;
}
