package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.services.ProductService;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService service;

    @GetMapping("/by-name")
    public ResponseEntity<List<?>> searchByName(@Nullable @RequestParam String name) {
        List<? extends ProductSearchResultsDTO> results = null;

        if (name == null || name.isBlank()) {
            results = service.findAll();
        } else {
            results = service.findByName(name);
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

    @GetMapping()
    public ResponseEntity<List<?>> searchAll() {
        List<? extends ProductSearchResultsDTO> results = service.findAll();

        return ResponseEntity.ok(results);
    }
}
