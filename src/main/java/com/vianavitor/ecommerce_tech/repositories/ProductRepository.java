package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.repositories.aux.ReadOnlyInterface;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends ReadOnlyInterface<Product, Integer> {
    Optional<Product> findBySku(String sku);

    @Query(value = """
            SELECT
                p.id, p.name, p.category, p.rating,
                p.rated_count, p.brand, p.price
            FROM products p
            WHERE MATCH(p.name) AGAINST(:name)
            """,
            nativeQuery = true)
    List<ProductSearchResultsDTO> searchByNameContaining(String name);

    List<ProductSearchResultsDTO> findByCategory(ProductCategory category);

    @Query(value = """
            SELECT
                p.id, p.name, p.category, p.rating,
                p.rated_count, p.brand, p.price
            FROM products p
            WHERE p.category = :#{#category.toString()} AND MATCH(p.name) AGAINST(:name)
            """,
            nativeQuery = true)
    List<ProductSearchResultsDTO> searchByCategoryAndNameContaining(ProductCategory category, String name);
}
