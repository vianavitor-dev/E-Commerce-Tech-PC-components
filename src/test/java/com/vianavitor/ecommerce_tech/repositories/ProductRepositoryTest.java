package com.vianavitor.ecommerce_tech.repositories;


import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.Ssd;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ActiveProfiles("test")
@SpringBootTest
@Testcontainers
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @Autowired
    public TransactionTemplate transactionTemplate;

    @Container
    @ServiceConnection
    static MySQLContainer container = new MySQLContainer(DockerImageName.parse("mysql:latest"));

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
                (short) 10,
                // SSD values
                "ASU650SS-512GT-R",
                (byte) 512,
                SsdInterface.SATA,
                SsdFormFactor.INCH_2_5,
                SsdProtocol.AHCI,
                (short) 520,
                (short) 420
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
    public void searchByNameContainingTest__whenItsSSD() {
        String name = "SSD de 512 GB";

        List<ProductSearchResultsDTO> results = repository.searchByNameContaining(name);
        Assert.notEmpty(results, "No results found when it were expected to");
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

        List<ProductSearchResultsDTO> results = repository.searchByCategoryAndNameContaining(categoryInput, nameInput);

        Assert.notEmpty(results, "No results found when it were expected to");
        Assert.isTrue(results.getFirst().getBrand().equals("Adata"), "The first item were expected to be from Adata brand");
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
