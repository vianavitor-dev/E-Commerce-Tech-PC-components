package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Order;
import com.vianavitor.ecommerce_tech.ecommerce.entities.PurchasedProduct;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchasedProductRepository extends CrudRepository<PurchasedProduct, Integer> {
    List<PurchasedProduct> findByOrder(Order order);
}
