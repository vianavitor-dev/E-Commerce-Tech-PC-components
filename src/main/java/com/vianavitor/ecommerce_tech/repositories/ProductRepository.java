package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@NoRepositoryBean
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Optional<Product> findBySku(String sku);

    List<Product> FindByNameContaining(String name);

    List<Product> FindByCategory(ProductCategory category);

    List<Product> FindByCategoryAndNameContaining(ProductCategory category, String name);
}
