package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Ssd;
import com.vianavitor.ecommerce_tech.shared.repositories.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

@Repository
public interface SsdRepository extends ReadOnlyInterface<Ssd, Integer> {
}
