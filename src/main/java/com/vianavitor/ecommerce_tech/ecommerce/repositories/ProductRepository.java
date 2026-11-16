package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.shared.repositories.ReadOnlyInterface;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Lock;
import jakarta.persistence.LockModeType;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

@Repository
public interface ProductRepository extends ReadOnlyInterface<Product, Integer> {
    Optional<Product> findBySku(String sku);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from products p where p.id = :id")
    Optional<Product> findByIdForUpdate(Integer id);

    Product save(Product entity);

    Page<Product> findAll(Pageable pageable);

    @Query(value = """
            SELECT
                p.id, p.name, p.category, p.rating,
                p.rated_count, p.brand, p.price
            FROM products p
            WHERE MATCH(p.name) AGAINST(:name)
            """,
            countQuery = "SELECT COUNT(*) FROM products p WHERE MATCH(p.name) AGAINST(:name)",
            nativeQuery = true)
    Page<ProductSearchResultsDTO> searchByNameContaining(String name, Pageable pageable);

    Page<ProductSearchResultsDTO> findByCategory(ProductCategory category, Pageable pageable);

    @Query(value = """
            SELECT
                p.id, p.name, p.category, p.rating,
                p.rated_count, p.brand, p.price
            FROM products p
            WHERE p.category = :#{#category.toString()} AND MATCH(p.name) AGAINST(:name)
            """,
            countQuery = """
                    SELECT COUNT(*) FROM products p
                    WHERE p.category = :#{#category.toString()} AND MATCH(p.name) AGAINST(:name)
                    """,
            nativeQuery = true)
    Page<ProductSearchResultsDTO> searchByCategoryAndNameContaining(ProductCategory category, String name, Pageable pageable);
}
