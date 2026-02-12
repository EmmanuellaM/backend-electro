package com.polytechnique.backend.status;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Convertisseur pour mapper l'enum StatutParametre aux valeurs minuscules de la
 * base de données
 */
@Converter(autoApply = true)
public class StatutParametreConverter implements AttributeConverter<StatutParametre, String> {

    @Override
    public String convertToDatabaseColumn(StatutParametre attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValue();
    }

    @Override
    public StatutParametre convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return StatutParametre.fromValue(dbData);
    }
}
