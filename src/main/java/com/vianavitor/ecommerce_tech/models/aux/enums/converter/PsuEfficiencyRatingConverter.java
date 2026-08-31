package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.PsuEfficiencyRating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsuEfficiencyRatingConverter implements AttributeConverter<PsuEfficiencyRating, String> {
    @Override
    public String convertToDatabaseColumn(PsuEfficiencyRating attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public PsuEfficiencyRating convertToEntityAttribute(String dbData) {
        return Stream.of(PsuEfficiencyRating.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
