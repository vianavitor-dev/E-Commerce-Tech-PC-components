package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.PurchasedProduct;
import com.vianavitor.ecommerce_tech.repositories.PurchasedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PurchasedProductService {
    @Autowired
    private PurchasedProductRepository repository;

    @Autowired
    private OrderService orderService;

    public List<PurchasedProduct> getOrderProducts(Integer orderId) {
        Order order = orderService.findById(orderId);
        return repository.findByOrder(order);
    }

}
