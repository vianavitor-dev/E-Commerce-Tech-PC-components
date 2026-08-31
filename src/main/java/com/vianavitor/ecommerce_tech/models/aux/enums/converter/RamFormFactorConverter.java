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
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public RamFormFactor convertToEntityAttribute(String dbData) {
        return Stream.of(RamFormFactor.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
