package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.response.OrdersProductDTO;
import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.User;
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

    public List<OrdersProductDTO> getOrderProducts(Integer orderId) {
        Order order = orderService.findById(orderId);

        return repository.findByOrder(order)
                .stream()
                .map(p -> {
                    User u = p.getOrder().getUser();
                    Product prod = p.getProduct();

                    return new OrdersProductDTO(
                            prod.getId(), prod.getName(), prod.getBrand(),
                            new OrdersProductDTO.UserWhoBought(
                                    u.getId(), u.getName()
                            ),
                            prod.getCategory(), prod.getPrice(), p.getAmount()
                    );
                })
                .toList();
    }

}
