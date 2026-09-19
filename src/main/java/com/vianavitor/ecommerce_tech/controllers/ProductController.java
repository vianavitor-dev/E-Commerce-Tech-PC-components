package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.dtos.request.ProductUserIdsDTO;
import com.vianavitor.ecommerce_tech.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.services.ProductService;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping
    public ResponseEntity<List<?>> search(
            @Nullable @RequestParam String name,
            @Nullable @RequestParam ProductCategory category
    ) {
        List<? extends ProductSearchResultsDTO> results = null;

        if (name == null) {
            if (category == null) {
                results = service.findAll();
            } else {
                results = service.findByCategory(category);
            }
        } else {
            if (category == null) {
                results = service.findByName(name);
            } else {
                results = service.findByCategoryAndName(category, name);
            }
        }

        return ResponseEntity.ok(results);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getById(@PathVariable(name = "id") Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PostMapping("/compatibility-check")
    public ResponseEntity<PcCompatibilityCheckResultDTO> checkPcComponentsCompatibility(@RequestBody PcComponentsDTO request) {
        return ResponseEntity.ok(service.checkPcComponentsCompatibility(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<?>> searchAll() {
        List<? extends ProductSearchResultsDTO> results = service.findAll();

        return ResponseEntity.ok(results);
    }

    @PostMapping("/rate")
    public ResponseEntity<BigDecimal> rate(@RequestBody ProductUserIdsDTO request, @Nullable @RequestParam BigDecimal value) {
        BigDecimal result;

        if (value == null) {
            result = service.removesRate(request.productId(), request.userId());
        } else {
            result = service.rateProduct(request.productId(), request.userId(), value);
        }

        return ResponseEntity.ok(result);
    }
}
