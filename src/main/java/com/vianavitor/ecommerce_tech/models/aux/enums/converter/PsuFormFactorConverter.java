package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.PsuFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsuFormFactorConverter implements AttributeConverter<PsuFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(PsuFormFactor attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public PsuFormFactor convertToEntityAttribute(String dbData) {
        return Stream.of(PsuFormFactor.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
