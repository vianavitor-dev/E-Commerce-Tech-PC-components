package com.vianavitor.ecommerce_tech.ecommerce.services;

import com.vianavitor.ecommerce_tech.ecommerce.dtos.response.ProductSearchResultsDTO;
import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import com.vianavitor.ecommerce_tech.ecommerce.entities.enums.ProductCategory;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.ProductRepository;
import com.vianavitor.ecommerce_tech.auth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

// TODO: create a Inventory System to manage the products
@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RatingHistoryService ratingHistoryService;

    @Transactional
    public BigDecimal rateProduct(Integer productId, Integer userId, BigDecimal rate) throws NotFoundResourceException {
        Product product = repository.findByIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided ID code"));
        boolean userExists = userRepository.existsById(userId);

        if (!userExists) {
            throw new NotFoundResourceException("Not found user with the provided ID");
        }

        ratingHistoryService.addRate(userId, product, rate);
        repository.save(product);

        return product.getRating();
    }

    @Transactional
    public BigDecimal removesRate(Integer productId, Integer userId) throws NotFoundResourceException {
        Product product = repository.findByIdForUpdate(productId)
                .orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided ID code"));

        boolean userExists = userRepository.existsById(userId);

        if (!userExists) {
            throw new NotFoundResourceException("Not found user with the provided ID");
        }

        var history = ratingHistoryService.clearRateFrom(product, userId);
        product = repository.save(product);

        ratingHistoryService.removeRate(history);

        return product.getRating();
    }

    public Product findById(Integer id) throws NotFoundResourceException {
        return repository.findById(id).
                orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided ID code"
                ));
    }

    public Product findBySku(String sku) throws NotFoundResourceException {
        return repository.findBySku(sku).
                orElseThrow(() -> new NotFoundResourceException(
                        "Not found any product with the provided SKU code"
                ));
    }

    public Page<ProductSearchResultsDTO> findByName(String name, Pageable pageable) {
        return repository.searchByNameContaining(name, pageable);
    }

    public Page<ProductSearchResultsDTO> findByCategory(ProductCategory category, Pageable pageable) {
        return repository.findByCategory(category, pageable);
    }

    public Page<ProductSearchResultsDTO> findByCategoryAndName(ProductCategory category, String name, Pageable pageable) {
        return repository.searchByCategoryAndNameContaining(category, name, pageable);
    }

//    @AllArgsConstructor
    public static class ProductSearchResultsDTOImpl implements ProductSearchResultsDTO {
        Integer id;
        String name;
        ProductCategory category;
        BigDecimal rating;
        int ratedCount;
        String brand;
        BigDecimal price;

        public ProductSearchResultsDTOImpl(Product p) {
            this.id = p.getId();
            this.name = p.getName();
            this.category = p.getCategory();
            this.rating = p.getRating();
            this.ratedCount = p.getRatedCount();
            this.brand = p.getBrand();
            this.price = p.getPrice();
        }

        @Override
        public Integer getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public ProductCategory getCategory() {
            return category;
        }

        @Override
        public BigDecimal getRating() {
            return rating;
        }

        @Override
        public int getRatedCount() {
            return ratedCount;
        }

        @Override
        public String getBrand() {
            return brand;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }
    }

    public Page<ProductSearchResultsDTO> findAll(Pageable pageable) {
        return repository.findAll(pageable)
                .map(ProductSearchResultsDTOImpl::new);
    }

}
