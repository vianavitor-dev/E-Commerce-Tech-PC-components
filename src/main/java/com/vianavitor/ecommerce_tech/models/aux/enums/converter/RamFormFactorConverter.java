package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.RamFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
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
