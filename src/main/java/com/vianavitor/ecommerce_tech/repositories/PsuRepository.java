package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Psu;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PsuRepository extends ReadOnlyInterface<Psu, Integer> {
}
