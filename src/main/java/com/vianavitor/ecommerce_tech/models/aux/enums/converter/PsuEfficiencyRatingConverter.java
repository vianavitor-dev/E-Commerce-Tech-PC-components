package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuEfficiencyRating;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsuEfficiencyRatingConverter implements AttributeConverter<PsuEfficiencyRating, String> {
    @Override
    public String convertToDatabaseColumn(PsuEfficiencyRating attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public PsuEfficiencyRating convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, PsuEfficiencyRating.class);
    }
}
