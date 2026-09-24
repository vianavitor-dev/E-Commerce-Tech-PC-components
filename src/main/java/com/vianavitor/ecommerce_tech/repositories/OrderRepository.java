package com.vianavitor.ecommerce_tech.repositories;

import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByUser(User user);
}
