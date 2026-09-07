package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ReadOnlyInterface<Product, Integer> {
    Optional<Product> findBySku(String sku);

    List<Product> findByNameContaining(String name);

    List<Product> findByCategory(ProductCategory category);

    List<Product> findByCategoryAndNameContaining(ProductCategory category, String name);
}
