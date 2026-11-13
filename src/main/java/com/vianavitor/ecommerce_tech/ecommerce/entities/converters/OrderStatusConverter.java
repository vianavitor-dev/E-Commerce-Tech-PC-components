package com.vianavitor.ecommerce_tech.ecommerce.entities.converters;

import com.vianavitor.ecommerce_tech.ecommerce.entities.converters.ConverterAux;

import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.OrderStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    @Override
    public String convertToDatabaseColumn(OrderStatus attribute) {
        return ConverterAux.convertToDBColumn(attribute);
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        return ConverterAux.convertToEntityAtt(dbData, OrderStatus.class);
    }
}
