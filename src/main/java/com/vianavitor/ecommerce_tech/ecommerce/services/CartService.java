package com.vianavitor.ecommerce_tech.ecommerce.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.CartDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Order;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.PurchasedProduct;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.ProductRepository;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.annotation.RequestScope;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Getter
@RequiredArgsConstructor
@Service
@RequestScope
@Validated
public class CartService {
    private Map<Product, Byte> products;
    private LocalDateTime lastUpdate;
    private final ProductRepository productRepository;

    public void from(@NotNull(message = "Cart data is required") @Valid CartDTO data) throws IllegalArgumentException {
        this.lastUpdate = data.lastUpdate();

        Map<Product, Byte> resolvedProducts = new LinkedHashMap<>();
        data.products().forEach((productId, amount) -> {
            if (amount == null || amount <= 0) {
                throw new IllegalArgumentException("Product amount must be greater than zero");
            }
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NotFoundResourceException("Not found product with ID " + productId));
            resolvedProducts.put(product, amount);
        });

        this.products = Collections.unmodifiableMap(resolvedProducts);
    }

    public boolean hasProducts() {
        return !products.isEmpty();
    }

    public List<PurchasedProduct> toPurchasedProducts(
            @NotNull(message = "An order is required for purchased products") Order order) {
        return products.entrySet().stream()
                .map(entry -> new PurchasedProduct(null, entry.getKey(), order, entry.getValue()))
                .toList();
    }
}
