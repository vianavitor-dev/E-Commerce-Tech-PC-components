package com.vianavitor.ecommerce_tech.ecommerce.entities.converters;

import com.vianavitor.ecommerce_tech.ecommerce.entities.converters.ConverterAux;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdProtocol;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SsdProtocolConverter implements AttributeConverter<SsdProtocol, String> {
    @Override
    public String convertToDatabaseColumn(SsdProtocol attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public SsdProtocol convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, SsdProtocol.class);
    }
}
