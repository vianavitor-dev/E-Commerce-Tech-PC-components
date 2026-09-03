package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// TODO: create a Inventory System to manage the products
// TODO: implement PC components compatibility check
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product findBySku(String sku) {
        return repository.findBySku(sku).
                orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided SKU code"
                ));
    }

    public List<Product> findByName(String name) {
        return repository.FindByNameContaining(name);
    }

    public List<Product> findByCategory(ProductCategory category) {
        return repository.FindByCategory(category);
    }

    public List<Product> findByCategoryAndName(ProductCategory category, String name) {
        return repository.FindByCategoryAndNameContaining(category, name);
    }
}
