package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Psu;
import com.vianavitor.ecommerce_tech.shared.repositories.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

@Repository
public interface PsuRepository extends ReadOnlyInterface<Psu, Integer> {
}
