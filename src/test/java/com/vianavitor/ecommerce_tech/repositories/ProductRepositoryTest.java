package com.vianavitor.ecommerce_tech.repositories;


import com.vianavitor.ecommerce_tech.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.models.Product;
import com.vianavitor.ecommerce_tech.models.Ssd;
import com.vianavitor.ecommerce_tech.models.aux.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdFormFactor;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdInterface;
import com.vianavitor.ecommerce_tech.models.aux.enums.SsdProtocol;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

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

        entityManager.persistAndFlush(ssd);
    }

    @Test
    public void searchByNameContainingTest__whenItsSSD() {
        String name = "SSD de 512GB";

        List<ProductSearchResultsDTO> results = repository.searchByNameContaining(name);

        Assert.notEmpty(results, "No results found when it were expected to");
    }

    @Test
    public void searchByCategoryAndNameContainingTest__whenItsSSD() {
        String nameInput = "SSD da Adata";
        ProductCategory categoryInput = ProductCategory.SSD;

        List<ProductSearchResultsDTO> results = repository.searchByCategoryAndNameContaining(categoryInput, nameInput);

        Assert.notEmpty(results, "No results found when it were expected to");
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
