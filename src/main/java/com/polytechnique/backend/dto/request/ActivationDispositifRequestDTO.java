package com.polytechnique.backend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Requête pour activer un dispositif et l'assigner à un infirmier")
public class ActivationDispositifRequestDTO {

    @NotNull(message = "L'ID de l'infirmier est obligatoire pour l'activation")
    @Schema(description = "ID de l'infirmier responsable du dispositif", example = "1")
    private Integer infirmierId;
}
