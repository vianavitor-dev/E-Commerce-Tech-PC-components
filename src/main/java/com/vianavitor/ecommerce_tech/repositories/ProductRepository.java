package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ReadOnlyInterface<Product, Integer> {
    Optional<Product> findBySku(String sku);

    @Query(value = """
            SELECT * 
            FROM products WHERE MATCH(name) AGAINST(:name)
            """,
            nativeQuery = true)
    List<Product> findByNameContaining(String name);

    List<Product> findByCategory(ProductCategory category);

    @Query(value = """
            SELECT * 
            FROM products WHERE category = :category AND MATCH(name) AGAINST(:name)
            """,
            nativeQuery = true)
    List<Product> findByCategoryAndNameContaining(ProductCategory category, String name);
}
