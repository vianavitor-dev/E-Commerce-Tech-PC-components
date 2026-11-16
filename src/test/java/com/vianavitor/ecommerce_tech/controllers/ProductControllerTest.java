package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.ecommerce.controllers.ProductController;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.ecommerce.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService service;
    @InjectMocks
    private ProductController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/products", "/api/products/all"})
    void defaultsToFirstPageOfTwentySortedById(String path) throws Exception {
        var pageable = PageRequest.of(0, 20, Sort.by("id"));
        when(service.findAll(pageable)).thenReturn(new PageImpl<ProductSearchResultsDTO>(List.of(), pageable, 0));

        mvc.perform(get(path)).andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.number").value(0))
                .andExpect(jsonPath("$.size").value(20))
                .andExpect(jsonPath("$.totalElements").value(0));
        verify(service).findAll(pageable);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "name=SSD", "category=SSD", "name=SSD&category=SSD"})
    void passesPaginationAndFiltersToService(String filters) throws Exception {
        var pageable = PageRequest.of(1, 2, Sort.by("price").descending());
        var page = new PageImpl<ProductSearchResultsDTO>(List.of(), pageable, 2);
        if (filters.contains("name") && filters.contains("category")) {
            when(service.findByCategoryAndName(ProductCategory.SSD, "SSD", pageable)).thenReturn(page);
        } else if (filters.contains("name")) {
            when(service.findByName("SSD", pageable)).thenReturn(page);
        } else if (filters.contains("category")) {
            when(service.findByCategory(ProductCategory.SSD, pageable)).thenReturn(page);
        } else {
            when(service.findAll(pageable)).thenReturn(page);
        }

        mvc.perform(get("/api/products?page=1&size=2&sort=price,desc&" + filters))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty())
                .andExpect(jsonPath("$.number").value(1))
                .andExpect(jsonPath("$.size").value(2))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.totalPages").value(1));
    }
}
