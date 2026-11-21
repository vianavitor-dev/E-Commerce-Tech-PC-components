package com.vianavitor.ecommerce_tech.controllers;

import com.vianavitor.ecommerce_tech.ecommerce.controllers.ProductController;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.request.PcComponentsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.PcCompatibilityCheckResultDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.CompatibleReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.aux.PcComponentsReportDTO;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.ecommerce.services.ProductService;
import com.vianavitor.ecommerce_tech.ecommerce.services.CompatibilityCheckService;
import com.vianavitor.ecommerce_tech.ecommerce.services.compatibility.CompatibilityCheckFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {
    @Mock
    private ProductService service;
    @Mock
    private CompatibilityCheckService compatibilityCheckService;
    @InjectMocks
    private ProductController controller;
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver()).build();
    }

    @Test
    void compatibilityEndpointUsesDedicatedServiceAndPreservesResponse() throws Exception {
        var report = PcComponentsReportDTO.builder()
                .cpu(new CompatibleReportDTO(true, "CPU", null)).build();
        when(compatibilityCheckService.checkPcComponentsCompatibility(any(PcComponentsDTO.class)))
                .thenReturn(new PcCompatibilityCheckResultDTO(report, ""));

        mvc.perform(post("/api/products/compatibility-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"motherboard\":{},\"psu\":{},\"cpu\":{\"socket\":\"AM5\"}}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.componentsReport.cpu.pcComponentName").value("CPU"))
                .andExpect(jsonPath("$.componentsReport.cpu.isPcComponentCompatible").value(true))
                .andExpect(jsonPath("$.extraInformation").value(""))
                .andExpect(jsonPath("$.areAllComponentsCompatible").value(true))
                .andExpect(jsonPath("$.compatibleComponentsCount").value(1))
                .andExpect(jsonPath("$.incompatibleComponentsCount").value(0));
        verify(compatibilityCheckService).checkPcComponentsCompatibility(argThat(request ->
                request.getCpu().getSocket().equals("AM5")
                        && request.getMotherboard() != null && request.getPsu() != null));
    }

    @Test
    void compatibilityEndpointAcceptsRamModulesWithoutMotherboardOrPsu() throws Exception {
        var realService = new CompatibilityCheckService(new CompatibilityCheckFactory());
        when(compatibilityCheckService.checkPcComponentsCompatibility(any(PcComponentsDTO.class)))
                .thenAnswer(invocation -> realService.checkPcComponentsCompatibility(invocation.getArgument(0)));

        mvc.perform(post("/api/products/compatibility-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"rams":[
                                  {"name":"RAM 1","ddrGeneration":"DDR4","formFactor":"DIMM"},
                                  {"name":"RAM 2","ddrGeneration":"DDR5","formFactor":"DIMM"}
                                ]}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hasCompatibilityChecks").value(true))
                .andExpect(jsonPath("$.areAllComponentsCompatible").value(false))
                .andExpect(jsonPath("$.compatibleComponentsCount").value(0))
                .andExpect(jsonPath("$.incompatibleComponentsCount").value(2))
                .andExpect(jsonPath("$.componentsReport.rams[0].reportText")
                        .value("Selected RAM modules must use the same DDR generation.\n"));
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
