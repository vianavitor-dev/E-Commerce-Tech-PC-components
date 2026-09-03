package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Cpu;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;


@Repository
public interface CpuRepository extends ReadOnlyInterface<Cpu, Integer> {
}
