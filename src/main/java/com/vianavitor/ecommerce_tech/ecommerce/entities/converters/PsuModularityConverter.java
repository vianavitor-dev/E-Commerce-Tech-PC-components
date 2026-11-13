package com.vianavitor.ecommerce_tech.ecommerce.entities.converters;

import com.vianavitor.ecommerce_tech.ecommerce.entities.converters.ConverterAux;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.PsuModularity;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class PsuModularityConverter implements AttributeConverter<PsuModularity, String> {
    @Override
    public String convertToDatabaseColumn(PsuModularity attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public PsuModularity convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, PsuModularity.class);
    }
}
