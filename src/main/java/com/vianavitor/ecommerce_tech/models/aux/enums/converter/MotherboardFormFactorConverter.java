package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class MotherboardFormFactorConverter implements AttributeConverter<MotherboardFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(MotherboardFormFactor attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public MotherboardFormFactor convertToEntityAttribute(String dbData) {
        return Stream.of(MotherboardFormFactor.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
