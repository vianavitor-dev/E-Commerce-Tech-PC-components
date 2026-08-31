package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SsdFormFactorConverter implements AttributeConverter<SsdFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(SsdFormFactor attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public SsdFormFactor convertToEntityAttribute(String dbData) {
        return Stream.of(SsdFormFactor.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
