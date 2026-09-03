package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Ram;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RamRepository extends JpaRepository<Ram, Integer> {
    List<Ram> findByModel(String model);
}
