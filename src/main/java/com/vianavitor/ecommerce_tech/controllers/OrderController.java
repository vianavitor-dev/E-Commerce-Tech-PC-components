package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService service;

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Order>> getUserOrders(@PathVariable Integer userId) {
        return ResponseEntity.ok(service.findByUser(userId));
    }

    @PostMapping("/for-user/{userId}")
    public ResponseEntity<?> orderProducts(@PathVariable Integer userId, @RequestBody Map<Integer, Byte> cart) {
        service.orderProducts(userId, cart);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/refund/{id}")
    public ResponseEntity<?> refund(@PathVariable Integer id) {
        service.refund(id);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> changeStatus(@PathVariable Integer id, @RequestParam Boolean cancel) {
        service.changeStatus(id, cancel);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
