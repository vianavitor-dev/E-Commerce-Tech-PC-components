package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.PsuEfficiencyRating;
import com.vianavitor.ecommerce_tech.models.aux.enums.PsuFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsuFormFactorConverter implements AttributeConverter<PsuFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(PsuFormFactor attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public PsuFormFactor convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, PsuFormFactor.class);
    }
}
