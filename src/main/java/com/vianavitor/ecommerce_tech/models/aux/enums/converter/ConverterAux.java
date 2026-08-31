package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import java.util.stream.Stream;

/**
 * Class used to provide mapping methods to format Database to Entities Enums and vice versa
 */
public class ConverterAux {
    public static <E extends Enum<E>> String convertToDBColumn(E attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.toString();
    }

    public static <E extends Enum<E>> E convertToEntityAtt(String dbData, Class<E> eClass) {
        if (dbData.isEmpty() || eClass == null) {
            return null;
        }

        return Stream.of(eClass.getEnumConstants())
                .filter(f -> f.toString().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
