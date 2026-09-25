package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.PurchasedProduct;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.models.aux.enums.OrderStatus;
import com.vianavitor.ecommerce_tech.repositories.OrderRepository;
import com.vianavitor.ecommerce_tech.repositories.PurchasedProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

    public List<Order> findByUser(Integer userId) throws NotFoundResourceException {
        User user = userService.getById(userId);

        return repository.findByUser(user);
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

    public void refund(Integer id) throws NotFoundResourceException, IllegalArgumentException {
        Order order = this.findById(id);
        this.updateStatusAndDate(order, order.getStatus().refund());

        if (order.getStatus() == null) {
            throw new IllegalArgumentException("You must cancel the order before requesting refund!");
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
