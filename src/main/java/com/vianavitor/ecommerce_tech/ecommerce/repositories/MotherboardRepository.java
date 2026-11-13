package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Motherboard;
import com.vianavitor.ecommerce_tech.shared.repositories.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

@Repository
public interface MotherboardRepository extends ReadOnlyInterface<Motherboard, Integer> {
}
