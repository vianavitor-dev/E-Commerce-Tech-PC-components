package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.dtos.response.OrdersProductDTO;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Order;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.PurchasedProduct;
import com.vianavitor.ecommerce_tech.models.User;
import com.vianavitor.ecommerce_tech.models.aux.enums.OrderStatus;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.repositories.PurchasedProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PurchasedProductServiceTest {

    @Mock
    private PurchasedProductRepository repository;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PurchasedProductService purchasedProductService;

    private Order sampleOrder;
    private PurchasedProduct samplePurchasedProduct;

    @BeforeEach
    void setUp() {
        User sampleUser = new User();
        sampleUser.setId(1);
        sampleUser.setName("John Doe");

        sampleOrder = new Order(
                1,
                sampleUser,
                OrderStatus.IN_PROCESS,
                LocalDate.now(),
                LocalDate.now()
        );

        Product sampleProduct = new Product(
                10,
                "Core i7-13700K",
                "CPU-13700K",
                BigDecimal.valueOf(4.8),
                120,
                "Intel",
                ProductCategory.CPU,
                "High performance desktop processor",
                "16 cores (8 P-cores + 8 E-cores), up to 5.4 GHz",
                BigDecimal.valueOf(389.99),
                (short) 15
        );

        samplePurchasedProduct = new PurchasedProduct(
                100,
                sampleProduct,
                sampleOrder,
                (byte) 2
        );
    }

    @Test
    void getOrderProducts_ShouldReturnMappedDTOList_WhenOrderExists() throws NotFoundResourceException {
        when(orderService.findById(1)).thenReturn(sampleOrder);
        when(repository.findByOrder(sampleOrder)).thenReturn(List.of(samplePurchasedProduct));

        List<OrdersProductDTO> result = purchasedProductService.getOrderProducts(1);

        assertNotNull(result);
        assertEquals(1, result.size());

        OrdersProductDTO dto = result.get(0);
        assertEquals(10, dto.id());
        assertEquals("Core i7-13700K", dto.name());
        assertEquals("Intel", dto.brand());
        assertEquals(ProductCategory.CPU, dto.category());
        assertEquals(BigDecimal.valueOf(389.99), dto.price());
        assertEquals((byte) 2, dto.amount());

        assertNotNull(dto.user());
        assertEquals(1, dto.user().id());
        assertEquals("John Doe", dto.user().name());

        verify(orderService, times(1)).findById(1);
        verify(repository, times(1)).findByOrder(sampleOrder);
    }

    @Test
    void getOrderProducts_ShouldPropagateException_WhenOrderNotFound() throws NotFoundResourceException {
        when(orderService.findById(1)).thenThrow(new NotFoundResourceException("This order does not exists"));

        assertThrows(NotFoundResourceException.class, () -> purchasedProductService.getOrderProducts(1));

        verify(orderService, times(1)).findById(1);
        verify(repository, never()).findByOrder(any());
    }
}