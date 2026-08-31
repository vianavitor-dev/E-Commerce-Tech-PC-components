package com.vianavitor.ecommerce_tech.models.aux.enums.converter;

import com.vianavitor.ecommerce_tech.models.aux.enums.MotherboardFormFactor;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class MotherboardFormFactorConverter implements AttributeConverter<MotherboardFormFactor, String> {
    @Override
    public String convertToDatabaseColumn(MotherboardFormFactor attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public MotherboardFormFactor convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, MotherboardFormFactor.class);
    }
}
