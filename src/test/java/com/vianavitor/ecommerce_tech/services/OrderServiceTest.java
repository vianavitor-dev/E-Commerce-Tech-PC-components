package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.response.UsersOrderDTO;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.models.aux.enums.OrderStatus;
import com.vianavitor.ecommerce_tech.repositories.OrderRepository;
import com.vianavitor.ecommerce_tech.repositories.PurchasedProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository repository;

    @Mock
    private ProductService productService;

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
    private Product sampleProduct;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleOrder = new Order();
        sampleOrder.setStatus(OrderStatus.IN_PROCESS);

        sampleProduct = new Product();
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

// --- orderProducts ---

    @Test
    void orderProducts_ShouldCreateOrderAndPurchasedProducts_WhenValidInput() throws NotFoundResourceException {
        // Given
        Map<Integer, Byte> cart = Map.of(10, (byte) 2);
        when(userService.getById(1)).thenReturn(sampleUser);
        when(productService.findById(10)).thenReturn(sampleProduct);
        when(repository.save(any(Order.class))).thenReturn(sampleOrder);

        // When
        orderService.orderProducts(1, cart);

        // Then
        verify(userService, times(1)).getById(1);
        verify(productService, times(1)).findById(10);
        verify(repository, times(1)).save(any(Order.class));
        verify(purchasedProductRepository, times(1)).saveAll(anyList());
    }

    @Test
    void orderProducts_ShouldThrowIllegalArgumentException_WhenCartIsEmpty() {
        // Given
        Map<Integer, Byte> emptyCart = Collections.emptyMap();

        // When / Then
        assertThrows(IllegalArgumentException.class, () -> orderService.orderProducts(1, emptyCart));

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
    void refund_ShouldThrowIllegalArgumentException_WhenStatusAfterRefundIsNull() {
        sampleOrder.setStatus(orderStatusMock);
        when(repository.findById(1)).thenReturn(Optional.of(sampleOrder));
        when(orderStatusMock.refund()).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () -> orderService.refund(1));
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