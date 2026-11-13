package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.auth.services.UserService;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.CartDTO;
import com.vianavitor.ecommerce_tech.ecommerce.services.OrderService;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.UsersOrderDTO;
import com.vianavitor.ecommerce_tech.ecommerce.services.CartService;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.ecommerce.exceptions.OrderNotCanceledException;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Order;
import com.vianavitor.ecommerce_tech.ecommerce.entities.PurchasedProduct;
import com.vianavitor.ecommerce_tech.auth.entities.User;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.OrderStatus;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.OrderRepository;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.PurchasedProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private CartService cartService;

    @Mock
    private UserService userService;

    @Mock
    private PurchasedProductRepository purchasedProductRepository;

    @Mock
    private OrderStatus orderStatusMock;

    @InjectMocks
    private OrderService orderService;

    private User sampleUser;
    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleOrder = new Order();
        sampleOrder.setStatus(OrderStatus.IN_PROCESS);

    }

    // --- findByUser ---

    @Test
    void findByUser_ShouldReturnOrders_WhenUserExists() throws NotFoundResourceException {
        when(userService.getById(1)).thenReturn(sampleUser);
        when(repository.findByUser(sampleUser)).thenReturn(List.of(sampleOrder));

        List<UsersOrderDTO> result = orderService.findByUser(1);

        UsersOrderDTO sampleDTO = new UsersOrderDTO(
                sampleOrder.getId(), sampleOrder.getStatus(),
                sampleOrder.getOrderedAt(), sampleOrder.getStatusUpdatedAt()
        );

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(sampleDTO, result.get(0));
        verify(userService, times(1)).getById(1);
        verify(repository, times(1)).findByUser(sampleUser);
    }

    // --- orderProducts ---

    @Test
    void orderProducts_ShouldCreateOrderAndPurchasedProducts_WhenValidInput() throws NotFoundResourceException {
        // Given
        CartDTO cart = new CartDTO(Map.of(10, (byte) 2), LocalDateTime.now());
        List<PurchasedProduct> purchasedProducts = List.of(new PurchasedProduct());
        when(cartService.isEmpty()).thenReturn(false);
        when(cartService.toPurchasedProducts(sampleOrder)).thenReturn(purchasedProducts);
        when(userService.getById(1)).thenReturn(sampleUser);
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        // When
        orderService.orderProducts(1, cart);

        // Then
        verify(userService, times(1)).getById(1);
        verify(cartService).from(cart);
        verify(cartService).toPurchasedProducts(sampleOrder);
        verify(repository, times(1)).save(any(Order.class));
        verify(purchasedProductRepository).saveAll(purchasedProducts);
    }

    @Test
    void orderProducts_ShouldThrowIllegalArgumentException_WhenCartIsEmpty() {
        // Given
        CartDTO emptyCart = new CartDTO(Collections.emptyMap(), LocalDateTime.now());
        when(cartService.isEmpty()).thenReturn(true);

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> orderService.orderProducts(1, emptyCart));

        verify(cartService).from(emptyCart);
        verify(cartService, never()).toPurchasedProducts(any());
        verify(userService, never()).getById(any());
        verify(repository, never()).save(any());
        verify(purchasedProductRepository, never()).saveAll(any());
    }

    // --- refund ---

    @Test
    void refund_ShouldSaveOrder_WhenRefundIsValid() throws NotFoundResourceException {
        sampleOrder.setStatus(orderStatusMock);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusMock.refund()).thenReturn(OrderStatus.IN_PROCESS);

        orderService.refund(1);

        verify(repository, times(1)).save(sampleOrder);
    }

    @Test
    void refund_ShouldThrowOrderNotCanceledException_WhenStatusAfterRefundIsNull() {
        sampleOrder.setStatus(orderStatusMock);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusMock.refund()).thenReturn(null);

        assertThrows(OrderNotCanceledException.class, () -> orderService.refund(1));
        verify(repository, never()).save(sampleOrder);
    }

    // --- changeStatus ---

    @Test
    void changeStatus_ShouldSaveOrder_WhenStatusChanges() throws NotFoundResourceException {
        sampleOrder.setStatus(orderStatusMock);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusMock.next(false)).thenReturn(OrderStatus.DELIVERED);

        orderService.changeStatus(1, false);

        verify(repository, times(1)).save(sampleOrder);
    }

    @Test
    void changeStatus_ShouldNotSaveOrder_WhenStatusDoesNotChange() throws NotFoundResourceException {
        sampleOrder.setStatus(orderStatusMock);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusMock.next(false)).thenReturn(orderStatusMock);

        orderService.changeStatus(1, false);

        verify(repository, never()).save(sampleOrder);
    }
}
