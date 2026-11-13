package com.vianavitor.ecommerce_tech.ecommerce.repositories;

import com.vianavitor.ecommerce_tech.ecommerce.entities.RatingHistory;
import com.vianavitor.ecommerce_tech.ecommerce.entities.RatingHistoryId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatingHistoryRepository extends CrudRepository<RatingHistory, RatingHistoryId> {
}
