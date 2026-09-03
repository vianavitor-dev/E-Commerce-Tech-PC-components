package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Gpu;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;


@Repository
public interface GpuRepository extends ReadOnlyInterface<Gpu, Integer> {
}
