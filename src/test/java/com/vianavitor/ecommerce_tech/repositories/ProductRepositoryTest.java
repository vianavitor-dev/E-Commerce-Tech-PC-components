package com.vianavitor.ecommerce_tech.repositories;


import com.vianavitor.ecommerce_tech.aux.TestcontainerClass;
import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Ssd;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.ProductRepository;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.SsdProtocol;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.Optional;

@SpringBootTest
public class ProductRepositoryTest extends TestcontainerClass {
    @Autowired
    private ProductRepository repository;

    @Autowired
    public TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager entityManager;

    Product product;

    @BeforeEach
    void setUp() {
        Ssd ssd = new Ssd(
                // Product values
                "SSD Adata SU650, 512GB, SATA III, 2.5\", Leitura: 520MB/s, Gravação: 450MB/s, Preto - ASU650SS-512GT-R",
                "SSD-ADATASU650-512-SATA3",
                BigDecimal.valueOf(0.0),
                0,
                "Adata",
                ProductCategory.SSD,
                "",
                "",
                BigDecimal.valueOf(638.22),
                Short.valueOf("10"),
                // SSD values
                "ASU650SS-512GT-R",
                Short.valueOf("512"),
                SsdInterface.SATA,
                SsdFormFactor.INCH_2_5,
                SsdProtocol.AHCI,
                Short.valueOf("520"),
                Short.valueOf("420")
        );

        product = transactionTemplate.execute(status -> {
            entityManager.persist(ssd);
            return ssd;
        });
    }

    @AfterEach
    void tearDown() {
        if (product != null) {
            transactionTemplate.executeWithoutResult(status -> {
                Product managedProduct = entityManager.merge(product);
                entityManager.remove(managedProduct);
                entityManager.flush();
            });
        }
    }

    @Test
    void paginatesAllSearchVariantsWithAccurateTotals() {
        List<Product> fixtures = new ArrayList<>();
        try {
            transactionTemplate.executeWithoutResult(status -> {
                for (int index = 0; index < 3; index++) {
                    Product fixture = new Product();
                    fixture.setName("Paginationfixture " + index);
                    fixture.setSku("PAGINATION-FIXTURE-" + index);
                    fixture.setCategory(ProductCategory.SSD);
                    fixture.setBrand("Pagination");
                    fixture.setShortDescription("");
                    fixture.setTechnicalDescription("");
                    fixture.setPrice(BigDecimal.TEN);
                    fixture.setRating(BigDecimal.ZERO);
                    fixture.setStock((short) 1);
                    entityManager.persist(fixture);
                    fixtures.add(fixture);
                }
            });

            assertPages(pageable -> repository.searchByNameContaining("Paginationfixture", pageable), 3);
            assertPages(pageable -> repository.searchByCategoryAndNameContaining(
                    ProductCategory.SSD, "Paginationfixture", pageable), 3);
            long categoryTotal = repository.findByCategory(ProductCategory.SSD, PageRequest.of(0, 1)).getTotalElements();
            assertPages(pageable -> repository.findByCategory(ProductCategory.SSD, pageable), categoryTotal);
            long total = repository.findAll(PageRequest.of(0, 1)).getTotalElements();
            assertPages(repository::findAll, total);
        } finally {
            transactionTemplate.executeWithoutResult(status -> fixtures.forEach(fixture ->
                    entityManager.remove(entityManager.merge(fixture))));
        }
    }

    private void assertPages(Function<Pageable, ? extends Page<?>> search, long total) {
        var first = search.apply(PageRequest.of(0, 1, Sort.by("id")));
        var second = search.apply(PageRequest.of(1, 1, Sort.by("id")));
        var beyond = search.apply(PageRequest.of((int) total, 1, Sort.by("id")));

        assertThat(first.getContent()).hasSize(1);
        assertThat(second.getContent()).hasSize(1);
        Object firstId = first.getContent().getFirst() instanceof Product product
                ? product.getId() : ((ProductSearchResultsDTO) first.getContent().getFirst()).getId();
        Object secondId = second.getContent().getFirst() instanceof Product product
                ? product.getId() : ((ProductSearchResultsDTO) second.getContent().getFirst()).getId();
        assertThat(firstId).isNotEqualTo(secondId);
        assertThat(first.getTotalElements()).isEqualTo(total);
        assertThat(second.getTotalElements()).isEqualTo(total);
        assertThat(first.getTotalPages()).isEqualTo((int) total);
        assertThat(beyond.getContent()).isEmpty();
        assertThat(beyond.getTotalElements()).isEqualTo(total);
    }

    @Test
    public void searchByNameContainingTest__whenItsSSD() {
        String name = "SSD de 512 GB";

        Page<ProductSearchResultsDTO> results = repository.searchByNameContaining(name, PageRequest.of(0, 1));
        Assert.notEmpty(results.getContent(), "No results found when it were expected to");
        Assert.isTrue(results.stream()
                        .filter(dto -> !dto.getCategory().equals(ProductCategory.SSD))
                        .findAny()
                        .isEmpty(),
                "It should return only SSDs"
        );
    }

    @Test
    public void searchByCategoryAndNameContainingTest__whenItsSSD() {
        String nameInput = "SSD da Adata";
        ProductCategory categoryInput = ProductCategory.SSD;

        Page<ProductSearchResultsDTO> results = repository.searchByCategoryAndNameContaining(categoryInput, nameInput, PageRequest.of(0, 1));

        Assert.notEmpty(results.getContent(), "No results found when it were expected to");
        Assert.isTrue(results.getContent().getFirst().getBrand().equals("Adata"), "The first item were expected to be from Adata brand");
    }

    @Test
    public void findBySkuTest__whenItsSSD() {
        String sku = "SSD-ADATASU650-512-SATA3";
        Optional<Product> result = repository.findBySku(sku);

        Assert.isTrue(result.isPresent(), "This result cannot be null");
        Assert.isTrue(
                result.get().getCategory().equals(ProductCategory.SSD),
                "This result must be from the SSD category"
        );
    }
}
