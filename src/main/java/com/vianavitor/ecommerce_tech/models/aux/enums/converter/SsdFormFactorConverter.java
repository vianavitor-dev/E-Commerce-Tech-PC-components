package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SsdFormFactorConverter implements AttributeConverter<SsdFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(SsdFormFactor attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public SsdFormFactor convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, SsdFormFactor.class);
    }
}
