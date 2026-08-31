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
        if (attribute == null) {
            return null;
        }

        return attribute.getFormattedName();
    }

    @Override
    public SsdInterface convertToEntityAttribute(String dbData) {
        return Stream.of(SsdInterface.values())
                .filter(form -> form.getFormattedName().equals(dbData))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
