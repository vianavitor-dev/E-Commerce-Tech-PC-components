package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class SsdInterfaceConverter implements AttributeConverter<SsdInterface, String> {
    @Override
    public String convertToDatabaseColumn(SsdInterface attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public SsdInterface convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, SsdInterface.class);
    }
}
