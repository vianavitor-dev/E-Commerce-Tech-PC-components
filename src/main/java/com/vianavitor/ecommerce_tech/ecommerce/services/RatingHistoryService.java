package com.vianavitor.ecommerce_tech.ecommerce.services;

import com.vianavitor.ecommerce_tech.ecommerce.entities.Product;
import com.vianavitor.ecommerce_tech.ecommerce.entities.RatingHistory;
import com.vianavitor.ecommerce_tech.ecommerce.entities.RatingHistoryId;
import com.vianavitor.ecommerce_tech.ecommerce.repositories.RatingHistoryRepository;
import com.vianavitor.ecommerce_tech.shared.exceptions.NotFoundResourceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class RatingHistoryService {
    @Autowired
    private RatingHistoryRepository repository;

    @Transactional
    public void addRate(Integer userId, Product product, BigDecimal rate) {
        var previous = repository.findById(new RatingHistoryId(userId, product.getId()));
        int firstRate = previous.isEmpty() ? 1 : 0;
        BigDecimal previousRate = previous.map(RatingHistory::getRate).orElse(BigDecimal.ZERO);
        BigDecimal storedRate = rate.setScale(1, RoundingMode.HALF_UP);
        RatingHistory history = previous.orElseGet(() -> new RatingHistory(userId, product.getId(), storedRate));
        history.setRate(storedRate);
        repository.save(history);

        int count = product.getRatedCount();
        BigDecimal lastSum = product.getRating().multiply(BigDecimal.valueOf(count));
        BigDecimal currentSum = lastSum.subtract(previousRate).add(storedRate);
        int updatedCount = count + firstRate;
        BigDecimal average = currentSum.divide(BigDecimal.valueOf(updatedCount), 1, RoundingMode.HALF_UP);

        product.setRating(average);
        product.setRatedCount(updatedCount);
    }

    @Transactional
    public RatingHistory clearRateFrom(Product product, Integer userId) throws NotFoundResourceException {
        RatingHistory history = repository.findById(new RatingHistoryId(userId, product.getId()))
                .orElseThrow(() -> new NotFoundResourceException("This user have never rated this product"));

        int count = product.getRatedCount();
        BigDecimal lastSum = product.getRating().multiply(BigDecimal.valueOf(count));
        BigDecimal currentSum = lastSum.subtract(history.getRate());
        BigDecimal average = count > 1
                ? currentSum.divide(BigDecimal.valueOf(count - 1), 1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO.setScale(1, RoundingMode.HALF_DOWN);

        product.setRating(average);
        product.setRatedCount(count - 1);
        return history;
    }

    @Transactional
    public void removeRate(RatingHistory history) throws NotFoundResourceException {
        if (!repository.existsById(new RatingHistoryId(history.getUserId(), history.getProductId()))) {
            throw new NotFoundResourceException("This user have never rated this product");
        }
        repository.delete(history);
    }

}
