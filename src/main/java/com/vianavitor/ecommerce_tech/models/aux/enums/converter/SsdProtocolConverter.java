package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
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
