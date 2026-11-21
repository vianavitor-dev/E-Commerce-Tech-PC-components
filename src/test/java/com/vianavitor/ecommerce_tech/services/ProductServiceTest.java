package com.vianavitor.ecommerce_tech.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.ProductRepository;
import com.vianavitor.ecommerce_tech.ecommerce.services.ProductService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @Test
    void findAllPreservesPaginationAndMapsProductFields() {
        var pageable = PageRequest.of(1, 2, Sort.by("id"));
        Product product = new Product();
        product.setId(3);
        product.setName("SSD");
        product.setCategory(ProductCategory.SSD);
        product.setBrand("Adata");
        product.setPrice(java.math.BigDecimal.TEN);
        product.setRating(java.math.BigDecimal.ONE);
        product.setRatedCount(4);
        when(repository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(product), pageable, 3));

        Page<ProductSearchResultsDTO> result = service.findAll(pageable);

        Assertions.assertThat(result.getNumber()).isEqualTo(1);
        Assertions.assertThat(result.getSize()).isEqualTo(2);
        Assertions.assertThat(result.getTotalElements()).isEqualTo(3);
        Assertions.assertThat(result.getTotalPages()).isEqualTo(2);
        Assertions.assertThat(result.getContent().getFirst())
                .usingRecursiveComparison().isEqualTo(new ProductService.ProductSearchResultsDTOImpl(product));
        verify(repository).findAll(pageable);
    }

    @Test
    void filteredSearchesPreservePageableAndMetadata() {
        var pageable = PageRequest.of(2, 5, Sort.by("price").descending());
        Page<ProductSearchResultsDTO> emptyPage = new PageImpl<>(List.of(), pageable, 8);
        when(repository.searchByNameContaining("SSD", pageable)).thenReturn(emptyPage);
        when(repository.findByCategory(ProductCategory.SSD, pageable)).thenReturn(emptyPage);
        when(repository.searchByCategoryAndNameContaining(ProductCategory.SSD, "SSD", pageable)).thenReturn(emptyPage);

        Assertions.assertThat(service.findByName("SSD", pageable)).isSameAs(emptyPage);
        Assertions.assertThat(service.findByCategory(ProductCategory.SSD, pageable)).isSameAs(emptyPage);
        Assertions.assertThat(service.findByCategoryAndName(ProductCategory.SSD, "SSD", pageable)).isSameAs(emptyPage);
    }

}
