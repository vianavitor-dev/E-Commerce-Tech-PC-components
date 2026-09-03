package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Cpu;
import com.vianavitor.ecommerce_tech.models.Gpu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CpuRepository extends JpaRepository<Cpu, Integer> {
    List<Gpu> findByModel(String model);
}
