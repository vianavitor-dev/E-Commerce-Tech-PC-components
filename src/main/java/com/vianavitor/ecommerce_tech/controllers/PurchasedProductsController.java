package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.services.PurchasedProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/purchased-products")
public class PurchasedProductsController {
    @Autowired
    private PurchasedProductService service;

    @GetMapping("/by-order/{orderId}")
    public ResponseEntity<List<?>> getByOrder(@PathVariable Integer orderId) {
        return ResponseEntity.ok(service.getOrderProducts(orderId));
    }
}
