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
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public SsdProtocol convertToEntityAttribute(String dbData) {
        return Stream.of(SsdProtocol.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
