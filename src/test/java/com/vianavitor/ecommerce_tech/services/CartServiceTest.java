package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.CartDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Order;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.ProductRepository;
import com.vianavitor.ecommerce_tech.ecommerce.services.CartService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void resolvesProductsAndAttachesThemToOrder() {
        Product product = new Product();
        Order order = new Order();
        LocalDateTime lastUpdate = LocalDateTime.now();
        when(productRepository.findById(10)).thenReturn(Optional.of(product));

        cartService.from(new CartDTO(Map.of(10, (byte) 2), lastUpdate));
        var purchasedProducts = cartService.toPurchasedProducts(order);

        assertFalse(cartService.isEmpty());
        assertEquals(lastUpdate, cartService.getLastUpdate());
        assertEquals(1, purchasedProducts.size());
        assertSame(product, purchasedProducts.getFirst().getProduct());
        assertSame(order, purchasedProducts.getFirst().getOrder());
        assertEquals((byte) 2, purchasedProducts.getFirst().getAmount());
    }

    @Test
    void emptyCartHasNoProducts() {
        cartService.from(new CartDTO(Map.of(), LocalDateTime.now()));

        assertTrue(cartService.isEmpty());
        verifyNoInteractions(productRepository);
    }

    @Test
    void rejectsNonPositiveQuantityBeforeResolvingProduct() {
        CartDTO cart = new CartDTO(Map.of(10, (byte) 0), LocalDateTime.now());

        assertThrows(IllegalArgumentException.class, () -> cartService.from(cart));
        verifyNoInteractions(productRepository);
    }
}
