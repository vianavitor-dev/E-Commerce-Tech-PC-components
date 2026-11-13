package com.vianavitor.ecommerce_tech.ecommerce.services;

import com.vianavitor.ecommerce_tech.auth.services.UserService;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.UsersOrderDTO;
import com.vianavitor.ecommerce_tech.ecommerce.services.ProductService;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.ecommerce.exceptions.OrderNotCanceledException;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Order;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.PurchasedProduct;
import com.vianavitor.ecommerce_tech.auth.entities.User;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.OrderStatus;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.OrderRepository;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.PurchasedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository repository;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private PurchasedProductRepository purchasedProductRepository;

    public Order findById(Integer id) throws NotFoundResourceException {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundResourceException("This order does not exists"));
    }

    public List<UsersOrderDTO> findByUser(Integer userId) throws NotFoundResourceException {
        User user = userService.getById(userId);

        return repository.findByUser(user)
                .stream()
                .map(order -> new UsersOrderDTO(
                        order.getId(), order.getStatus(), order.getOrderedAt(), order.getStatusUpdatedAt()
                ))
                .toList();
    }

    public void orderProducts(Integer userId, Map<Integer, Byte> cart) throws NotFoundResourceException, IllegalArgumentException {
        if (cart.isEmpty()) {
            throw new IllegalArgumentException("There are no products to order");
        }

        User user = userService.getById(userId);

        LocalDate now = LocalDate.now();
        Order userOrder = repository.save(new Order(null, user, OrderStatus.IN_PROCESS, now, now));

        // attach products bought with the order
        List<PurchasedProduct> purchasedProducts = new ArrayList<>();

        for (var entry = cart.entrySet().iterator(); entry.hasNext();) {
            Map.Entry<Integer, Byte> current = entry.next();

            Integer productId = current.getKey();
            Byte productAmount = current.getValue();

            Product product = productService.findById(productId);
            purchasedProducts.add(
                    new PurchasedProduct(null, product, userOrder, productAmount)
            );
        }

        purchasedProductRepository.saveAll(purchasedProducts);
    }

    private void updateStatusAndDate(Order order, OrderStatus newValue) {
        LocalDate now = LocalDate.now();

        order.setStatus(newValue);
        order.setStatusUpdatedAt(now);
    }

    public void refund(Integer id) throws NotFoundResourceException, OrderNotCanceledException {
        Order order = this.findById(id);
        this.updateStatusAndDate(order, order.getStatus().refund());

        if (order.getStatus() == null) {
            throw new OrderNotCanceledException("You must cancel the order before requesting refund!");
        }

        repository.save(order);
    }

    public void changeStatus(Integer id, Boolean cancel) throws NotFoundResourceException {
        Order order = this.findById(id);
        OrderStatus previousStatus = order.getStatus();
        this.updateStatusAndDate(order, order.getStatus().next(cancel));

        if (previousStatus == order.getStatus()) {
            return;
        }

        repository.save(order);
    }
}
