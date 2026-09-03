package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Ssd;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsdRepository extends ReadOnlyInterface<Ssd, Integer> {
}
