package com.polytechnique.backend.status;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Arrays;

/**
 * Enum représentant les différents statuts possibles des paramètres médicaux
 */
@Schema(description = "Statut des paramètres médicaux dans le système")
public enum StatutParametre {

    @Schema(description = "Paramètres en attente de diagnostic")
    EN_ATTENTE("en_attente"),

    @Schema(description = "Paramètres diagnostiqués par un médecin")
    DIAGNOSTIQUE("diagnostique"),

    @Schema(description = "Ancien paramètre archivé (non diagnostiqué ou obsolète)")
    ARCHIVE("archive");

    private final String value;

    StatutParametre(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    @JsonCreator
    public static StatutParametre fromValue(String value) {
        if (value == null)
            return null;
        return Arrays.stream(StatutParametre.values())
                .filter(s -> s.value.equalsIgnoreCase(value) || s.name().equalsIgnoreCase(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return value;
    }
}
