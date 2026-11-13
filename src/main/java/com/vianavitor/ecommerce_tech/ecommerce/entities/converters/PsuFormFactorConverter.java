package com.vianavitor.ecommerce_tech.ecommerce.entities.converters;

import com.vianavitor.ecommerce_tech.ecommerce.entities.converters.ConverterAux;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.PsuFormFactor;
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
