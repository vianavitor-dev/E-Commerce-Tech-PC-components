package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Ssd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SsdRepository extends JpaRepository<Ssd, Integer> {
    List<Ssd> findByModel(String model);
}
