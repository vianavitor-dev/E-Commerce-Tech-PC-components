package com.vianavitor.ecommerce_tech.repositories;
import com.vianavitor.ecommerce_tech.models.Hd;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

@Repository
public interface HdRepository extends ReadOnlyInterface<Hd, Integer> {
}

