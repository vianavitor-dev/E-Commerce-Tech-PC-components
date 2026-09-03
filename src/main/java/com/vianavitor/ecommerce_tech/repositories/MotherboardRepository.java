package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Motherboard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MotherboardRepository extends JpaRepository<Motherboard, Integer> {
    List<Motherboard> findByModelContaining(String model);
}
