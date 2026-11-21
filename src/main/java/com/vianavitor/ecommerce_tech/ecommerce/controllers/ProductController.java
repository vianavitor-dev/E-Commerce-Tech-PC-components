package com.vianavitor.ecommerce_tech.ecommerce.controllers;

import com.vianavitor.ecommerce_tech.ecommerce.services.ProductService;
import com.vianavitor.ecommerce_tech.ecommerce.services.CompatibilityCheckService;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.ProductIdDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.auth.security.UserDetailsImpl;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import jakarta.annotation.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @Autowired
    private CompatibilityCheckService compatibilityCheckService;

    @GetMapping
    public ResponseEntity<Page<ProductSearchResultsDTO>> search(
            @Nullable @RequestParam String name,
            @Nullable @RequestParam ProductCategory category,
            @PageableDefault(size = 20, sort = "id") Pageable pageable
    ) {
        Page<ProductSearchResultsDTO> results;

        if (name == null) {
            if (category == null) {
                results = service.findAll(pageable);
            } else {
                results = service.findByCategory(category, pageable);
            }
        } else {
            if (category == null) {
                results = service.findByName(name, pageable);
            } else {
                results = service.findByCategoryAndName(category, name, pageable);
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
        return ResponseEntity.ok(compatibilityCheckService.checkPcComponentsCompatibility(request));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProductSearchResultsDTO>> searchAll(
            @PageableDefault(size = 20, sort = "id") Pageable pageable) {
        Page<ProductSearchResultsDTO> results = service.findAll(pageable);

        return ResponseEntity.ok(results);
    }

    @PostMapping("/rate")
    public ResponseEntity<BigDecimal> rate(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody ProductIdDTO request,
            @Nullable @RequestParam BigDecimal value) {

        BigDecimal result;

        if (value == null) {
            result = service.removesRate(request.productId(), principal.getUserId());
        } else {
            result = service.rateProduct(request.productId(), principal.getUserId(), value);
        }

        return ResponseEntity.ok(result);
    }
}
