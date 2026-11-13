package com.vianavitor.ecommerce_tech.ecommerce.entities.converters;

import com.vianavitor.ecommerce_tech.ecommerce.entities.converters.ConverterAux;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.RamFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RamFormFactorConverter implements AttributeConverter<RamFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(RamFormFactor attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public RamFormFactor convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, RamFormFactor.class);
    }
}
